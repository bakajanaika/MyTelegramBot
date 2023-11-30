package com.example.mytgbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    private double temp_c;
    private double feelslike_c;
    private Condition condition;


}

