package com.onea.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceApprovEpMapperTest {

    private SourceApprovEpMapper sourceApprovEpMapper;

    @BeforeEach
    public void setUp() {
        sourceApprovEpMapper = new SourceApprovEpMapperImpl();
    }
}
