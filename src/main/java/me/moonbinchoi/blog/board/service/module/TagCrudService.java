package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.board.repository.TagRepository;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagCrudService implements BoardCrudService<Tag, Long> {

    private final TagRepository repository;

    @Override
    public Tag save(Tag request) {
        return repository.save(request);
    }

    @Override
    public Optional<Tag> findById(final Long id) {
        return repository.findById(id);
    }

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public List<Tag> findAll(final Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    public Tag update(final Long id, final Tag request) {
        Tag found = EntityUtils.getEntityOrThrow(repository.findById(id), id);
        found.setName(request.getName());

        return found;
    }

    @Override
    public void deleteById(final Long id) {
        Tag found = EntityUtils.getEntityOrThrow(repository.findById(id), id);
        repository.delete(found);
    }

    public void delete(final Tag tag) {
        repository.delete(tag);
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }

    public boolean existsAllById(final Collection<Long> ids) {
        return repository.existsAllById(ids);
    }
}
