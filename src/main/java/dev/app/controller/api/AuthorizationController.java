package dev.app.controller.api;

import dev.app.controller.response.BaseResponse;
import dev.app.service.authorization.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
