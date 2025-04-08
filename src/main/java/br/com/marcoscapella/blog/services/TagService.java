package br.com.marcoscapella.blog.services;

import br.com.marcoscapella.blog.models.Tag;
import br.com.marcoscapella.blog.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired private TagRepo tagRepo;

    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    public Optional<Tag> getTagByName(String name) {
        return tagRepo.findByName(name);
    }

    public Tag createTag(Tag tag) {
        return tagRepo.save(tag);
    }

    public void deleteTag(Long id) {
        tagRepo.deleteById(id);
    }
}
