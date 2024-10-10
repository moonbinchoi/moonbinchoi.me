package me.moonbinchoi.blog.board.service.component;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.PostTagRelation;
import me.moonbinchoi.blog.board.domain.Tag;
import me.moonbinchoi.blog.board.service.module.PostTagRelationReadOnlyService;
import me.moonbinchoi.blog.board.service.module.TagCrudService;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagCrudService tagCrudService;
    private final PostTagRelationReadOnlyService postTagRelationReadOnlyService;

    public Tag createTag(Tag entity) {
        return tagCrudService.save(entity);
    }

    public Optional<Tag> findTag(Long id) {
        return tagCrudService.findById(id);
    }

    public List<Tag> findAllTags() {
        return tagCrudService.findAll();
    }

    public List<Tag> findAllTagsByIds(final Collection<Long> ids) {
        return tagCrudService.findAll(ids);
    }

    public Tag updateTag(Long id, Tag entity) {
        return tagCrudService.update(id, entity);
    }

    public void deleteTag(Long id) {
        final Tag found = EntityUtils.getEntityOrThrow(tagCrudService.findById(id), id);

        //find relational posts
        List<PostTagRelation> relations = postTagRelationReadOnlyService.findByTagId(id);
        if (!relations.isEmpty()) {
            //disconnect by Posts
            relations.forEach(relation -> relation.getPost().removeTag(found));
        }
        tagCrudService.delete(found);
    }
}
