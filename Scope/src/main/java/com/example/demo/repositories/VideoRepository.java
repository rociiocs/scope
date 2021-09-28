package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Video;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}
