package org.example.socialbe.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static class UserStatus {
        public static final Integer ACTIVE = 2;
        public static final Integer INACTIVE = 1;
        public static final Integer DELETED = 3;
        public static final Integer LOCKED = 4;
    }

    public static class FriendStatus {
        public static final Integer PENDING = 1;
        public static final Integer ACCEPT = 2;
        public static final Integer BLOCK = 3;
        public static final Integer REJECT = 4;
    }

    public static class PostStatus {
        public static final Integer PRIVATE = 1;
        public static final Integer PUBLIC = 2;
        public static final Integer FRIEND = 3;
        public static final Integer CUSTOM = 4;
    }

    public static class LikeStatus {
        public static final Integer LIKE = 1;
        public static final Integer UNLIKE = 0;
    }

    public static class CommentStatus {
        public static final Integer PRESENTLY = 1;
        public static final Integer HIDDEN = 0;
    }

    public static class Role_Group {
        public static final String MEMBER = "member";
        public static final String ADMIN = "admin";
        public static final String MODERATOR = "moderator";
    }

    public static class GROUP_PRIVACY {
        public static final Integer PRIVATE = 0;
        public static final Integer PUBLIC = 1;
    }

    public static class SHARE_PRIVACY {
        public static final Integer PRIVATE = 0;
        public static final Integer PUBLIC = 1;
    }

    public static class TYPE_POST {
        public static final String NORMAL = "NORMAL";
        public static final String SHARED = "SHARED";
    }

    public static class PAGE_ROLE {
        public static final String ADMIN = "ADMIN";
        public static final String MEMBER = "MEMBER";
    }

    public static class PAGE_FOLLOW_STATUS {
        public static final Integer APPROVED = 1;
        public static final Integer INVITED = 2;
        public static final Integer WAITING = 3;
        public static final Integer BLOCKED = 4;
        public static final Integer REJECTED = 5;
    }

    public static class GROUP_MEMBER_STATUS {
        public static final Integer APPROVED = 1;
        public static final Integer INVITED = 2;
        public static final Integer WAITING = 3;
        public static final Integer BLOCKED = 4;
        public static final Integer REJECTED = 5;
    }

    public static class NotificationType {
        public static final String FRIEND_REQUEST = "FRIEND_REQUEST";
        public static final String LIKE = "LIKE";
        public static final String COMMENT = "COMMENT";
        public static final String SHARE = "SHARE";
        public static final String TAG = "TAG";
        public static final String SYSTEM = "SYSTEM";
    }
}
