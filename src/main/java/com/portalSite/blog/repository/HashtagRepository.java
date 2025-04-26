package com.portalSite.blog.repository;

import com.portalSite.blog.entity.Hashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    List<Hashtag> findByTagIn(List<String> tags);
}
