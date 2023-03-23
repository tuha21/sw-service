package com.example.demo.service.authorization;

import com.example.demo.common.consts.ErrorMessageConst;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.authorization.AuthorizationData;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    public BaseResponse login (String username, String password) {
        authenticate(username, password);
        var response = new BaseResponse();
        AuthorizationData data = new AuthorizationData();
        var user = userDetailService.loadUserByUsername(username);
        if (user != null) {
            var token = jwtUtil.generateToken(user.getUsername());
            data.setToken(token);
        } else {
            response.setError(ErrorMessageConst.AuthErrorMessage.ERROR_USERNAME_NOT_EXIST);
        }
        response.setData(data);
        return response;
    }

    public void authenticate (String username, String password) {
        var user = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(user);
    }

}
