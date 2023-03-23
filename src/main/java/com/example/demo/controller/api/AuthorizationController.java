package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.authorization.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping("/login")
    public ResponseEntity<BaseResponse> login (
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return new ResponseEntity<>(authorizationService.login(username, password), HttpStatus.OK);
    }

}
