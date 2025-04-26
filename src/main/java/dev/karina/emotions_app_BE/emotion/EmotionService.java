package dev.karina.emotions_app_BE.emotion;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmotionService {

    private final EmotionRepository emotionRepository;

    public EmotionService(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    public Emotion saveEmotion(Emotion emotion) {
        if (emotion.getCreatedAt() == null) {
            emotion.setCreatedAt(LocalDateTime.now());
        }
        return emotionRepository.save(emotion);
    }

    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAll();
    }

    public Optional<Emotion> getEmotionById(Long id) {
        return emotionRepository.findById(id);
    }

    public Emotion updateEmotion(Long id, Emotion updatedEmotion) {
        return emotionRepository.findById(id).map(emotion -> {
            emotion.setEmotion(updatedEmotion.getEmotion());
            emotion.setText(updatedEmotion.getText());
            return emotionRepository.save(emotion);
        }).orElseThrow(() -> new RuntimeException("Emotion not found with id " + id));
    }

    public void deleteEmotion(Long id) {
        emotionRepository.deleteById(id);
    }
}