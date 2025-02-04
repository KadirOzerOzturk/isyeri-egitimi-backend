package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.FormDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Form;
import com.isyeriegitimi.backend.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    public List<Form> getAllForms() {
        try {
            List<Form> forms = formRepository.findAll();
            return forms;
        } catch (Exception e) {
            throw new InternalServerErrorException("Forms could not be fetched. Reason: " + e.getMessage());
        }
    }

    public Form createForm(Form form) {
        try {
            return formRepository.save(form);
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be created. Reason: " + e.getMessage());
        }
    }

    public void deleteForm(UUID id) {
        try {
            formRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be deleted. Reason: " + e.getMessage());
        }
    }

    public Form getFormById(UUID id) {
        try {
            return formRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Form", "id", id.toString()));
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be fetched. Reason: " + e.getMessage());
        }
    }

    public Form updateForm(FormDto formDto, UUID id) {
        try {
            Form form = formRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Form", "id", formDto.getId().toString()));
            form.setDescription(formDto.getDescription());
            form.setTitle(formDto.getTitle());
            return formRepository.save(form);
        } catch (Exception e) {
            throw new InternalServerErrorException("Form could not be updated. Reason: " + e.getMessage());
        }
    }
}