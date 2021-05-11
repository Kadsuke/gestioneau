package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommuneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommuneDTO.class);
        CommuneDTO communeDTO1 = new CommuneDTO();
        communeDTO1.setId(1L);
        CommuneDTO communeDTO2 = new CommuneDTO();
        assertThat(communeDTO1).isNotEqualTo(communeDTO2);
        communeDTO2.setId(communeDTO1.getId());
        assertThat(communeDTO1).isEqualTo(communeDTO2);
        communeDTO2.setId(2L);
        assertThat(communeDTO1).isNotEqualTo(communeDTO2);
        communeDTO1.setId(null);
        assertThat(communeDTO1).isNotEqualTo(communeDTO2);
    }
}
