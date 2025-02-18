package com.example.dictionaryapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/meaning/{word}")
    public ResponseEntity<?> getMeaning(@PathVariable(value = "word", required = false) String word) {
        // Validate the word parameter
        if (word == null || word.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Please provide a word to search for its meaning."));
        }

        // Check if the input is a number
        if (word.matches("\\d+")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid input. Please enter a valid word."));
        }

        String lowerCaseWord = word.toLowerCase();
        return dictionaryService.getMeaning(lowerCaseWord)
                .map(meaning -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("word", lowerCaseWord);
                    response.put("meaning", meaning);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Collections.singletonMap("error", "Word not found"))
                );
    }

    @GetMapping("/randomWord")
    public Map<String, String> getRandomWord() {
        String randomWord = dictionaryService.getRandomWord();
        return Collections.singletonMap("word", randomWord);
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }


    @GetMapping("/randomWordWithMeaning")
    public Map<String, String> getRandomWordWithMeaning() {
        Map.Entry<String, String> entry = dictionaryService.getRandomWordWithMeaning();
        Map<String, String> response = new HashMap<>();
        response.put("word", entry.getKey());
        response.put("meaning", entry.getValue());
        return response;
    }
}
