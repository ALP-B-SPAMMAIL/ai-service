package com.kt.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.ai.model.Ai;

@Repository
public interface AiRepository extends JpaRepository<Ai, Long> {
    
    
}