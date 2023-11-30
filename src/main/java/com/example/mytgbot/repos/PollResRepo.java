package com.example.mytgbot.repos;

import com.example.mytgbot.models.PollResults;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollResRepo extends JpaRepository<PollResults, Long> {
}
