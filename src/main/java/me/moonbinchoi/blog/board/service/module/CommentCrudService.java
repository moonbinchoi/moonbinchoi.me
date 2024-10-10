package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.Comment;
import me.moonbinchoi.blog.board.repository.CommentRepository;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentCrudService implements BoardCrudService<Comment, Long> {

    private final CommentRepository repository;

    @Override
    public Comment save(Comment request) {
        return repository.save(request);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    public Comment update(Long id, Comment request) {
        Comment found = EntityUtils.getEntityOrThrow(repository.findById(id), id);

        if (!Objects.equals(found.getAuthor(), request.getAuthor())) {
            throw new IllegalArgumentException(
                    String.format("Comment with request author %s and origin author %s not found",
                            request.getAuthor(), found.getAuthor()));
        }

        found.setIsPrivate(request.getIsPrivate());
        found.setBody(request.getBody());

        return repository.save(found);
    }

    @Override
    public void deleteById(Long id) {
        final Comment found = EntityUtils.getEntityOrThrow(repository.findById(id), id);
        repository.delete(found);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
