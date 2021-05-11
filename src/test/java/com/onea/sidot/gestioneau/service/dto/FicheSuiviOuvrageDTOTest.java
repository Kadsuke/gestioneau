package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FicheSuiviOuvrageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FicheSuiviOuvrageDTO.class);
        FicheSuiviOuvrageDTO ficheSuiviOuvrageDTO1 = new FicheSuiviOuvrageDTO();
        ficheSuiviOuvrageDTO1.setId(1L);
        FicheSuiviOuvrageDTO ficheSuiviOuvrageDTO2 = new FicheSuiviOuvrageDTO();
        assertThat(ficheSuiviOuvrageDTO1).isNotEqualTo(ficheSuiviOuvrageDTO2);
        ficheSuiviOuvrageDTO2.setId(ficheSuiviOuvrageDTO1.getId());
        assertThat(ficheSuiviOuvrageDTO1).isEqualTo(ficheSuiviOuvrageDTO2);
        ficheSuiviOuvrageDTO2.setId(2L);
        assertThat(ficheSuiviOuvrageDTO1).isNotEqualTo(ficheSuiviOuvrageDTO2);
        ficheSuiviOuvrageDTO1.setId(null);
        assertThat(ficheSuiviOuvrageDTO1).isNotEqualTo(ficheSuiviOuvrageDTO2);
    }
}
