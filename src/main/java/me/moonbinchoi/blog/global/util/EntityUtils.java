package me.moonbinchoi.blog.global.util;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class EntityUtils {

    public static <T> T getEntityOrThrow(Optional<T> optional, Object id) {
        return optional.orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("%s not found with id: %s", optional.getClass().getSimpleName(), id)));
    }
}
