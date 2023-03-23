package com.example.demo.controller.api;

import com.example.demo.service.connect.CallbackService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/callback")
@RequiredArgsConstructor
public class CallbackController {

    private final CallbackService callbackService;

    @RequestMapping(value = "/tik-tok")
    public void callbackTikTok (
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
        callbackService.callbackTikTok(code, state, response, request);
    }

}
