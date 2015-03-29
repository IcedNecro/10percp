package com.checkpoint.mobilenetwork.core;

public class NotificationRequest {
	private NotificationStatus status;
	
	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public enum NotificationStatus {
		OK,
		NOTIFICATION_NOT_DELIVERED
	}
}
