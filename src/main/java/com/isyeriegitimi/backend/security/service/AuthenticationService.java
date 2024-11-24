package com.isyeriegitimi.backend.security.service;

import com.isyeriegitimi.backend.dto.CommissionDto;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.dto.UserResponse;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.model.User;
import com.isyeriegitimi.backend.security.repository.UserRepository;
import com.isyeriegitimi.backend.service.CommissionService;
import com.isyeriegitimi.backend.service.CompanyService;
import com.isyeriegitimi.backend.service.LecturerService;
import com.isyeriegitimi.backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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




    public UserResponse save(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .title(userRequest.getTitle())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .build();


        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();

    }

    public UserResponse auth(UserRequest userRequest) {
        // Authenticate the user with the provided credentials
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        // Fetch the user from the repository
        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow();

        // Generate the JWT token
        String token = jwtService.generateToken(user);

        // Create the appropriate DTO based on the user type
        Object userDto = null;
        if (user.getTitle().equals(String.valueOf(Role.LECTURER))) {
            Optional<Lecturer> lecturer = lecturerService.getLecturerByLecturerId(Long.parseLong(user.getUsername()));

            // Map the user to LecturerDto
            userDto = lecturerService.mapToDto(lecturer.get());
        } else if (user.getTitle().equals(String.valueOf(Role.COMPANY))) {
            Optional<Company> company = companyService.getCompanyById(Long.parseLong(user.getUsername()));

            // Map the user to CompanyDto
            userDto =companyService.mapToDto(company.get());
        } else if (user.getTitle().equals(String.valueOf(Role.COMMISSION))) {
            Long commissionId=Long.parseLong(user.getUsername());
            Optional<CommissionDto> commission = commissionService.getCommissionById(commissionId);

            // Map the user to CommissionDto
            userDto = commission;
        } else if (user.getTitle().equals(String.valueOf(Role.STUDENT))) {
            Optional<Student> student = studentService.getStudentByStudentNo(Long.parseLong(user.getUsername()));

            // Map the user to StudentDto
            userDto = studentService.mapToDto(student.get());
        }

        // Return the UserResponse with the appropriate DTO
        return UserResponse.builder()
                .token(token)
                .userDto(userDto)
                .build();
    }

}
