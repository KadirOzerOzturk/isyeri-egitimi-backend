package com.isyeriegitimi.backend.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ogrenci_grup")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int grupId;

    @ManyToOne
    @JoinColumn(name = "izleyici_id")
    private Lecturer izleyici;



}
