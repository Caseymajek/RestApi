package com.RestApi.RestfulApi.serviceImpl;

import com.RestApi.RestfulApi.dto.EditPostRequestDto;
import com.RestApi.RestfulApi.dto.PostDto;
import com.RestApi.RestfulApi.model.Post;
import com.RestApi.RestfulApi.model.Users;
import com.RestApi.RestfulApi.repository.PostRepository;
import com.RestApi.RestfulApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl {
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Post> savePost(Post newPost) {
        Post post = new Post(newPost.getTitle(), newPost.getDescription());
        postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);

    }

    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> postList = postRepository.findAll();
        return new ResponseEntity<>(postList, HttpStatus.FOUND);
    }

    public ResponseEntity<Post> editPostById(Long id, Post newPost){ //throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()) {
            Post post1 = post.get();
            post1.setTitle(newPost.getTitle());
            post1.setDescription(newPost.getDescription());
            postRepository.save(post1);
            return new ResponseEntity<>(post1, HttpStatus.OK);
        }else{
            //You can use either of the two
            //throw new PostNotFoundException("post not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<Post> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post post1 = post.get();
            return new ResponseEntity<>(post1, HttpStatus.FOUND);
        }else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Void> deletePostById(Long id) {
       Optional<Post> post = postRepository.findById(id);

       if(post.isPresent()) {
           Post post1 = post.get();
           postRepository.deleteById(post1.getId());
           return new ResponseEntity<>(HttpStatus.OK);
       }else{
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
           //OR//
           //postRepository.deleteById(id);
           //return new ResponseEntity<>(HttpStatus.OK);
       }

    public ResponseEntity<List<Post>> getPostByPostTitle(String title) {
        List<Post> postList = postRepository.findPostByTitleIgnoreCaseContains(title);

        if(postList.isEmpty()) {
            return new ResponseEntity<>(postList, HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(postList, HttpStatus.FOUND);
        }
    }

    public ResponseEntity<String> createPost(PostDto postDto, Long userId) {
        Users optionalUser = this.userRepository.findById(userId).orElseThrow(()->new RuntimeException("\"No user with ID \" + userId + \" found in my database\""));

            Post post = new Post();
            post.setTitle(postDto.getPostTitle());
            post.setDescription(postDto.getContent());
            post.setUsers(String.valueOf(optionalUser));
            Post post1 = this.postRepository.save(post);
            return new ResponseEntity<>(post1.getTitle(), HttpStatus.OK);
        }

    public ResponseEntity<String> saveUsers(PostDto postDto) {
        Post post = new Post(postDto.getPostTitle(), postDto.getContent());
        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<String> editPostContent(EditPostRequestDto editPostRequestDto, Long userId, Long postId) {
        Post optionalPost = this.postRepository.findById(postId).orElseThrow(() -> new RuntimeException("No post with ID " + postId + " found in my database"));
        Users optionalUser = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with ID " + userId + " found in my database"));


        if (!optionalPost.getUsers().equals(optionalUser)){
            throw new RuntimeException("Please you can not edit this post simply because you were not the one that made the posted ");
        }

        optionalPost.setDescription(editPostRequestDto.getContent());
        this.postRepository.save(optionalPost);

        return new ResponseEntity<>("Post successfully edited", HttpStatus.OK  );
    }
}
