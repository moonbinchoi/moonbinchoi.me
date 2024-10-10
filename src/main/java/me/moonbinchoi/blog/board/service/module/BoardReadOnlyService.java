package me.moonbinchoi.blog.board.service.module;

import me.moonbinchoi.blog.board.domain.BoardEntity;

import java.util.Optional;

public interface BoardReadOnlyService<T extends BoardEntity, ID> {

    Optional<T> findById(final ID id);

    Iterable<T> findAll();

    boolean existsById(final ID id);
}
