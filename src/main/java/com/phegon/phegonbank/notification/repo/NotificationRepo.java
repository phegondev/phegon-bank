package com.phegon.phegonbank.notification.repo;

import com.phegon.phegonbank.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
