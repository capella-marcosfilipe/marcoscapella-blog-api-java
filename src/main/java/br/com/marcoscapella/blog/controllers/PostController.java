package br.com.marcoscapella.blog.controllers;

import br.com.marcoscapella.blog.dtos.PostRequestDTO;
import br.com.marcoscapella.blog.dtos.PostResponseDTO;
import br.com.marcoscapella.blog.models.Post;
import br.com.marcoscapella.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired private PostService postService;

    @GetMapping
    public List<Post> getAll() {
        return postService.getAllPosts();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> getPostBySlug(@PathVariable String slug) {
        Post post = postService.getPostBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        PostResponseDTO dto = postService.convertToResponseDTO(post);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequestDTO postDto) {
        Post post = postService.createPost(postDto);
        return postService.createPost(postDto);
    }

    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping
    public void deletePost(@RequestBody Post post) {
        postService.deletePost(post);
    }

}
