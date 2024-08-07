package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentGroupRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    private LecturerRepository lecturerRepository;
    private StudentsInGroupRepository studentsInGroupRepository;

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository, StudentsInGroupRepository studentsInGroupRepository) {
        this.lecturerRepository = lecturerRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
    }

    public Optional<Lecturer> getLecturerByLecturerNo(String lecturerNo) {
        return  lecturerRepository.findByIzleyiciNo(lecturerNo);
    }


    public Optional<Lecturer> getLecturerByLecturerId(Long lecturerId) {
        return lecturerRepository.findByIzleyiciId(lecturerId);
    }

    public void update(Long lecturerId, LecturerDto lecturerDto) {
        Optional<Lecturer> lecturer=lecturerRepository.findByIzleyiciId(lecturerId);
        if (lecturer.isEmpty()){
            return ;
        }
        lecturerDto.setIzleyiciId(lecturerId);
        lecturerRepository.save(mapToLecturerEntity(lecturerDto));

    }

    private Lecturer mapToLecturerEntity(LecturerDto  lecturerDto){
        Lecturer lecturer=new Lecturer();

        lecturer.setIzleyiciAd(lecturerDto.getIzleyiciAd());
        lecturer.setIzleyiciNo(lecturerDto.getIzleyiciNo());
        lecturer.setIzleyiciSoyad(lecturerDto.getIzleyiciSoyad());
        lecturer.setIzleyiciEposta(lecturerDto.getIzleyiciEposta());
        lecturer.setIzleyiciId(lecturerDto.getIzleyiciId());
        lecturer.setIzleyiciFakulte(lecturerDto.getIzleyiciFakulte());
        lecturer.setIzleyiciParola(lecturerDto.getIzleyiciParola());
        lecturer.setIzleyiciHakkinda(lecturerDto.getIzleyiciHakkinda());

        return lecturer;
    }
    private LecturerDto mapToDto(Lecturer lecturer){
        LecturerDto lecturerDto=new LecturerDto();
        lecturerDto.setIzleyiciId(lecturer.getIzleyiciId());
        lecturerDto.setIzleyiciHakkinda(lecturer.getIzleyiciHakkinda());
        lecturerDto.setIzleyiciFakulte(lecturer.getIzleyiciFakulte());
        lecturerDto.setIzleyiciNo(lecturer.getIzleyiciNo());
        lecturerDto.setIzleyiciAd(lecturer.getIzleyiciAd());
        lecturerDto.setIzleyiciEposta(lecturer.getIzleyiciEposta());
        lecturerDto.setIzleyiciParola(lecturer.getIzleyiciParola());
        lecturerDto.setIzleyiciSoyad(lecturer.getIzleyiciSoyad());

        return lecturerDto;
    }

    public Optional<Lecturer> getLecturerOfStudentByStudentNo(Long studentNo) {
        try {
            StudentGroup studentGroup = studentsInGroupRepository.findByStudentOgrenciNo(studentNo).get().getStudentGroup();
            Optional<Lecturer> lecturer= Optional.ofNullable(studentGroup.getIzleyici());
            return lecturer;
        }catch (Exception e){
            return null;
        }




    }

    public List<LecturerDto> getAllLecturer() {

            List<Lecturer> lecturerList=lecturerRepository.findAll();
            List<LecturerDto> lecturerDtoList =lecturerList.stream().map((lecturer -> mapToDto(lecturer))).collect(Collectors.toList());
        return lecturerDtoList;
    }
}
