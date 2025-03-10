package com.isyeriegitimi.backend.model;

import com.isyeriegitimi.backend.converter.RoleListJsonConverter;
import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "form")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;

    @Convert(converter = RoleListJsonConverter.class)
    @Column(name = "roles")
    private List<Role> roles;
}
