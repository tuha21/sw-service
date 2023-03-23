package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.connect.ConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/connect")
public class ConnectController {

    private final ConnectService connectService;

    @GetMapping("/install")
    public ResponseEntity<BaseResponse> getConnectLink (
            @RequestParam("tenantId") int tenantId
    ) {
        return new ResponseEntity<>(connectService.getConnectLink(tenantId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getConnections (
        @RequestParam("tenantId") int tenantId
    ) {
        return new ResponseEntity<>(connectService.getConnections(tenantId), HttpStatus.OK);
    }

}
