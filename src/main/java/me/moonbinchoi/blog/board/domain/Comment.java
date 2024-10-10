package me.moonbinchoi.blog.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


@Entity
@ToString(callSuper = true)
@NoArgsConstructor
public class Comment extends BoardEntity {

    /* Comment metadata */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Getter
    private Post post;

    @Column(nullable = false)
    @Getter
    private String author;

    @Column(nullable = false)
    @Getter
    private Boolean isDeleted = Boolean.FALSE;

    @Column(nullable = false, updatable = false)
    @Setter
    @Getter
    private Boolean isPrivate;

    /* Reply */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Getter
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @Getter
    private List<Comment> children = new ArrayList<>();

    /* Body */
    @Getter
    @Setter
    @Column(nullable = false)
    private String body;

    @Builder
    public Comment(Post post, String author, Boolean isPrivate, @Nullable Comment parent, String body) {
        this.post = post;
        this.author = author;
        this.isPrivate = isPrivate != null ? isPrivate : Boolean.FALSE;
        this.parent = parent;
        this.body = body;
    }

    public void softDelete() {
        isDeleted = true;
    }

    public Boolean childExists() {
        return !children.isEmpty();
    }

    public int childCount() {
        return children.size();
    }

    public void addChild(Comment child) {
        children.add(child);
    }

    public void deleteChild(Comment child) {
        if (child.childExists()) {
            child.softDelete();
            return;
        }
        children.remove(child);
    }
}
