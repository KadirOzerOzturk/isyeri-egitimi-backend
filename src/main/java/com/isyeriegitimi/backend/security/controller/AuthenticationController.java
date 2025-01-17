package com.isyeriegitimi.backend.security.controller;

import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthenticationController {

        private final AuthenticationService authenticationService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<T>> save(@RequestBody UserRequest userRequest) {
            return ResponseEntity.ok(authenticationService.save(userRequest));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<T>> auth(@RequestBody UserRequest userRequest) {
            return ResponseEntity.ok(authenticationService.auth(userRequest));
        }
    }
