package com.onea.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionMapperTest {

    private SectionMapper sectionMapper;

    @BeforeEach
    public void setUp() {
        sectionMapper = new SectionMapperImpl();
    }
}
