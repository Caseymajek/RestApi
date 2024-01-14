package com.RestApi.RestfulApi.controller;

import com.RestApi.RestfulApi.dto.EditPostRequestDto;
import com.RestApi.RestfulApi.dto.PostDto;
import com.RestApi.RestfulApi.model.Post;
import com.RestApi.RestfulApi.serviceImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class PostController {
    private PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/save-post")
    public ResponseEntity<Post> savePost (@RequestBody Post post){
        return postService.savePost(post);
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Post>> getAllPost(){
        return postService.getAllPost();
    }

    @PutMapping("/edit-post/{id}")
    public ResponseEntity<Post> editPostById (@PathVariable Long id, @RequestBody Post newPost) {//throws PostNotFoundException {
        return postService.editPostById(id, newPost);
    }

    @GetMapping("/get-post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        return postService.deletePostById(id);
    }
    @GetMapping("/get-post-title/{title}")
    public ResponseEntity<List<Post>> getPostByPostTitle(@PathVariable String title){
        return postService.getPostByPostTitle(title);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createBlogPost(@RequestBody PostDto postDto, @PathVariable Long userId){
        return postService.createPost(postDto,userId);
    }

    @PutMapping("/edit/{postId}/{userId}")
    public ResponseEntity<String> editPost(@RequestBody EditPostRequestDto editPostRequestDto, @PathVariable Long postId, @PathVariable Long userId){
        return
                postService.editPostContent(editPostRequestDto, userId, postId);
    }
    @PostMapping("/save-users")
    public ResponseEntity<String> saveUsers (@RequestBody PostDto postDto){
        return postService.saveUsers(postDto);
    }






}
