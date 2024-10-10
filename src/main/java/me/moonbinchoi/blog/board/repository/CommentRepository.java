package me.moonbinchoi.blog.board.repository;

import me.moonbinchoi.blog.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //
}
