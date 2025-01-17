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
@Table(name = "ogrenci_gorev")
public class StudentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student student;

    private String taskDescription;
    private String taskStatus;

}
