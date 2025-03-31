package com.example.Stars.queries.read_model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

public enum NotificationStatus {
    LIKE,
    FOLLOW
}
