package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.SurveyAnswerDto;
import com.isyeriegitimi.backend.dto.SurveyAnswerRequest;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Survey;
import com.isyeriegitimi.backend.model.SurveyAnswer;
import com.isyeriegitimi.backend.repository.SurveyAnswerRepository;
import com.isyeriegitimi.backend.repository.SurveyQuestionRepository;
import com.isyeriegitimi.backend.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SurveyAnswerService {

    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;


    public void saveAnswers(List<SurveyAnswerRequest> answerDtoList){
        try {
            Optional<Survey>  survey= surveyRepository.findById(answerDtoList.get(0).getSurveyId());
            if(survey.isPresent()){
                for (SurveyAnswerRequest surveyAnswerDto: answerDtoList) {
                    SurveyAnswerDto surveyAnswer = new SurveyAnswerDto();
                    surveyAnswer.setSurvey(survey.get());
                    surveyAnswer.setSurveyQuestion(surveyQuestionRepository.findById(surveyAnswerDto.getQuestionId()).get());
                    surveyAnswer.setUserId(surveyAnswerDto.getUserId());
                    surveyAnswer.setUserRole(surveyAnswerDto.getUserRole());
                    surveyAnswer.setAnswer(surveyAnswerDto.getAnswer());

                    surveyAnswerRepository.save(mapToEntity(surveyAnswer));
                }
            }else {
                throw new ResourceNotFoundException("Survey","Survey Id",answerDtoList.get(0).getSurveyId().toString());
            }
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while saving the answers: " + e.getMessage());
        }
    }
    public List<SurveyAnswerDto> getAnswersBySurveyAndUser(UUID surveyId, UUID userId) {
        try {
            boolean isExist= surveyRepository.existsById(surveyId);
            if(isExist){
                List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findBySurveyIdAndUserId(surveyId, userId);
                return surveyAnswers.stream().map(this::mapToDto).toList();
            }else {
                throw new ResourceNotFoundException("Survey","Survey Id",surveyId.toString());
            }
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the answers: " + e.getMessage());
        }

    }
    public void updateAnswers(List<SurveyAnswerRequest> answerDtoList) {
        try {
            Optional<Survey>  survey= surveyRepository.findById(answerDtoList.get(0).getSurveyId());
            if(survey.isPresent()){
                for (SurveyAnswerRequest surveyAnswerDto: answerDtoList) {
                    Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findBySurveyQuestion_QuestionId(surveyAnswerDto.getQuestionId());
                    if(surveyAnswer.isPresent()){
                        surveyAnswer.get().setAnswer(surveyAnswerDto.getAnswer());
                        surveyAnswerRepository.save(surveyAnswer.get());
                    }
                }




            }else {
                throw new ResourceNotFoundException("Survey","Survey Id",answerDtoList.get(0).getSurveyId().toString());
            }
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while updating the answers: " + e.getMessage());
        }
    }
    private SurveyAnswer mapToEntity(SurveyAnswerDto surveyAnswerDto){
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setAnswer(surveyAnswerDto.getAnswer());
        surveyAnswer.setSurvey(surveyAnswerDto.getSurvey());
        surveyAnswer.setSurveyQuestion(surveyAnswerDto.getSurveyQuestion());
        surveyAnswer.setUserRole(surveyAnswerDto.getUserRole());
        surveyAnswer.setUserId(surveyAnswerDto.getUserId());
        return surveyAnswer;
    }
    private SurveyAnswerDto mapToDto(SurveyAnswer surveyAnswer){
        SurveyAnswerDto surveyAnswerDto = new SurveyAnswerDto();
        surveyAnswerDto.setAnswer(surveyAnswer.getAnswer());
        surveyAnswerDto.setSurvey(surveyAnswer.getSurvey());
        surveyAnswerDto.setSurveyQuestion(surveyAnswer.getSurveyQuestion());
        surveyAnswerDto.setUserRole(surveyAnswer.getUserRole());
        surveyAnswerDto.setUserId(surveyAnswer.getUserId());
        return surveyAnswerDto;
    }



}
