package com.onea.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CentreMapperTest {

    private CentreMapper centreMapper;

    @BeforeEach
    public void setUp() {
        centreMapper = new CentreMapperImpl();
    }
}
