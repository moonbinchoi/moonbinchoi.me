package me.moonbinchoi.blog.board.service.module;

import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.PostTagRelation;
import me.moonbinchoi.blog.board.repository.PostTagRelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostTagRelationReadOnlyService implements BoardReadOnlyService<PostTagRelation, Long> {

    private final PostTagRelationRepository repository;

    @Override
    public Optional<PostTagRelation> findById(final Long id) {
        return repository.findById(id);
    }

    public List<PostTagRelation> findByTagId(final Long tagId) {
        return repository.findByTagId(tagId);
    }

    @Override
    public Iterable<PostTagRelation> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }
}
