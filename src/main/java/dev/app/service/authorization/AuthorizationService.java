package dev.app.service.authorization;

import dev.app.common.consts.ErrorMessageConst;
import dev.app.controller.response.BaseResponse;
import dev.app.controller.response.authorization.AuthorizationData;
import dev.app.security.JwtUtil;
import dev.app.security.UserDetailService;
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
