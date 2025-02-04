package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.model.FormQuestion;
import com.isyeriegitimi.backend.repository.FormQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormQuestionService {

    @Autowired
    private FormQuestionRepository formQuestionRepository;

    public List<FormQuestion> getAllQuestions() {
        return formQuestionRepository.findAll();
    }

    public FormQuestion getQuestionById(UUID id) {
        Optional<FormQuestion> question=  formQuestionRepository.findById(id);
        return question.orElse(null);
    }


    public UUID  createQuestion(FormQuestion question) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        formQuestionRepository.insertQuestion(
                question.getForm().getId(),
                objectMapper.writeValueAsString(question.getOptions()), // JSON string olarak kaydet
                question.getQuestionNumber(),
                question.getQuestionText(),
                UUID.randomUUID()
        );
        return question.getQuestionId();
    }
//public UUID  createQuestion(FormQuestion question) {
//    return formQuestionRepository.save(question).getQuestionId();
//}
    public FormQuestion updateQuestion(UUID id, FormQuestion updatedQuestion) {
        FormQuestion question = getQuestionById(id);
        question.setQuestionNumber(updatedQuestion.getQuestionNumber());
        question.setQuestionText(updatedQuestion.getQuestionText());
        question.setOptions(updatedQuestion.getOptions());
        return formQuestionRepository.save(question);
    }

    public void deleteQuestion(UUID id) {
        formQuestionRepository.deleteById(id);
    }
}
