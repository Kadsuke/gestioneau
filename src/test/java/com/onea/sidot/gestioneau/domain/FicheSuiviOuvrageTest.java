package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FicheSuiviOuvrageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FicheSuiviOuvrage.class);
        FicheSuiviOuvrage ficheSuiviOuvrage1 = new FicheSuiviOuvrage();
        ficheSuiviOuvrage1.setId(1L);
        FicheSuiviOuvrage ficheSuiviOuvrage2 = new FicheSuiviOuvrage();
        ficheSuiviOuvrage2.setId(ficheSuiviOuvrage1.getId());
        assertThat(ficheSuiviOuvrage1).isEqualTo(ficheSuiviOuvrage2);
        ficheSuiviOuvrage2.setId(2L);
        assertThat(ficheSuiviOuvrage1).isNotEqualTo(ficheSuiviOuvrage2);
        ficheSuiviOuvrage1.setId(null);
        assertThat(ficheSuiviOuvrage1).isNotEqualTo(ficheSuiviOuvrage2);
    }
}
