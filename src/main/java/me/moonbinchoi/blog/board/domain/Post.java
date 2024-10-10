package me.moonbinchoi.blog.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.*;

@Entity
@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class Post extends BoardEntity {

    /* Post metadata */
    @Column(nullable = false)
    private String author;

    // Relation table with Tag
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTagRelation> postTagRelations;

    /* Body */
    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false)
    @Setter
    private String content;

    /* Comments */
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("created asc")
    private List<Comment> comments;

    @Builder
    public Post(final String author, final String title, final String content, @Nullable final Collection<Tag> tags) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.postTagRelations = new HashSet<>();
        this.comments = new ArrayList<>();

        if (tags != null) {
            tags.forEach(this::addTag);
        }
    }

    /* Methods */
    public void addTag(Tag tag) {
        PostTagRelation relation = new PostTagRelation(this, tag);
        postTagRelations.add(relation);
        tag.addRelation(relation);
    }

    public void removeTag(Tag tag) {
        PostTagRelation targetRelation = postTagRelations.stream()
                .filter(t -> t.getTag().equals(tag))
                .findFirst()
                .orElse(null);

        if (targetRelation == null) {
            return;
        }
        postTagRelations.remove(targetRelation);
        tag.removeRelation(targetRelation);
    }

    public void clearTags() {
        Set<Tag> tags = getTags();
        tags.forEach(this::removeTag);
    }

    public void updateTags(Set<Tag> newTags) {
        Set<Tag> originTags = getTags();

        if (originTags.equals(newTags)) {
            return;
        }

        if (originTags.isEmpty()) {
            newTags.forEach(this::addTag);
            return;
        }

        if (newTags.isEmpty()) {
            clearTags();
            return;
        }

        // Add (newTags - originTags) to this
        newTags.stream()
                .filter(t -> !originTags.contains(t))
                .forEach(this::addTag);

        // Remove (originTags - newTags) from this
        originTags.stream()
                .filter(t -> !newTags.contains(t))
                .forEach(this::removeTag);
    }

    public Set<Tag> getTags() {
        Set<Tag> tags = new HashSet<>();
        for (PostTagRelation relation : postTagRelations) {
            tags.add(relation.getTag());
        }
        return tags;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
