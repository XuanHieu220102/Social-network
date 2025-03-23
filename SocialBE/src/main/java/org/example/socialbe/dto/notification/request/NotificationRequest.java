package org.example.socialbe.dto.notification.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NotificationRequest {
    private int page;
    private int size;
}
