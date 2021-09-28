package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
