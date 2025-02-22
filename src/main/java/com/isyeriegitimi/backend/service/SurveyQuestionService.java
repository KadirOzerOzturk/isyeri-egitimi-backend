package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Survey;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import com.isyeriegitimi.backend.repository.SurveyQuestionRepository;
import com.isyeriegitimi.backend.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class SurveyQuestionService {
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UUID createQuestion(SurveyQuestion question) {
        try {
            UUID questionId = UUID.randomUUID();
            surveyQuestionRepository.insertQuestion(
                    question.getSurvey().getId(),
                    objectMapper.writeValueAsString(question.getOptions()),
                    question.getQuestionNumber(),
                    question.getQuestionText(),
                    question.getQuestionType(),
                    questionId
            );
            return questionId;
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Error processing JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while creating the question: " + e.getMessage());
        }
    }
    public List<SurveyQuestion> getAllQuestions() {
        try {
            return surveyQuestionRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while retrieving questions: " + e.getMessage());
        }
    }

    public List<SurveyQuestion> getQuestionsBySurveyId(UUID id) {
        try {
            List<SurveyQuestion> questions = surveyQuestionRepository.findBySurvey_Id(id);
            return questions;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the questions: " + e.getMessage());
        }

    }
    public SurveyQuestion getQuestionById(UUID id) {
        return surveyQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SurveyQuestion", "survey id", id.toString()));
    }
   

    public SurveyQuestion updateQuestion(UUID id, SurveyQuestion updatedQuestion) {
        try {
            SurveyQuestion existingQuestion = getQuestionById(id);
            existingQuestion.setQuestionNumber(updatedQuestion.getQuestionNumber());
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setOptions(updatedQuestion.getOptions());
            return surveyQuestionRepository.save(existingQuestion);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the question: " + e.getMessage());
        }
    }

    public void deleteQuestion(UUID id) {
        try {
            if (!surveyQuestionRepository.existsById(id)) {
                throw new ResourceNotFoundException("SurveyQuestion", "id", id.toString());
            }
            surveyQuestionRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the question: " + e.getMessage());
        }
    }

    public void createQuestions(List<SurveyQuestion> questions) {

        try {
            Optional<Survey> survey = surveyRepository.findById(questions.get(0).getSurvey().getId());
            if (survey.isEmpty()) {
                throw new ResourceNotFoundException("Survey", "id", questions.get(0).getSurvey().getId().toString());
            }
            for (SurveyQuestion question : questions) {
                UUID questionId = UUID.randomUUID();
                surveyQuestionRepository.insertQuestion(
                        question.getSurvey().getId(),
                        objectMapper.writeValueAsString(question.getOptions()),
                        question.getQuestionNumber(),
                        question.getQuestionText(),
                        question.getQuestionType(),
                        questionId
                );
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Failed to save questions: " + e.getMessage());
        }

    }

    public UUID updateQuestions(UUID surveyId, List<SurveyQuestion> questions) {
        try {
            Optional<Survey> survey = surveyRepository.findById(surveyId);
            if (survey.isEmpty()) {
                throw new ResourceNotFoundException("Survey", "id", surveyId.toString());
            }
            for (SurveyQuestion question : questions) {
                SurveyQuestion existingQuestion = surveyQuestionRepository.findById(question.getQuestionId())
                        .orElseThrow(() -> new ResourceNotFoundException("SurveyQuestion", "id", question.getQuestionId().toString()));


                surveyQuestionRepository.updateQuestion(
                        objectMapper.writeValueAsString(question.getOptions()),
                        question.getQuestionNumber(),
                        question.getQuestionText(),
                        question.getQuestionType(),
                        existingQuestion.getQuestionId()
                );
                return existingQuestion.getQuestionId();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update questions: " + e.getMessage());
        }
        return null;
    }


}
