package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<FormQuestion> getAllQuestions() {
        try {
            return formQuestionRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while retrieving questions: " + e.getMessage());
        }
    }

    public List<FormQuestion> getQuestionsByFormId(UUID id) {
        try {
            List<FormQuestion> questions = formQuestionRepository.findByForm_Id(id);
            return questions;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the questions: " + e.getMessage());
        }

    }
    public FormQuestion getQuestionById(UUID id) {
        return formQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FormQuestion", "form id", id.toString()));
    }
    public UUID createQuestion(FormQuestion question) {
        try {
            UUID questionId = UUID.randomUUID();
            formQuestionRepository.insertQuestion(
                    question.getForm().getId(),
                    objectMapper.writeValueAsString(question.getOptions()),
                    question.getQuestionNumber(),
                    question.getQuestionText(),
                    questionId,
                    question.getQuestionType().toString().toUpperCase(),
                    question.getRequiredFor().toUpperCase()
                    );
            return questionId;
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Error processing JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while creating the question: " + e.getMessage());
        }
    }

    public FormQuestion updateQuestion(UUID id, FormQuestion updatedQuestion) {
        try {
            FormQuestion existingQuestion = getQuestionById(id);
            existingQuestion.setQuestionNumber(updatedQuestion.getQuestionNumber());
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setOptions(updatedQuestion.getOptions());
            existingQuestion.setQuestionType(updatedQuestion.getQuestionType());
            existingQuestion.setRequiredFor(updatedQuestion.getRequiredFor());
            return formQuestionRepository.save(existingQuestion);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the question: " + e.getMessage());
        }
    }

    public void deleteQuestion(UUID id) {
        try {
            if (!formQuestionRepository.existsById(id)) {
                throw new ResourceNotFoundException("FormQuestion", "id", id.toString());
            }
            formQuestionRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the question: " + e.getMessage());
        }
    }

    public void bulkSave(List<FormQuestion> questions, UUID formId) {
        try {
            for (FormQuestion question : questions){
                UUID questionId = UUID.randomUUID();
                formQuestionRepository.insertQuestion(
                        question.getForm().getId(),
                        objectMapper.writeValueAsString(question.getOptions()),
                        question.getQuestionNumber(),
                        question.getQuestionText(),
                        questionId,
                        question.getQuestionType().toString().toUpperCase(),
                        question.getRequiredFor().toUpperCase()
                );
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
