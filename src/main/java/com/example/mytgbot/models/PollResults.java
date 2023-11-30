package com.example.mytgbot.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PollResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String pollId;
    String username;
    String selectedOption;
}
