package me.moonbinchoi.blog.board.service.module;

import me.moonbinchoi.blog.board.domain.BoardEntity;

import java.util.Optional;

public interface BoardCrudService<T extends BoardEntity, ID> {

    T save(final T entity);

    Optional<T> findById(final ID id);

    Iterable<T> findAll();

    T update(final ID id, final T entity);

    void deleteById(final ID id);

    boolean existsById(final ID id);
}
