package com.example.dictionaryapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class DictionaryService {

    private Map<String, String> dictionary = new HashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/dictionary.json");
            TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};
            dictionary = mapper.readValue(inputStream, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dictionary data", e);
        }
    }

    public Optional<String> getMeaning(String word) {
        return Optional.ofNullable(dictionary.get(word));
    }

    public String getRandomWord() {
        List<String> keys = new ArrayList<>(dictionary.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    public Map.Entry<String, String> getRandomWordWithMeaning() {
        List<Map.Entry<String, String>> entries = new ArrayList<>(dictionary.entrySet());
        return entries.get(random.nextInt(entries.size()));
    }
}
