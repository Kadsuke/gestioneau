package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NatureOuvrageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NatureOuvrageDTO.class);
        NatureOuvrageDTO natureOuvrageDTO1 = new NatureOuvrageDTO();
        natureOuvrageDTO1.setId(1L);
        NatureOuvrageDTO natureOuvrageDTO2 = new NatureOuvrageDTO();
        assertThat(natureOuvrageDTO1).isNotEqualTo(natureOuvrageDTO2);
        natureOuvrageDTO2.setId(natureOuvrageDTO1.getId());
        assertThat(natureOuvrageDTO1).isEqualTo(natureOuvrageDTO2);
        natureOuvrageDTO2.setId(2L);
        assertThat(natureOuvrageDTO1).isNotEqualTo(natureOuvrageDTO2);
        natureOuvrageDTO1.setId(null);
        assertThat(natureOuvrageDTO1).isNotEqualTo(natureOuvrageDTO2);
    }
}
