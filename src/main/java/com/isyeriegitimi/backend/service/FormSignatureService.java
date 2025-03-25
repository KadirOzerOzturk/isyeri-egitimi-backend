package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.*;
import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormSignatureService {
    private final FormSignatureRepository formSignatureRepository;
    private final FormRepository formRepository;
    private final CommissionRepository commissionRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final CompanyRepository companyRepository;

    public FormSignatureService(FormSignatureRepository formSignatureRepository, FormRepository formRepository,
                                CommissionRepository commissionRepository, StudentRepository studentRepository,
                                LecturerRepository lecturerRepository, CompanyRepository companyRepository) {
        this.formSignatureRepository = formSignatureRepository;
        this.formRepository = formRepository;
        this.commissionRepository = commissionRepository;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public String signForm(UUID formId, UUID userId, String userRole,UUID studentID) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form", "id", formId.toString()));

        UUID actualUserId = null;
        if (userRole.equals(Role.COMMISSION.toString())) {
            Commission commission = commissionRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Commission", "id", userId.toString()));
            actualUserId = commission.getCommissionId();
        } else if (userRole.equals(Role.STUDENT.toString())) {
            Student student = studentRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", userId.toString()));
            actualUserId = student.getStudentId();
        } else if (userRole.equals(Role.LECTURER.toString())) {
            Lecturer lecturer = lecturerRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", userId.toString()));
            actualUserId = lecturer.getLecturerId();
        } else if (userRole.equals(Role.COMPANY.toString())) {
            Company company = companyRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", userId.toString()));
            actualUserId = company.getCompanyId();
        }

        Role role = Role.valueOf(userRole);

        if (!form.getRoles().contains(role)) {
            throw new IllegalArgumentException("This user role is not allowed to sign this form.");
        }

        Optional<FormSignature> existingSignature = formSignatureRepository.findByFormAndSignedByAndSignedByRole(form, actualUserId, role.toString());
        if (existingSignature.isPresent()) {
            FormSignature signature = FormSignature.builder()
                    .id(existingSignature.get().getId())
                    .form(form)
                    .signedBy(actualUserId)
                    .signedByRole(role.toString())
                    .signedAt(new Date())
                    .studentId(studentID)
                    .build();
            formSignatureRepository.save(signature);
            return "Form already signed.";

        }

        FormSignature signature = FormSignature.builder()
                .form(form)
                .signedBy(actualUserId)
                .signedByRole(role.toString())
                .signedAt(new Date())
                .studentId(studentID)
                .build();

        formSignatureRepository.save(signature);
        return "Form signed successfully.";
    }
}
