package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private StudentsInGroupRepository studentsInGroupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentsInGroupRepository studentsInGroupRepository) {
        this.studentRepository = studentRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
    }

    public Optional<Student> getStudentByStudentNo(Long studentNo){
        return studentRepository.findByOgrenciNo(studentNo);
    }
    public String save(StudentDto studentDto ,Long studentNo) {

            Optional<Student> existingStudent = studentRepository.findByOgrenciNo(studentNo);

            if (existingStudent.isPresent()) {
                Student student = new Student();
                student.setOgrenciNo(studentDto.getOgrenciNo());
                student.setOgrenciAd(studentDto.getOgrenciAd());
                student.setOgrenciSoyad(studentDto.getOgrenciSoyad());
                student.setOgrenciEposta(studentDto.getOgrenciEposta());
                student.setOgrenciTelNo(studentDto.getOgrenciTelNo());
                student.setOgrenciKimlikNo(studentDto.getOgrenciKimlikNo());
                student.setOgrenciAdres(studentDto.getOgrenciAdres());
                student.setOgrenciAgno(studentDto.getOgrenciAgno());
                student.setOgrenciParola(studentDto.getOgrenciParola());
                student.setOgrenciSinif(studentDto.getOgrenciSinif());
                student.setOgrenciFotograf(studentDto.getOgrenciFotograf());
                student.setOgrenciFakulte(studentDto.getOgrenciFakulte());
                student.setOgrenciHakkinda(studentDto.getOgrenciHakkinda());
                student.setFirma(studentDto.getFirma());
                studentRepository.save(student);
        }
        return "success";
    }




    public Student mapDtoToEntity(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }

        Student student = new Student();
        student.setOgrenciNo(studentDto.getOgrenciNo());
        student.setOgrenciAd(studentDto.getOgrenciAd());
        student.setOgrenciSoyad(studentDto.getOgrenciSoyad());
        student.setOgrenciEposta(studentDto.getOgrenciEposta());
        student.setOgrenciTelNo(studentDto.getOgrenciTelNo());
        student.setOgrenciKimlikNo(studentDto.getOgrenciKimlikNo());
        student.setOgrenciAdres(studentDto.getOgrenciAdres());
        student.setOgrenciAgno(studentDto.getOgrenciAgno());
        student.setOgrenciParola(studentDto.getOgrenciParola());
        student.setOgrenciSinif(studentDto.getOgrenciSinif());
        student.setOgrenciFotograf(studentDto.getOgrenciFotograf());
        student.setOgrenciFakulte(studentDto.getOgrenciFakulte());
        student.setOgrenciHakkinda(studentDto.getOgrenciHakkinda());
        student.setFirma(studentDto.getFirma());

        return student;
    }

    public StudentDto mapToDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setOgrenciNo(student.getOgrenciNo());
        studentDto.setOgrenciAd(student.getOgrenciAd());
        studentDto.setOgrenciSoyad(student.getOgrenciSoyad());
        studentDto.setOgrenciEposta(student.getOgrenciEposta());
        studentDto.setOgrenciTelNo(student.getOgrenciTelNo());
        studentDto.setOgrenciKimlikNo(student.getOgrenciKimlikNo());
        studentDto.setOgrenciAdres(student.getOgrenciAdres());
        studentDto.setOgrenciAgno(student.getOgrenciAgno());
        studentDto.setOgrenciParola(student.getOgrenciParola());
        studentDto.setOgrenciSinif(student.getOgrenciSinif());
        studentDto.setOgrenciFotograf(student.getOgrenciFotograf());
        studentDto.setOgrenciFakulte(student.getOgrenciFakulte());
        studentDto.setOgrenciHakkinda(student.getOgrenciHakkinda());
        studentDto.setFirma(student.getFirma());

        return studentDto;
    }

    public List<StudentDto> getAllStudents() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = studentList.stream()
                .map(student -> mapToDto(student))
                .collect(Collectors.toList());
        return studentDtoList;
    }

    public List<StudentDto> getAllStudentsWithoutGroup() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentInGroup> studentInGroupList = studentsInGroupRepository.findAll();

        // Collect the student numbers of students who are in a group into a set
        Set<Long> studentNumbersInGroup = studentInGroupList.stream()
                .map(studentInGroup -> studentInGroup.getStudent().getOgrenciNo())
                .collect(Collectors.toSet());

        // Filter out students whose numbers are in the set
        List<Student> studentsWithoutGroup = studentList.stream()
                .filter(student -> !studentNumbersInGroup.contains(student.getOgrenciNo()))
                .collect(Collectors.toList());
        List<StudentDto> studentDtoList =studentsWithoutGroup.stream().map(student -> mapToDto(student)).collect(Collectors.toList());

        return studentDtoList;
    }
}
