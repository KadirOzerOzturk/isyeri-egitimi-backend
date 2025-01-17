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
@Table(name = "student_group")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID groupId;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
