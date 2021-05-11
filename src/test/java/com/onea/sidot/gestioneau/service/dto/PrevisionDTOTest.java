package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrevisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrevisionDTO.class);
        PrevisionDTO previsionDTO1 = new PrevisionDTO();
        previsionDTO1.setId(1L);
        PrevisionDTO previsionDTO2 = new PrevisionDTO();
        assertThat(previsionDTO1).isNotEqualTo(previsionDTO2);
        previsionDTO2.setId(previsionDTO1.getId());
        assertThat(previsionDTO1).isEqualTo(previsionDTO2);
        previsionDTO2.setId(2L);
        assertThat(previsionDTO1).isNotEqualTo(previsionDTO2);
        previsionDTO1.setId(null);
        assertThat(previsionDTO1).isNotEqualTo(previsionDTO2);
    }
}
