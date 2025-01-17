package com.isyeriegitimi.backend.security.service;

import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.CommissionRepository;
import com.isyeriegitimi.backend.repository.CompanyRepository;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.dto.UserResponse;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.model.User;
import com.isyeriegitimi.backend.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final CommissionRepository commissionRepository;
    private final LecturerRepository lecturerRepository;




   public ApiResponse save(UserRequest userRequest) {
       Optional<User> existingUser = userRepository.findByUsernameAndTitle(userRequest.getUsername(), userRequest.getTitle());
       if (existingUser.isPresent()) {
           return ApiResponse.error("Kullanıcı zaten kayıtlı", 400);
       }
       User user = User.builder()
               .username(userRequest.getUsername())
               .title(userRequest.getTitle())
               .password(passwordEncoder.encode(userRequest.getPassword()))
               .role(Role.USER)
               .build();

       userRepository.save(user);

       return ApiResponse.success(user, "Kayit başarılı");
   }

    public ApiResponse auth(UserRequest userRequest) {
        // Kullanıcıyı title ve username ile doğrula
        User user = userRepository.findByUsernameAndTitle(userRequest.getUsername(), userRequest.getTitle())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        // Şifre doğrulama işlemini gerçekleştir
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );

        // JWT token oluştur
        String token = jwtService.generateToken(user);

        // UserDto oluştur
        Object userDto = null;
        if (user.getTitle().equals(String.valueOf(Role.LECTURER))) {
            Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(UUID.fromString(user.getUsername()));
            userDto = lecturer.orElseThrow(() -> new RuntimeException("Lecturer bulunamadı"));
        } else if (user.getTitle().equals(String.valueOf(Role.COMPANY))) {
            Optional<Company> company = companyRepository.findByCompanyId(UUID.fromString(user.getUsername()));
            userDto = company.orElseThrow(() -> new RuntimeException("Company bulunamadı"));
        } else if (user.getTitle().equals(String.valueOf(Role.COMMISSION))) {

            Optional<Commission> commission = commissionRepository.findById(UUID.fromString(user.getUsername()));
            userDto = commission.orElseThrow(() -> new RuntimeException("Commission bulunamadı"));
        } else if (user.getTitle().equals(String.valueOf(Role.STUDENT))) {
            Optional<Student> student = studentRepository.findByStudentNumber(String.valueOf(user.getUsername()));
            if (student.isEmpty()) {
                return ApiResponse.error("Öğrenci bulunamadı", 404);
            }
            userDto = student.orElseThrow(() -> new RuntimeException("Student bulunamadı"));
        }

        // Response döndür
UserResponse userResponse = UserResponse.builder().userDto(userDto).token(token).build();
return ApiResponse.success(userResponse, "Giriş başarılı");
}


}
