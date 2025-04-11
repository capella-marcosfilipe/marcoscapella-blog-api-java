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
    public List<PostResponseDTO> getAll() {
        List<Post> posts = postService.getAllPosts();
        return posts.stream().map(postService::convertToResponseDTO).toList();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> getPostBySlug(@PathVariable String slug) {
        Post post = postService.getPostBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        PostResponseDTO dto = postService.convertToResponseDTO(post);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO postDto) {
        PostResponseDTO responseDTO = postService.createPost(postDto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePostPartially(
            @PathVariable Long id,
            @RequestBody PostRequestDTO updatedFields
    ) {
        Post updatedPost = postService.updatePostPartially(id, updatedFields);
        PostResponseDTO responseDTO = postService.convertToResponseDTO(updatedPost);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        if (!postService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

}
