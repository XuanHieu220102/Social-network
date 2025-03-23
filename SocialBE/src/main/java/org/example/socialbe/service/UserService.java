package org.example.socialbe.service;

import aj.org.objectweb.asm.TypeReference;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.socialbe.constant.Constant;
import org.example.socialbe.dto.BaseFilterRequest;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.users.request.*;
import org.example.socialbe.dto.users.response.AccessTokenPayload;
import org.example.socialbe.dto.users.response.UserFilterResponse;
import org.example.socialbe.dto.users.response.UserResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.enums.ErrorCode;
import org.example.socialbe.exception.LeaklessException;
import org.example.socialbe.repository.CommomRepositoryCustom;
import org.example.socialbe.repository.UsersRepository;
import org.example.socialbe.util.EmailSender;
import org.example.socialbe.util.HashUtil;
import org.example.socialbe.util.JsonUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Resource
    private UsersRepository usersRepository;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private CipherService cipherService;

    @Resource
    private CloudinaryService cloudinaryService;

    @Resource
    private Environment env;

    @Resource
    private JsonUtil jsonUtil;

    @Resource
    private CommomRepositoryCustom commomRepositoryCustom;

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = usersRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new LeaklessException(ErrorCode.INVALID_CREDENTIAL);
        }
        user.setFailedLoginCount(0);
        checkPassword(user, loginRequest.getPassword(), false);
        String accessToken = generateAccessTokenPayload(user);
        return new LoginResponse(accessToken, user.getFullName());
    }

    public ErrorResponse createUser(UserRequest request) {
        try {
            if (StringUtils.isEmpty(request.getUsername()) || StringUtils.isEmpty(request.getPassword())
             || StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPhoneNumber())) {
                throw new LeaklessException(ErrorCode.INVALID_CREDENTIAL);
            }
            if (usersRepository.existsByUsername(request.getUsername())) {
                throw new LeaklessException(ErrorCode.CREATE_FAILED,"Username already exists");
            }
            if (usersRepository.existsByEmail(request.getEmail())) {
                throw new LeaklessException(ErrorCode.CREATE_FAILED,"Email already exists");
            }
            UserEntity user = modelMapper.map(request, UserEntity.class);
            if (request.getAvatarUrl() != null) {
                String urlAvatar = cloudinaryService.uploadImage(request.getAvatarUrl());
                user.setAvatarUrl(urlAvatar);
                List<String> images = new ArrayList<>();
                images.add(urlAvatar);
                user.setListImages(images);
            }else {
                user.setAvatarUrl("https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg");
            }
            user.setStatus(Constant.UserStatus.ACTIVE);
            usersRepository.save(user);
            savePassword(user, request.getPassword());
            return ErrorResponse.success(user.getId());
        }catch (Exception ex) {
            log.error(ex.getMessage());
            return ErrorResponse.internalServerError(ex);
        }
    }

    public void savePassword(UserEntity user, String newPassword) {
        String passwordHash = hashPassword(user, newPassword);
        user.setPassword(passwordHash);
        user.setAccessTokenResetAt(LocalDateTime.now());
        usersRepository.save(user);
    }

    private String hashPassword(UserEntity user, String password) {
        String data = env.getProperty("password.salt") + user.getId() + password;
        return HashUtil.getSHA256Hash(data);
    }


    private String generateAccessTokenPayload(UserEntity user) {
        AccessTokenPayload payload = new AccessTokenPayload();
        payload.setUserId(user.getId());
        payload.setPadding(RandomStringUtils.randomAlphabetic(200));
        return cipherService.encrypt(payload);
    }

    private AccessTokenPayload getCurrentAccessTokenPayload() {
        String accessToken = httpServletRequest.getHeader("Authorization");
        if(StringUtils.isEmpty(accessToken)) {
            throw new LeaklessException(HttpStatus.UNAUTHORIZED, ErrorCode.ACCESS_DENIED);
        }

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.replaceFirst("Bearer ", "");
        }
        try {
            return cipherService.decrypt(accessToken, AccessTokenPayload.class);
        }catch (Exception ex) {
            throw new LeaklessException(HttpStatus.UNAUTHORIZED, ErrorCode.ACCESS_DENIED);
        }
    }

    private UserEntity getCurrentUser() {
        AccessTokenPayload payload = getCurrentAccessTokenPayload();
        UserEntity user = usersRepository.findById(payload.getUserId()).orElse(null);
        if (user.getAccessTokenResetAt() != null) {
            LocalDateTime accessTokenResetAt = payload.getCreatedAt();
            if (accessTokenResetAt.isBefore(user.getAccessTokenResetAt())) {
                throw new LeaklessException(HttpStatus.UNAUTHORIZED, ErrorCode.SESSION_EXPIRED);
            }
        }
        return user;
    }

    private void checkPassword(UserEntity user, String password, boolean already) {
        if (user.getFailedLoginCount() > 10) {
            throw new LeaklessException(ErrorCode.USER_LOCKED);
        }
        String passwordHash = hashPassword(user, password);
        if (!passwordHash.equals(user.getPassword())) {
            user.setFailedLoginCount(user.getFailedLoginCount() + 1);
            usersRepository.save(user);
            throw new LeaklessException(Boolean.TRUE.equals(already) ? ErrorCode.INVALID_PASSWORD : ErrorCode.INVALID_CREDENTIAL);
        }

        if (user.getFailedLoginCount() > 0) {
            user.setFailedLoginCount(0);
            usersRepository.save(user);
        }
    }

    public void changePassword(UserEntity user, ChangePasswordRequest request) {
        checkPassword(user, request.getCurrentPassword(), true);
        savePassword(user, request.getNewPassword());
    }

    public ErrorResponse forgetPassword(ForgetPasswordForm request) {
        try {
            UserEntity userExists = usersRepository.findByEmail(request.getEmail());
            if (userExists == null) {
                throw new LeaklessException(ErrorCode.EMAIL_NOT_EXIST);
            }
            String newPassword = generateRandomPassword(6);
            EmailSender.sendNewPassword(userExists.getEmail(), newPassword);
            savePassword(userExists, newPassword);
            return ErrorResponse.success("New password is sent is your email !");
        }catch (Exception ex) {
            log.error(ex.getMessage());
            throw new LeaklessException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }


    public UserEntity checkAuthentication() {
        UserEntity user = getCurrentUser();
        return user;
    }

    public UserResponse detail(String id) {
        UserEntity user = usersRepository.findById(id).orElse(null);
        if (user == null) {
            throw new LeaklessException(ErrorCode.RECORD_NOT_FOUND);
        }
        return modelMapper.map(user, UserResponse.class);
    }

    @Cacheable(value = "curentUser", key = "#user.username")
    public UserResponse currentUserInfo(UserEntity user) {
        return modelMapper.map(user, UserResponse.class);
    }

    @CacheEvict(value = "curentUser", key = "#user.username")
    public ErrorResponse updateProfilePicture(UserEntity user,UpdateProfilePictureRequest request) {
        try {
            String image = cloudinaryService.uploadImage(request.getImage());
            user.setAvatarUrl(image);
            List<String> images = user.getListImages();
            images.add(image);
            user.setListImages(images);
            usersRepository.save(user);
            return ErrorResponse.success(user.getId());
        }catch (Exception ex) {
            log.error("Has is error !");
            return ErrorResponse.internalServerError(ex);
        }
    }

    public  List<UserFilterResponse> searchUser(BaseFilterRequest request) {
        Page<UserEntity> userEntities = commomRepositoryCustom.searchUser(request);
        return modelMapper.map(userEntities.getContent(), new TypeToken<List<UserFilterResponse>>() {}.getType());
    }

    public List<UserFilterResponse> getListSuggestionUser(UserEntity user) {
        List<UserEntity> userEntities = usersRepository.findNonFriendsOrRejected(user.getId());
        return modelMapper.map(userEntities, new TypeToken<List<UserFilterResponse>>() {}.getType());

    }
}
