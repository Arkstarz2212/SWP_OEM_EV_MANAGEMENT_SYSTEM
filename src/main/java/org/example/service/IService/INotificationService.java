package org.example.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface INotificationService {
    // Unified Notification Dispatch
    boolean sendNotification(Long recipientId, String message, String channel, String type);

    boolean sendBulkNotification(List<Long> recipientIds, String message, String channel, String type);

    Map<String, Object> createNotificationTemplate(String templateName, String subject, String body, String channel);

    boolean sendTemplatedNotification(Long recipientId, String templateName, Map<String, Object> variables);

    // Channel-Specific Messaging
    boolean sendSmsNotification(String phoneNumber, String message);

    boolean sendEmailNotification(String email, String subject, String body);

    boolean sendPushNotification(Long userId, String title, String message, Map<String, Object> data);

    boolean sendInAppNotification(Long userId, String message, String type);

    // Recall Notifications
    boolean sendRecallUrgentNotification(String recallDetails, String urgencyLevel);

    boolean sendPromotionNotification(Long customerId, String promotionDetails);

    // Warranty & Claim Notifications
    boolean notifyClaimStatusChange(Long claimId, String newStatus);

    boolean sendWarrantyExpiryReminder(String vin, Integer daysUntilExpiry);

    boolean notifyPartsShipped(Long claimId, String trackingNumber);

    boolean sendClaimApprovalNotification(Long claimId, Long serviceCenterId);

    // User & System Notifications
    boolean sendPasswordResetNotification(String email, String resetToken);

    boolean sendAccountActivationNotification(Long userId);

    boolean sendSystemMaintenanceNotification(List<Long> userIds, LocalDateTime maintenanceTime);

    boolean sendSecurityAlertNotification(Long userId, String alertType, String details);

    // Notification Preferences & Management
    boolean updateNotificationPreferences(Long userId, Map<String, Boolean> channelPreferences);

    Map<String, Boolean> getNotificationPreferences(Long userId);

    boolean subscribeToNotificationType(Long userId, String notificationType);

    boolean unsubscribeFromNotificationType(Long userId, String notificationType);

    // Notification History & Tracking
    List<Map<String, Object>> getNotificationHistory(Long userId, int limit, int offset);

    Map<String, Object> getNotificationStatus(Long notificationId);

    boolean markNotificationAsRead(Long notificationId, Long userId);

    List<Map<String, Object>> getUnreadNotifications(Long userId);

    // Delivery & Analytics
    Map<String, Object> getNotificationDeliveryStats(LocalDateTime fromDate, LocalDateTime toDate);

    Double getDeliverySuccessRate(String channel, LocalDateTime fromDate, LocalDateTime toDate);

    List<Map<String, Object>> getFailedNotifications(String channel, LocalDateTime fromDate, LocalDateTime toDate);

    boolean retryFailedNotification(Long notificationId);

    // Template Management
    List<Map<String, Object>> getNotificationTemplates(String channel, String type);

    boolean updateNotificationTemplate(Long templateId, String subject, String body);

    boolean deactivateNotificationTemplate(Long templateId);
}