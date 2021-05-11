package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreRegroupementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreRegroupementDTO.class);
        CentreRegroupementDTO centreRegroupementDTO1 = new CentreRegroupementDTO();
        centreRegroupementDTO1.setId(1L);
        CentreRegroupementDTO centreRegroupementDTO2 = new CentreRegroupementDTO();
        assertThat(centreRegroupementDTO1).isNotEqualTo(centreRegroupementDTO2);
        centreRegroupementDTO2.setId(centreRegroupementDTO1.getId());
        assertThat(centreRegroupementDTO1).isEqualTo(centreRegroupementDTO2);
        centreRegroupementDTO2.setId(2L);
        assertThat(centreRegroupementDTO1).isNotEqualTo(centreRegroupementDTO2);
        centreRegroupementDTO1.setId(null);
        assertThat(centreRegroupementDTO1).isNotEqualTo(centreRegroupementDTO2);
    }
}
