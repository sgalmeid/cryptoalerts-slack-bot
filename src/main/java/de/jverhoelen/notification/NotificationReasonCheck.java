package de.jverhoelen.notification;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationReasonCheck {

    private Map<CourseNotificationEvent, Boolean> notifiablePerSource;
    private CourseNotificationEvent notificationReason;
    private boolean shouldBeNotified;

    public NotificationReasonCheck(Map<CourseNotificationEvent, Boolean> notifiablePerSource) {
        this.notifiablePerSource = notifiablePerSource;

        Map<CourseNotificationEvent, Boolean> allNotifiables = notifiablePerSource
                .entrySet().stream()
                .filter(notifiable -> notifiable.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!allNotifiables.isEmpty()) {
            this.shouldBeNotified = true;
            this.notificationReason = new ArrayList<>(allNotifiables.keySet()).get(0);
        }
    }

    public CourseNotificationEvent getNotificationReason() {
        return notificationReason;
    }

    public void setNotificationReason(CourseNotificationEvent notificationReason) {
        this.notificationReason = notificationReason;
    }

    public boolean isShouldBeNotified() {
        return shouldBeNotified;
    }

    public boolean shouldBeNotified() {
        return isShouldBeNotified();
    }

    public void setShouldBeNotified(boolean shouldBeNotified) {
        this.shouldBeNotified = shouldBeNotified;
    }

    public Map<CourseNotificationEvent, Boolean> getNotifiablePerSource() {
        return notifiablePerSource;
    }

    public void setNotifiablePerSource(Map<CourseNotificationEvent, Boolean> notifiablePerSource) {
        this.notifiablePerSource = notifiablePerSource;
    }
}
