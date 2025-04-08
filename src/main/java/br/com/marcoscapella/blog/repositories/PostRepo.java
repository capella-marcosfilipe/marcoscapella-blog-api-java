package br.com.marcoscapella.blog.repositories;

import br.com.marcoscapella.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
}
