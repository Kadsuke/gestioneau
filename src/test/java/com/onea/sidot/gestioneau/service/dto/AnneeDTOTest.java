package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnneeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeDTO.class);
        AnneeDTO anneeDTO1 = new AnneeDTO();
        anneeDTO1.setId(1L);
        AnneeDTO anneeDTO2 = new AnneeDTO();
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
        anneeDTO2.setId(anneeDTO1.getId());
        assertThat(anneeDTO1).isEqualTo(anneeDTO2);
        anneeDTO2.setId(2L);
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
        anneeDTO1.setId(null);
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
    }
}
