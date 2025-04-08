package br.com.marcoscapella.blog.controllers;

import br.com.marcoscapella.blog.models.Post;
import br.com.marcoscapella.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Optional<Post>> getPostBySlug(@PathVariable String slug) {
        Optional<Post> post = postService.getPostBySlug(slug);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

}
