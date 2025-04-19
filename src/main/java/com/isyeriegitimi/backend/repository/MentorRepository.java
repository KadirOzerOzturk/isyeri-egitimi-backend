package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {
}
