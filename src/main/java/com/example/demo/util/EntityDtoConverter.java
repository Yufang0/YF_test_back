package com.example.demo.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for converting between entity and DTO objects
 */
@Slf4j
public class EntityDtoConverter {

    private EntityDtoConverter() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a source object to a target class using Spring BeanUtils
     *
     * @param <S>         source type
     * @param <T>         target type
     * @param source      source object
     * @param targetClass target class
     * @return converted target object
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("Error converting from {} to {}: {}",
                    source.getClass().getSimpleName(), targetClass.getSimpleName(), e.getMessage());
            throw new RuntimeException("Error converting between objects", e);
        }
    }

    /**
     * Converts a list of source objects to target class objects
     *
     * @param <S>         source type
     * @param <T>         target type
     * @param sourceList  list of source objects
     * @param targetClass target class
     * @return list of converted target objects
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(source -> convert(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Converts an optional source object to target class
     *
     * @param <S>         source type
     * @param <T>         target type
     * @param source      optional source object
     * @param targetClass target class
     * @return optional of converted target object
     */
    public static <S, T> Optional<T> convertOptional(Optional<S> source, Class<T> targetClass) {
        return source.map(s -> convert(s, targetClass));
    }

    /**
     * Creates a builder for more complex conversions
     *
     * @param <S>         source type
     * @param <T>         target type
     * @param source      source object
     * @param targetClass target class
     * @return conversion builder
     */
    public static <S, T> ConversionBuilder<S, T> createBuilder(S source, Class<T> targetClass) {
        return new ConversionBuilder<>(source, targetClass);
    }

    /**
     * Builder class for more complex object conversion scenarios
     *
     * @param <S> source type
     * @param <T> target type
     */
    public static class ConversionBuilder<S, T> {
        private final S source;
        private T target;

        private ConversionBuilder(S source, Class<T> targetClass) {
            this.source = source;

            try {
                this.target = targetClass.getDeclaredConstructor().newInstance();
                if (source != null) {
                    BeanUtils.copyProperties(source, target);
                }
            } catch (Exception e) {
                log.error("Error creating instance of {}: {}", targetClass.getSimpleName(), e.getMessage());
                throw new RuntimeException("Error creating conversion builder", e);
            }
        }

        /**
         * Apply a custom mapping function
         *
         * @param customMapping the custom mapping function
         * @return the builder instance
         */
        public ConversionBuilder<S, T> withCustomMapping(CustomMapping<S, T> customMapping) {
            if (source != null) {
                customMapping.apply(source, target);
            }
            return this;
        }

        /**
         * Build the final converted object
         *
         * @return the converted target object
         */
        public T build() {
            return target;
        }
    }

    /**
     * Functional interface for custom mapping operations
     *
     * @param <S> source type
     * @param <T> target type
     */
    @FunctionalInterface
    public interface CustomMapping<S, T> {
        void apply(S source, T target);
    }
}
