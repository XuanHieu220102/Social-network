package org.example.socialbe.dto.conversation.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationRequest {
    @Pattern(regexp = "PRIVATE|GROUP", message = "Type must be either 'PRIVATE' or 'GROUP'")
    private String type;
    private String name;
    private String senderId;
    private String receiverId;
}
