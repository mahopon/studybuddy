package com.tcyao.studybuddy.status;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {
    @GetMapping("")
    public ResponseEntity<StatusResponse> getStatus() {
        StatusResponse resp = new StatusResponse("up");
        return ResponseEntity.ok(resp);
    }
}
