package com.isyeriegitimi.backend.security.controller;

import com.isyeriegitimi.backend.security.dto.UserDto;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.dto.UserResponse;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/authv2")
    @RequiredArgsConstructor
    public class AuthenticationController {

        private final AuthenticationService authenticationService;

        @PostMapping("/register")
        public ResponseEntity<UserResponse> save(@RequestBody UserRequest userRequest) {
            return ResponseEntity.ok(authenticationService.save(userRequest));
        }

        @PostMapping("/login")
        public ResponseEntity<UserResponse> auth(@RequestBody UserRequest userRequest) {
            return ResponseEntity.ok(authenticationService.auth(userRequest));
        }
    }
