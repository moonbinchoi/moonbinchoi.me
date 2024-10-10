package me.moonbinchoi.blog.board.repository;

import me.moonbinchoi.blog.board.domain.PostTagRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRelationRepository extends JpaRepository<PostTagRelation, Long> {

    boolean existsByTagId(Long tagId);

    List<PostTagRelation> findByTagId(Long tagId);
}
