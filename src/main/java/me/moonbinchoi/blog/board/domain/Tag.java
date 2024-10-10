package me.moonbinchoi.blog.board.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@ToString(callSuper = true)
@NoArgsConstructor
public class Tag extends BoardEntity {

    @Setter
    @Getter
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<PostTagRelation> postTagRelations = new HashSet<>();

    public Tag(String name) {
        this.name = name;
    }

    public void addRelation(PostTagRelation relation) {
        postTagRelations.add(relation);
    }

    public void removeRelation(PostTagRelation postTagRelation) {
        postTagRelations.remove(postTagRelation);
    }

    public int getRelationCount() {
        return postTagRelations.size();
    }
    
}
