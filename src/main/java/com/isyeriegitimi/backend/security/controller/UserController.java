package com.isyeriegitimi.backend.security.controller;


import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.security.dto.UserDto;
import com.isyeriegitimi.backend.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update/{studentNo}")
    public ResponseEntity<ApiResponse<String>> updateUser(@RequestBody UserDto userDto) {

        userService.update(userDto);
        return ResponseEntity.ok(ApiResponse.success(null,"User updated successfully"));

    }

}

