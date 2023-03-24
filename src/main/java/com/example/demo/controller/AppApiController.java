package com.example.demo.controller;

import com.example.demo.controller.response.AppInfoData;
import com.example.demo.controller.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AppApiController {

    @GetMapping("/app-info")
    public ResponseEntity<BaseResponse> getAppInfo () {
        BaseResponse baseResponse = new BaseResponse();
        var appInfo = AppInfoData.builder()
            .appName("SW Service")
            .author("Kakarot")
            .version("2023.03.v1")
            .build();
        baseResponse.setData(appInfo);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
