package com.example.mytgbot.repos;

import com.example.mytgbot.models.FromMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FromMessageRepo extends JpaRepository<FromMessage, Long> {
}
