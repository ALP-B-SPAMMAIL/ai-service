package com.example.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ai.model.Ai;

@Repository
public interface AiRepository extends JpaRepository<Ai, Integer> {
}