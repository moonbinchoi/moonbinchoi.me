package me.moonbinchoi.blog.board.service.component;

import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.service.module.PostCrudService;
import me.moonbinchoi.blog.board.service.module.TagCrudService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostCrudService postCrudService;
    private final TagCrudService tagCrudService;

    public Post createPost(final Post entity, final Set<Long> tagIds) {
        if (!tagIds.isEmpty()) {
            tagCrudService.findAll(tagIds).forEach(entity::addTag);
        }
        return postCrudService.save(entity);
    }

    public Optional<Post> findPost(final Long id) {
        return postCrudService.findById(id);
    }

    public List<Post> findAllPosts() {
        return postCrudService.findAll();
    }

    public List<Post> findLatestPosts(final int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("created").descending());

        return postCrudService.findAll(pageRequest).getContent();
    }

    public Post updatePost(final Long id, final Post entity, final Set<Long> tagIds) {
        if (!tagIds.isEmpty()) {
            tagCrudService.findAll(tagIds).forEach(entity::addTag);
        }
        return postCrudService.update(id, entity);
    }

    public void deletePost(final Long id) {
        postCrudService.deleteById(id);
    }
}
