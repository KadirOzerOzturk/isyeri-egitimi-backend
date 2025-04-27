package com.isyeriegitimi.backend.security.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.*;
import com.isyeriegitimi.backend.security.dto.PasswordChangeRequest;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.dto.UserResponse;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.model.User;
import com.isyeriegitimi.backend.security.repository.UserRepository;
import com.isyeriegitimi.backend.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final JwtService jwtService;
    private final StudentService studentService;
    private final CompanyService companyService;
    private final CommissionService commissionService;
    private final LecturerService lecturerService;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final CommissionRepository commissionRepository;
    private final LecturerRepository lecturerRepository;
    private final MentorRepository mentorRepository;
    private final EmailService emailService;

    public ApiResponse save(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ApiResponse.error("Username already taken. Please try another. ", 400);
        }


        // Handle different user roles
        switch (userRequest.getTitle()){
            case "STUDENT":
                Student student = Student.builder()
                        .email(userRequest.getUsername())
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .build();
                studentService.save(studentService.mapToDto(student));
                break;
            case "COMPANY":
                Company company = Company.builder()
                        .email(userRequest.getUsername())
                        .name(userRequest.getName())
                        .build();
                companyService.save(companyService.mapToDto(company));
                break;
            case "LECTURER":
                Lecturer lecturer = Lecturer.builder()
                        .email(userRequest.getUsername())
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .build();
                lecturerService.save(lecturerService.mapToDto(lecturer));
                break;
            case "COMMISSION":
                Commission commission = Commission.builder()
                        .email(userRequest.getUsername())
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .build();
                commissionService.save(commissionService.mapToDto(commission));
                break;
        }

        User user= User.builder()
                    .username(userRequest.getUsername())
                    .title(userRequest.getTitle())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .role(Role.valueOf(userRequest.getTitle()))
                    .build();





        userRepository.save(user);

        System.out.println("User saved: " + user);

        return ApiResponse.success(user, "Successfully saved");
    }

    public ApiResponse auth(UserRequest userRequest) {
        User existingUser = userRepository.findByUsernameAndTitle(userRequest.getUsername(), userRequest.getTitle())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );

        String token = jwtService.generateToken(existingUser);
        System.out.println(token    );
        Object user = null;
        if (existingUser.getTitle().equals(String.valueOf(Role.LECTURER))) {
            Optional<Lecturer> lecturer = lecturerRepository.findByEmail(existingUser.getUsername());
            user = lecturer.orElseThrow(() -> new RuntimeException("Lecturer not found"));
        } else if (existingUser.getTitle().equals(String.valueOf(Role.COMPANY))) {
            Optional<Company> company = companyRepository.findByEmail(existingUser.getUsername());
            user = company.orElseThrow(() -> new RuntimeException("Company not found"));
        } else if (existingUser.getTitle().equals(String.valueOf(Role.COMMISSION))) {

            Optional<Commission> commission = commissionRepository.findByEmail(existingUser.getUsername());
            user = commission.orElseThrow(() -> new RuntimeException("Commission not found"));
        } else if (existingUser.getTitle().equals(String.valueOf(Role.STUDENT))) {
            Optional<Student> student = studentRepository.findByEmail(existingUser.getUsername());
            if (student.isEmpty()) {
                return ApiResponse.error("Student not found", 404);
            }
            user = student.orElseThrow(() -> new RuntimeException("Student not found"));
        } else if (existingUser.getTitle().equals(String.valueOf(Role.MENTOR))) {
            Optional<Mentor> mentor = mentorRepository.findByEmail(existingUser.getUsername());
            if (mentor.isEmpty()) {
                return ApiResponse.error("Mentor not found", 404);

            }
            user = mentor.orElseThrow(() -> new RuntimeException("Mentor not found"));
        }


        UserResponse userResponse = UserResponse.builder().user(user).token(token).build();
        return ApiResponse.success(userResponse, "Successfully logged in");
    }


  public ApiResponse<T> changePassword(PasswordChangeRequest request) {
    try {
        User user = userRepository.findByUsernameAndTitle(request.getUsername(), request.getTitle())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ApiResponse.error("Old password incorrect", 400);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return ApiResponse.error("New password cannot be the same as the old password", 400);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success(null, "Password changed successfully");
    } catch (UsernameNotFoundException e) {
        return ApiResponse.error("User not found", 404);
    } catch (Exception e) {
        return ApiResponse.error("An error occurred while changing the password", 500);
    }
}

    public void forgotPassword(String email) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(email);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("User", "email", email);
            }

            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            String resetLink = "http://localhost:3000/reset-password?token=" + token;

            Email resetEmail = new Email();
            resetEmail.setTo(email);
            resetEmail.setName(user.getUsername()); // isim veya e-posta adresi
            resetEmail.setSubject("Şifre Sıfırlama Talebi");

            emailService.sendResetPasswordEmail(resetEmail, resetLink);

        } catch (Exception e) {
            throw new InternalServerErrorException("Şifre sıfırlama e-postası gönderilirken hata oluştu: " + e.getMessage());
        }
    }

    public void resetPassword(String token, String newPassword) {
        if (token == null || token.trim().isEmpty()) {
            throw new InternalServerErrorException("Reset token is required");
        }

        try {
            Optional<User> userOptional = userRepository.findByResetToken(token);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("User", "token", token);
            }

            User user = userOptional.get();

            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new InternalServerErrorException("Reset token expired");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);

            Email resetConfirmationEmail = new Email();
            resetConfirmationEmail.setTo(user.getUsername());
            resetConfirmationEmail.setSubject("Şifre Başarıyla Sıfırlandı");
            resetConfirmationEmail.setMessage("Şifreniz başarıyla sıfırlandı. Eğer bu işlemi siz yapmadıysanız hemen bizimle iletişime geçin.");

            String confirmationLink = "http://localhost:8080/login-options";

            emailService.sendResetPasswordEmail(resetConfirmationEmail, confirmationLink);

        } catch (Exception e) {
            throw new InternalServerErrorException("Şifre sıfırlanırken hata oluştu: " + e.getMessage());
        }
    }



}
