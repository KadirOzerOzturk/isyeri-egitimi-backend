package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.model.FormAnswer;
import com.isyeriegitimi.backend.repository.FormAnswerRepository;
import com.isyeriegitimi.backend.repository.FormQuestionRepository;
import com.isyeriegitimi.backend.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FormAnswerService {

    @Autowired
    private FormAnswerRepository formAnswerRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormQuestionRepository formQuestionRepository;

    public List<FormAnswer> getAllAnswers() {
        try {
            return formAnswerRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while retrieving answers: " + e.getMessage());
        }
    }

    public List<FormAnswer> getAnswerByFormId(UUID id) {
        try {
            List<FormAnswer> answers = formAnswerRepository.findByForm_Id(id);
            return answers;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the answer: " + e.getMessage());
        }
    }
    public FormAnswer getAnswerById(UUID id) {
        return formAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id));
    }
    public FormAnswer submitAnswer(FormAnswer answer) {
        try {
            return formAnswerRepository.save(answer);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while submitting the answer: " + e.getMessage());
        }
    }
    public void saveAnswers(List<FormAnswer> formAnswerRequests) {
        try {
            List<FormAnswer> answers = formAnswerRequests.stream().map(request -> {
                FormAnswer existingAnswer = formAnswerRepository
                        .findByForm_IdAndFormQuestionQuestionIdAndUserId(
                                request.getForm().getId(),
                                request.getFormQuestion().getQuestionId(),
                                request.getUserId()
                        )
                        .orElse(null);

                if (existingAnswer != null) {
                    // Eğer cevap zaten varsa, sadece güncelle
                    existingAnswer.setAnswer(request.getAnswer());
                    return existingAnswer;
                } else {
                    // Yeni cevap ekle
                    FormAnswer answer = new FormAnswer();
                    answer.setForm(formRepository.findById(request.getForm().getId())
                            .orElseThrow(() -> new RuntimeException("Form bulunamadı!")));
                    answer.setFormQuestion(formQuestionRepository.findById(request.getFormQuestion().getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Form sorusu bulunamadı!")));
                    answer.setUserId(request.getUserId());
                    answer.setUserRole(request.getUserRole());
                    answer.setAnswer(request.getAnswer());
                    answer.setStudentId(request.getStudentId());
                    return answer;
                }
            }).collect(Collectors.toList());

            formAnswerRepository.saveAll(answers);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while saving the answers: " + e.getMessage());
        }
    }

    public FormAnswer updateAnswer(UUID id, FormAnswer updatedAnswer) {
        try {
            FormAnswer answer = getAnswerById(id);
            answer.setAnswer(updatedAnswer.getAnswer());
            return formAnswerRepository.save(answer);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the answer: " + e.getMessage());
        }
    }

    public void deleteAnswer(UUID id) {
        try {
            if (!formAnswerRepository.existsById(id)) {
                throw new ResourceNotFoundException("Answer not found with id: " + id);
            }
            formAnswerRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the answer: " + e.getMessage());
        }
    }

    public List<FormAnswer> getAnswersByFormIdAndStudentId(UUID formId, UUID studentId) {
        try {
            return formAnswerRepository.findByForm_IdAndStudentId(formId, studentId);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the answers: " + e.getMessage());
        }
    }
}
