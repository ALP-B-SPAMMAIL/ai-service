package com.example.ai.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ai")
public class Ai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String actionBy;
    private LocalDateTime whenActioned;

    @Lob
    private String aiInput;

    @Lob
    private String aiOutput;

    public void complete(String output) {
        this.aiOutput = output;
        this.whenActioned = LocalDateTime.now();
    }
    
}
