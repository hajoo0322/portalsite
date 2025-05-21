package com.portalSite.dataSync.controller;

import com.portalSite.dataSync.service.DataSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data/sync")
public class DataSyncController {

    private final DataSyncService dataSyncService;

    @PostMapping("/news")
    public ResponseEntity<Void> syncNews() throws IOException {
        dataSyncService.syncAllNews();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/blog")
    public ResponseEntity<Void> syncBlog() throws IOException {
        dataSyncService.syncAllBlogPosts();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cafe")
    public ResponseEntity<Void> syncCafe() throws IOException {
        dataSyncService.syncAllCafePosts();
        return ResponseEntity.ok().build();
    }
}
