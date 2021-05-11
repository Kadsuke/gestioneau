package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SecteurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecteurDTO.class);
        SecteurDTO secteurDTO1 = new SecteurDTO();
        secteurDTO1.setId(1L);
        SecteurDTO secteurDTO2 = new SecteurDTO();
        assertThat(secteurDTO1).isNotEqualTo(secteurDTO2);
        secteurDTO2.setId(secteurDTO1.getId());
        assertThat(secteurDTO1).isEqualTo(secteurDTO2);
        secteurDTO2.setId(2L);
        assertThat(secteurDTO1).isNotEqualTo(secteurDTO2);
        secteurDTO1.setId(null);
        assertThat(secteurDTO1).isNotEqualTo(secteurDTO2);
    }
}
