package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreDTO.class);
        CentreDTO centreDTO1 = new CentreDTO();
        centreDTO1.setId(1L);
        CentreDTO centreDTO2 = new CentreDTO();
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
        centreDTO2.setId(centreDTO1.getId());
        assertThat(centreDTO1).isEqualTo(centreDTO2);
        centreDTO2.setId(2L);
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
        centreDTO1.setId(null);
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
    }
}
