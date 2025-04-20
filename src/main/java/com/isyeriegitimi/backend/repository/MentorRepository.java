package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {
    Optional<Mentor> findByEmail(String email);

    List<Mentor> findByCompanyCompanyId(UUID companyId  );

    List<Mentor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String searchText, String searchText1);
}
