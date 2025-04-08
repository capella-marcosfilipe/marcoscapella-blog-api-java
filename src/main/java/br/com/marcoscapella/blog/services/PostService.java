package br.com.marcoscapella.blog.services;

import br.com.marcoscapella.blog.models.Post;
import br.com.marcoscapella.blog.models.Tag;
import br.com.marcoscapella.blog.repositories.PostRepo;
import br.com.marcoscapella.blog.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired private PostRepo postRepo;
    @Autowired private TagRepo tagRepo;

    public List<Post> getAllPosts() {
        return (List<Post>)postRepo.findAll();
    }

    public Optional<Post> getPostBySlug(String slug) {
        return postRepo.findBySlug(slug);
    }

    public Post createPost(Post post) {
        // Check if the post object have tags (not null and not empty)
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            // Resolve tag names into actual Tag entities from the DB
            Set<Tag> resolvedTags = post.getTags().stream().map(tag -> {
                // Find a tag in the DB with the same name
                return tagRepo.findByName(tag.getName())
                        .orElseGet(() -> { // if the tag doesn't exist, (...)
                    // (...) create a new tag entity
                    Tag newTag = new Tag();
                    newTag.setName(tag.getName());

                    // Save the new tag to the DB so it gets an ID.
                    return tagRepo.save(newTag);
                });
            }).collect(Collectors.toSet());

            // Associate all the resolved tag entities (old and new) with the post.
            post.setTags(resolvedTags);
        }

        // Creating slug name
        post.setSlug(
                post.getTitle().toLowerCase().replaceAll(" ", "-")
        );

        // Finally save the post
        return postRepo.save(post);
    }

    public Post updatePost(Post post) {
        return postRepo.save(post);
    }

    public void deletePost(Post post) {
        postRepo.delete(post);
    }
}
