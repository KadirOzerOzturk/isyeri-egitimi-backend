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
@Table(name = "ogrenci_gorev")
public class StudentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gorevId;

    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student student;

    private String gorevMetni;
    private String gorevDurumu;

}
