package com.RestApi.RestfulApi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {


//        @Test
//        public void testSavePost() {
//            // Mock the PostRepository
//            PostRepository postRepository = mock(PostRepository.class);
//
//            // Create an instance of the PostService (or whatever class contains the savePost method)
//            PostService postService = new PostService(postRepository);
//
//            // Create a sample Post
//            Post newPost = new Post("Test Title", "Test Description");
//
//            // Mock the save method of PostRepository to return the saved post
//            when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
//                Post savedPost = invocation.getArgument(0);
//                savedPost.setId(1L); // Assuming an ID is set after saving
//                return savedPost;
//            });
//
//            // Call the method to be tested
//            ResponseEntity<Post> responseEntity = postService.savePost(newPost);
//
//            // Verify that the save method was called with the correct argument
//            verify(postRepository, times(1)).save(any(Post.class));
//
//            // Verify the response status and content
//            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//            assertEquals(newPost, responseEntity.getBody());
//            assertEquals(1L, responseEntity.getBody().getId()); // Assuming ID is set in the response
//
//            // You can add more assertions based on your specific requirements
//        }
//    }
//
//
}