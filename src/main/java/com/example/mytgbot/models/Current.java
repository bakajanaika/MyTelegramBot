package com.example.mytgbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    private double temp_c;
    private double feelslike_c;
    private Condition condition;


    public void setTemp_c(double temp_c) {
        this.temp_c = temp_c;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}

