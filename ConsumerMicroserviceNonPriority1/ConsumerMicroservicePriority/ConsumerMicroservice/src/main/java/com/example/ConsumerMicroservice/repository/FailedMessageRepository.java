package com.example.ConsumerMicroservice.repository;

import com.example.ConsumerMicroservice.model.FailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedMessageRepository extends JpaRepository<FailedMessage, Long>{}