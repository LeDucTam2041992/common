package com.kpro.common.excel;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public interface Parser<T, E extends BatchBaseEntity> {
    T parse(JpaRepository<E, Object> repository, Function<Row, E> toEntity, String id, String filePath, int maxTransaction, String language);
}
