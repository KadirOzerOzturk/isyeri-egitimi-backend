package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.FormAnswer;
import com.isyeriegitimi.backend.repository.FormAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FormAnswerService {

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    public List<FormAnswer> getAllAnswers() {
        return formAnswerRepository.findAll();
    }

    public FormAnswer getAnswerById(UUID id) {
        return formAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id));
    }

    public FormAnswer submitAnswer(FormAnswer answer) {
        return formAnswerRepository.save(answer);
    }

    public FormAnswer updateAnswer(UUID id, FormAnswer updatedAnswer) {
        FormAnswer answer = getAnswerById(id);
        answer.setAnswer(updatedAnswer.getAnswer());
        return formAnswerRepository.save(answer);
    }

    public void deleteAnswer(UUID id) {
        formAnswerRepository.deleteById(id);
    }
}
