package org.example.socialbe.util;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class EmailSender {
    public static void sendNewPassword(String to, String newPassword) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("xuanhieu220102@gmail.com", "knxtyqxwdhekkyoq");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xuanhieu220102@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Password reset");
            message.setText("Your new password is: " + newPassword);

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void sendOrderInfo(String to, OrderEntity orderJson) throws MessagingException {
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                return new javax.mail.PasswordAuthentication("xuanhieu220102@gmail.com", "knxtyqxwdhekkyoq");
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("xuanhieu220102@gmail.com"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            message.setSubject("THÔNG TIN ĐƠN HÀNG");
//
//            // Tạo nội dung email dạng HTML
//            String content = "<html>" +
//                    "<body>" +
//                    "<h3>Thông tin đơn hàng của bạn</h3>" +
//                    "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse; width: 100%;'>" +
//                    "<tr><th>Mã đơn hàng</th><td>" + orderJson.getCode() + "</td></tr>" +
//                    "<tr><th>Trạng thái</th><td>" + (orderJson.getStatus() == 1 ? "Đã xử lý" : "Chưa xử lý") + "</td></tr>" +
//                    "<tr><th>Phí ship</th><td>" + String.format("%,.0f", orderJson.getShippingFee()) + " VND</td></tr>" +
//                    "<tr><th>Tổng tiền</th><td>" + String.format("%,.0f", orderJson.getTotalAmount()) + " VND</td></tr>" +
//                    "<tr><th>Phương thức thanh toán</th><td>" + orderJson.getPaymentMethod() + "</td></tr>" +
//                    "<tr><th>Ngày tạo đơn</th><td>" + orderJson.getCreateAt() + "</td></tr>" +
//                    "<tr><th>Địa chỉ nhận hàng</th><td>" + (orderJson.getAddressDetail() == null ? "Không có" : orderJson.getAddressDetail()) + "</td></tr>" +
//                    "</table>" +
//                    "<p>Cảm ơn bạn đã mua sắm tại cửa hàng của chúng tôi!</p>" +
//                    "</body>" +
//                    "</html>";
//
//            // Đặt kiểu nội dung là HTML
//            message.setContent(content, "text/html; charset=utf-8");
//
//            // Gửi email
//            Transport.send(message);
//            log.info("Email sent successfully!");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
