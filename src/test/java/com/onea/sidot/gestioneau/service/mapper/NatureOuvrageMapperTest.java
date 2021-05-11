package com.onea.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NatureOuvrageMapperTest {

    private NatureOuvrageMapper natureOuvrageMapper;

    @BeforeEach
    public void setUp() {
        natureOuvrageMapper = new NatureOuvrageMapperImpl();
    }
}
