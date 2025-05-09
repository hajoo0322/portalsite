package com.portalSite.comment.controller;

import com.portalSite.comment.dto.request.CommentAlarmRequest;
import com.portalSite.comment.service.CommentAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafe-members/{cafeMemberId}/cafe-posts/{cafePostId}/alarms")
@RequiredArgsConstructor
public class CommentAlarmController {
    private final CommentAlarmService commentAlarmService;

    @PostMapping
    public ResponseEntity<Void> registerAlarm(
            @PathVariable Long cafeMemberId,
            @PathVariable Long cafePostId
            ) {
        commentAlarmService.registerAlarm(cafeMemberId,cafePostId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAlarm(
            @PathVariable Long cafeMemberId,
            @PathVariable Long cafePostId
    ) {
        commentAlarmService.cancelAlarm(cafeMemberId,cafePostId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
