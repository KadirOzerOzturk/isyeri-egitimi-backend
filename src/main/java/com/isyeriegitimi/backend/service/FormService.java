package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isyeriegitimi.backend.dto.FormDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Form;
import com.isyeriegitimi.backend.model.FormAnswer;
import com.isyeriegitimi.backend.model.FormSignature;
import com.isyeriegitimi.backend.repository.FormAnswerRepository;
import com.isyeriegitimi.backend.repository.FormQuestionRepository;
import com.isyeriegitimi.backend.repository.FormRepository;
import com.isyeriegitimi.backend.repository.FormSignatureRepository;
import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormSignatureRepository formSignatureRepository;
    @Autowired
    private FormQuestionRepository formQuestionRepository;
    @Autowired
    private FormAnswerRepository formAnswerRepository;

    public List<Form> getAllForms() {
        return formRepository.findAll();
    }

    public List<FormSignature> getSignedForms() {
        return formSignatureRepository.findAll();
    }

    public Map<String, Object> getSignedAndUnsignedForms(UUID userId, String userRole) {
        try {
            List<FormSignature> signedForms = formSignatureRepository.findBySignedByAndSignedByRole(userId, userRole);
            signedForms.sort(Comparator.comparing(formSignature -> formSignature.getForm().getTitle()));
            List<Form> allForms = formRepository.findAll();

            List<Form> unsignedForms = allForms.stream()
                    .filter(form -> form.getRoles().contains(Role.valueOf(userRole)))
                    .filter(form -> signedForms.stream().noneMatch(sig -> sig.getForm().equals(form)))
                    .sorted(Comparator.comparing(Form::getTitle))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("signedForms", signedForms);
            response.put("unsignedForms", unsignedForms);
            return response;

        } catch (Exception e) {
            throw new InternalServerErrorException("Error: " + e.getMessage());
        }
    }

    @Transactional
    public Form createForm(Form form, List<Role> roles) {
        try {
            Form savedForm = new Form();

            savedForm.setRoles(roles);
            savedForm.setTitle(form.getTitle());
            savedForm.setDescription(form.getDescription());
            formRepository.save(savedForm);

            return savedForm;
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be created. Reason: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteForm(UUID id) {
        try {
            formAnswerRepository.deleteAllByFormId(id);
            formQuestionRepository.deleteAllByFormId(id);
            formSignatureRepository.deleteAllByFormId(id);
            formRepository.deleteById(id);

        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be deleted. Reason: " + e.getMessage());
        }
    }

    public Form getFormById(UUID id) {
        try {
            return formRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Form", "id", id.toString()));
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be fetched. Reason: " + e.getMessage());
        }
    }

    public Form updateForm(FormDto formDto, UUID id) {
        try {
            Form form = formRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Form", "id", formDto.getId().toString()));
            form.setDescription(formDto.getDescription());
            form.setTitle(formDto.getTitle());
            form.setRoles(formDto.getRoles());
            return formRepository.save(form);
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be updated. Reason: " + e.getMessage());
        }
    }


    public Map<String, Object> getSignedFormsByStudentId(UUID userId, String userRole, UUID studentId) {
        try {
            List<FormSignature> signedForms = formSignatureRepository.findBySignedByAndSignedByRoleAndStudentId(userId, userRole,studentId);
            List<Form> allForms = formRepository.findAll();

            List<Form> unsignedForms = allForms.stream()
                    .filter(form -> form.getRoles().contains(Role.valueOf(userRole)))
                    .filter(form -> signedForms.stream().noneMatch(sig -> sig.getForm().equals(form)))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("signedForms", signedForms);
            response.put("unsignedForms", unsignedForms);
            return response;

        } catch (Exception e) {
            throw new InternalServerErrorException("Error: " + e.getMessage());
        }
    }
}
