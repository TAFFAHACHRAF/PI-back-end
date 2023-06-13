package org.sid.securityservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Date date;
    private String message;
    private String objet;
    private Boolean isRead;
    private Long userId;
}
