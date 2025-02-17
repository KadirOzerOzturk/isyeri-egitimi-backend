package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.SurveyAnswerDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.SurveyAnswer;
import com.isyeriegitimi.backend.repository.SurveyAnswerRepository;
import com.isyeriegitimi.backend.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SurveyAnswerService {

    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;
    @Autowired
    private SurveyRepository surveyRepository;


    public void saveAnswers(List<SurveyAnswerDto> answerDtoList){
        try {
            boolean isExist= surveyRepository.existsById(answerDtoList.get(0).getSurvey().getId());
            if(isExist){
                for (SurveyAnswerDto surveyAnswerDto: answerDtoList) {
                    surveyAnswerRepository.save(mapToEntity(surveyAnswerDto));
                }
            }else {
                throw new ResourceNotFoundException("Survey","Survey Id",answerDtoList.get(0).getSurvey().getId().toString());
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
