package com.onea.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SecteurMapperTest {

    private SecteurMapper secteurMapper;

    @BeforeEach
    public void setUp() {
        secteurMapper = new SecteurMapperImpl();
    }
}
