package com.RestApi.RestfulApi.repository;

import com.RestApi.RestfulApi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostByTitleIgnoreCaseContains(String title);
}
