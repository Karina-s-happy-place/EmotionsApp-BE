package dev.karina.emotions_app_BE.emotion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emotions")
@CrossOrigin(origins = "*")
public class EmotionController {

    private final EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @PostMapping
    public ResponseEntity<?> createEmotion(@RequestBody Emotion emotion) {
        Emotion savedEmotion = emotionService.saveEmotion(emotion);
        if (savedEmotion == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo registrar la emoción.");
        }
        return ResponseEntity.ok(savedEmotion);
    }

    @GetMapping
    public ResponseEntity<List<Emotion>> getAllEmotions() {
        List<Emotion> emotions = emotionService.getAllEmotions();
        return ResponseEntity.ok(emotions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emotion> getEmotionById(@PathVariable Long id) {
        return emotionService.getEmotionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emotion> updateEmotion(@PathVariable Long id, @RequestBody Emotion updatedEmotion) {
        Emotion emotion = emotionService.updateEmotion(id, updatedEmotion);
        return ResponseEntity.ok(emotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmotion(@PathVariable Long id) {
        emotionService.deleteEmotion(id);
        return ResponseEntity.noContent().build();
    }
}
