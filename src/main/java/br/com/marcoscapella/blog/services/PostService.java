package br.com.marcoscapella.blog.services;

import br.com.marcoscapella.blog.models.Post;
import br.com.marcoscapella.blog.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired private PostRepo postRepo;

    public List<Post> getAllPosts() {
        return (List<Post>)postRepo.findAll();
    }

    public Optional<Post> getPostBySlug(String slug) {
        return postRepo.findBySlug(slug);
    }

    public Post createPost(Post post) {
        // generating slug name
        post.setSlug(
                post.getTitle().toLowerCase().replaceAll(" ", "-")
        );
        return postRepo.save(post);
    }

    public Post updatePost(Post post) {
        return postRepo.save(post);
    }

    public void deletePost(Post post) {
        postRepo.delete(post);
    }
}
