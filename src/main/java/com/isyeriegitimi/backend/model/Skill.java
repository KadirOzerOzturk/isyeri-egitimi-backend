package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID skillId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String description;
    private String level;
}
