package com.isyeriegitimi.backend.security.controller;

import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.security.dto.PasswordChangeRequest;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody UserRequest userRequest) {
        ApiResponse response =authenticationService.save(userRequest);
        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> auth(@RequestBody UserRequest userRequest) {

        ApiResponse response = authenticationService.auth(userRequest);

        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody PasswordChangeRequest request) {
        ApiResponse response = authenticationService.changePassword(request);
        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }
}
