package org.example.service.Serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.example.service.IService.INotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    private final Map<Long, Map<String, Object>> notifications = new ConcurrentHashMap<>();

    @Override
    public boolean sendNotification(Long recipientId, String message, String channel, String type) {
        recordNotification(recipientId, message, channel, type);
        return true;
    }

    @Override
    public boolean sendBulkNotification(List<Long> recipientIds, String message, String channel, String type) {
        for (Long id : recipientIds) {
            recordNotification(id, message, channel, type);
        }
        return true;
    }

    @Override
    public Map<String, Object> createNotificationTemplate(String templateName, String subject, String body,
            String channel) {
        Map<String, Object> tpl = new HashMap<>();
        tpl.put("templateName", templateName);
        tpl.put("subject", subject);
        tpl.put("body", body);
        tpl.put("channel", channel);
        tpl.put("templateId", UUID.randomUUID().toString());
        return tpl;
    }

    @Override
    public boolean sendTemplatedNotification(Long recipientId, String templateName, Map<String, Object> variables) {
        recordNotification(recipientId, "TEMPLATE:" + templateName,
                String.valueOf(variables.getOrDefault("channel", "email")), "templated");
        return true;
    }

    @Override
    public boolean sendSmsNotification(String phoneNumber, String message) {
        return true;
    }

    @Override
    public boolean sendEmailNotification(String email, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendPushNotification(Long userId, String title, String message, Map<String, Object> data) {
        return true;
    }

    @Override
    public boolean sendInAppNotification(Long userId, String message, String type) {
        recordNotification(userId, message, "in_app", type);
        return true;
    }

    @Override
    public boolean sendRecallUrgentNotification(String recallDetails, String urgencyLevel) {
        return true;
    }

    @Override
    public boolean sendPromotionNotification(Long customerId, String promotionDetails) {
        return true;
    }

    @Override
    public boolean notifyClaimStatusChange(Long claimId, String newStatus) {
        return true;
    }

    @Override
    public boolean sendWarrantyExpiryReminder(String vin, Integer daysUntilExpiry) {
        return true;
    }

    @Override
    public boolean notifyPartsShipped(Long claimId, String trackingNumber) {
        return true;
    }

    @Override
    public boolean sendClaimApprovalNotification(Long claimId, Long serviceCenterId) {
        return true;
    }

    @Override
    public boolean sendPasswordResetNotification(String email, String resetToken) {
        return true;
    }

    @Override
    public boolean sendAccountActivationNotification(Long userId) {
        return true;
    }

    @Override
    public boolean sendSystemMaintenanceNotification(List<Long> userIds, LocalDateTime maintenanceTime) {
        return true;
    }

    @Override
    public boolean sendSecurityAlertNotification(Long userId, String alertType, String details) {
        return true;
    }

    @Override
    public boolean updateNotificationPreferences(Long userId, Map<String, Boolean> channelPreferences) {
        return true;
    }

    @Override
    public Map<String, Boolean> getNotificationPreferences(Long userId) {
        Map<String, Boolean> prefs = new HashMap<>();
        prefs.put("email", true);
        prefs.put("sms", false);
        prefs.put("push", true);
        return prefs;
    }

    @Override
    public boolean subscribeToNotificationType(Long userId, String notificationType) {
        return true;
    }

    @Override
    public boolean unsubscribeFromNotificationType(Long userId, String notificationType) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getNotificationHistory(Long userId, int limit, int offset) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> n : notifications.values()) {
            if (userId.equals(n.get("recipientId")))
                list.add(n);
        }
        int from = Math.min(offset, list.size());
        int to = Math.min(from + limit, list.size());
        return list.subList(from, to);
    }

    @Override
    public Map<String, Object> getNotificationStatus(Long notificationId) {
        return notifications.get(notificationId);
    }

    @Override
    public boolean markNotificationAsRead(Long notificationId, Long userId) {
        Map<String, Object> n = notifications.get(notificationId);
        if (n == null)
            return false;
        n.put("read", true);
        return true;
    }

    @Override
    public List<Map<String, Object>> getUnreadNotifications(Long userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> n : notifications.values()) {
            if (userId.equals(n.get("recipientId")) && !Boolean.TRUE.equals(n.get("read")))
                list.add(n);
        }
        return list;
    }

    @Override
    public Map<String, Object> getNotificationDeliveryStats(LocalDateTime fromDate, LocalDateTime toDate) {
        return Map.of("sent", notifications.size(), "from", fromDate, "to", toDate);
    }

    @Override
    public Double getDeliverySuccessRate(String channel, LocalDateTime fromDate, LocalDateTime toDate) {
        return 1.0;
    }

    @Override
    public List<Map<String, Object>> getFailedNotifications(String channel, LocalDateTime fromDate,
            LocalDateTime toDate) {
        return List.of();
    }

    @Override
    public boolean retryFailedNotification(Long notificationId) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getNotificationTemplates(String channel, String type) {
        return List.of();
    }

    @Override
    public boolean updateNotificationTemplate(Long templateId, String subject, String body) {
        return true;
    }

    @Override
    public boolean deactivateNotificationTemplate(Long templateId) {
        return true;
    }

    private void recordNotification(Long recipientId, String message, String channel, String type) {
        long id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        Map<String, Object> n = new HashMap<>();
        n.put("id", id);
        n.put("recipientId", recipientId);
        n.put("message", message);
        n.put("channel", channel);
        n.put("type", type);
        n.put("success", true);
        n.put("sentAt", LocalDateTime.now());
        n.put("read", false);
        notifications.put(id, n);
    }
}
