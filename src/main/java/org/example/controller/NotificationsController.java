package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.service.IService.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notification sending, templates, and user notification history")
public class NotificationsController {

    @Autowired
    private INotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send Notification", description = "Send a single notification to a user via a channel (email, sms, push).", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Notification payload", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"recipientId\": 1, \"message\": \"Your claim has been approved\", \"type\": \"claim_update\", \"channel\": \"email\"}"))))
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> body) {
        try {
            Long recipientId = Long.valueOf(body.get("recipientId").toString());
            String message = String.valueOf(body.get("message"));
            String type = String.valueOf(body.getOrDefault("type", "general"));
            String channel = String.valueOf(body.getOrDefault("channel", "email"));

            boolean success = notificationService.sendNotification(recipientId, message, channel, type);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "User Notifications", description = "Get paginated notification history for a user.", parameters = {
            @Parameter(name = "userId", description = "User ID", required = true, example = "1"),
            @Parameter(name = "limit", description = "Page size", required = false, example = "20", schema = @Schema(defaultValue = "20")),
            @Parameter(name = "offset", description = "Offset for pagination", required = false, example = "0", schema = @Schema(defaultValue = "0"))
    })
    public ResponseEntity<?> getUserNotifications(@PathVariable("userId") Long userId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            List<Map<String, Object>> notifications = notificationService.getNotificationHistory(userId, limit, offset);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{notificationId}/mark-read")
    @Operation(summary = "Mark Notification as Read", description = "Mark a specific notification as read for a user.", parameters = {
            @Parameter(name = "notificationId", description = "Notification ID", required = true, example = "10"),
            @Parameter(name = "userId", description = "User ID", required = true, example = "1")
    })
    public ResponseEntity<?> markAsRead(@PathVariable("notificationId") Long notificationId,
            @RequestParam("userId") Long userId) {
        try {
            boolean success = notificationService.markNotificationAsRead(notificationId, userId);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Unread Notifications", description = "Get all unread notifications for a user.", parameters = {
            @Parameter(name = "userId", description = "User ID", required = true, example = "1") })
    public ResponseEntity<?> getUnreadNotifications(@PathVariable("userId") Long userId) {
        try {
            List<Map<String, Object>> notifications = notificationService.getUnreadNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/templates")
    @Operation(summary = "Create Notification Template", description = "Create a reusable notification template for a given channel.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Template payload", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"templateName\": \"CLAIM_UPDATE\", \"subject\": \"Claim Update\", \"content\": \"Your claim status is {{status}}\", \"channel\": \"email\"}"))))
    public ResponseEntity<?> createTemplate(@RequestBody Map<String, Object> body) {
        try {
            String templateName = String.valueOf(body.get("templateName"));
            String subject = String.valueOf(body.get("subject"));
            String content = String.valueOf(body.get("content"));
            String channel = String.valueOf(body.getOrDefault("channel", "email"));

            Map<String, Object> template = notificationService.createNotificationTemplate(templateName, subject,
                    content, channel);
            return ResponseEntity.ok(template);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/templates")
    @Operation(summary = "List Notification Templates", description = "List available notification templates, optionally filtered by channel and type.", parameters = {
            @Parameter(name = "channel", description = "Notification channel", required = false, example = "email"),
            @Parameter(name = "type", description = "Template type", required = false, example = "claim_update")
    })
    public ResponseEntity<?> getTemplates(@RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "type", required = false) String type) {
        try {
            List<Map<String, Object>> templates = notificationService.getNotificationTemplates(channel, type);
            return ResponseEntity.ok(templates);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/bulk")
    @Operation(summary = "Send Bulk Notifications", description = "Send a notification to multiple users at once.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Bulk notification payload", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"recipientIds\": [1,2,3], \"message\": \"System maintenance tonight\", \"type\": \"system\", \"channel\": \"email\"}"))))
    public ResponseEntity<?> sendBulkNotification(@RequestBody Map<String, Object> body) {
        try {
            Object idsObj = body.get("recipientIds");
            List<Long> recipientIds = idsObj instanceof List<?> list
                    ? list.stream().map(Object::toString).map(Long::valueOf).toList()
                    : List.of();
            String message = String.valueOf(body.get("message"));
            String type = String.valueOf(body.getOrDefault("type", "general"));
            String channel = String.valueOf(body.getOrDefault("channel", "email"));

            boolean success = notificationService.sendBulkNotification(recipientIds, message, channel, type);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
