package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Form;
import com.isyeriegitimi.backend.model.FormSignature;
import com.isyeriegitimi.backend.security.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormSignatureRepository extends JpaRepository<FormSignature, UUID> {

    Optional<FormSignature> findByFormIdAndSignedByRole(UUID formId, String signedByRole);

    List<FormSignature> findAllByFormIdAndSignedByAndSignedByRole(UUID formId, UUID signedBy, String signedByRole);

    List<FormSignature> findAllByFormId(UUID formId);

    boolean existsByFormAndSignedByAndSignedByRole(Form form, UUID signedBy, String signedByRole);

    List<FormSignature> findBySignedByAndSignedByRole(UUID signedBy, String signedByRole);

    List<FormSignature> findAllByFormIdAndSignedByAndSignedByRole(UUID formId, UUID signedBy, Role signedByRole);

    void deleteAllByFormId(UUID id);
}
