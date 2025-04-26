package com.portalSite.comment.controller;

import com.portalSite.comment.dto.request.CommentAlarmRequest;
import com.portalSite.comment.service.CommentAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class CommentAlarmController {
    private final CommentAlarmService commentAlarmService;

    @PostMapping
    public ResponseEntity<Void> registerAlarm(
            @RequestBody CommentAlarmRequest request
            ) {
        commentAlarmService.registerAlarm(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAlarm(
            @RequestBody CommentAlarmRequest request
    ) {
        commentAlarmService.cancelAlarm(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
