package com.phegon.phegonbank.notification.services;

import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.notification.dtos.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO, User user);
}
