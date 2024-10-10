package me.moonbinchoi.blog.board.service.module;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.dto.request.PostRequest;
import me.moonbinchoi.blog.board.repository.PostRepository;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCrudService implements BoardCrudService<Post, Long> {

    private final PostRepository repository;

    @Override
    public Post save(final Post request) {
        return repository.save(request);
    }

    @Override
    public Optional<Post> findById(final Long id) {
        return repository.findById(id);
    }

    public List<Post> findAll() {
        return repository.findAll();
    }

    public Page<Post> findAll(final PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public Post update(final Long id, final Post request) {
        Post found = EntityUtils.getEntityOrThrow(repository.findById(id), id);

        found.updateTags(request.getTags());
        found.setTitle(request.getTitle());
        found.setContent(request.getContent());

        return found;
    }

    @Override
    public void deleteById(final Long id) {
        final Post entity = EntityUtils.getEntityOrThrow(repository.findById(id), id);
        repository.delete(entity);
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }
}
