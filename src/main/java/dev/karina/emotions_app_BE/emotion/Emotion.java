package dev.karina.emotions_app_BE.emotion;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotions")
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emotion;
    private String text;
    private LocalDateTime createdAt;

    public Emotion() {
    }

    public Emotion(String emotion, String text, LocalDateTime createdAt) {
        this.emotion = emotion;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
