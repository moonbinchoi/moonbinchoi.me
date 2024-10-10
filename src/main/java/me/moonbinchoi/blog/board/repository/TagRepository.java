package me.moonbinchoi.blog.board.repository;

import me.moonbinchoi.blog.board.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // Returns empty List if not match
    default boolean existsAllById(Collection<Long> ids) {
        List<Tag> tags = findAllById(ids);
        return tags.size() == ids.size();
    }
}

