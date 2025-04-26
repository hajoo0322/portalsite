package com.portalSite.blog.service;

import com.portalSite.blog.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public void saveHashtag() {

        hashtagRepository.save()
    }

}
