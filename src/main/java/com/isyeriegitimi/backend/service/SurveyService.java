package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.SurveyDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Survey;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import com.isyeriegitimi.backend.repository.SurveyQuestionRepository;
import com.isyeriegitimi.backend.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository  surveyQuestionRepository;
    public List<Survey> getAllSurveys() {
        try {
            List<Survey> surveys = surveyRepository.findAll();
            return surveys;
        } catch (Exception e) {
            throw new InternalServerErrorException("Surveys could not be fetched. Reason: " + e.getMessage());
        }
    }

    public Survey createSurvey(Survey survey) {
        try {
            return surveyRepository.save(survey);
        } catch (Exception e) {
            throw new InternalServerErrorException("Survey could not be created. Reason: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteSurvey(UUID id) {
        try {
            surveyQuestionRepository.deleteAllBySurveyId(id);
            surveyRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Survey could not be deleted. Reason: " + e.getMessage());
        }
    }

    public Survey getSurveyById(UUID id) {
        try {
            return surveyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Survey", "id", id.toString()));
        } catch (Exception e) {
            throw new InternalServerErrorException("Survey could not be fetched. Reason: " + e.getMessage());
        }
    }

    public Survey updateSurvey(SurveyDto surveyDto, UUID id) {
        try {
            Survey survey = surveyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Survey", "id", surveyDto.getId().toString()));
            survey.setDescription(surveyDto.getDescription());
            survey.setTitle(surveyDto.getTitle());
            return surveyRepository.save(survey);
        } catch (Exception e) {
            throw new InternalServerErrorException("Survey could not be updated. Reason: " + e.getMessage());
        }
    }

    public List<SurveyDto> getSurveysByRole(String userRole) {
        try {
            List<Survey> surveyList= surveyRepository.findAllByRequiredFor(userRole);
            if(surveyList.isEmpty()){
                return Collections.emptyList();
            }
            return surveyList.stream().map(this::mapToDto).toList();
        }catch (Exception e){
            throw new InternalServerErrorException("Surveys could not be fetched. Reason: " + e.getMessage());
        }
    }


    private SurveyDto mapToDto(Survey survey) {
        try {
            SurveyDto surveyDto = new SurveyDto();
            surveyDto.setDescription(survey.getDescription());
            surveyDto.setTitle(survey.getTitle());
            surveyDto.setId(survey.getId());
            surveyDto.setRequiredFor(survey.getRequiredFor());
            return surveyDto;
        }catch (Exception e){
            throw new InternalServerErrorException("Survey could not be mapped. Reason: " + e.getMessage());
        }
    }
}
