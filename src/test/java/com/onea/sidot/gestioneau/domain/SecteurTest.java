package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SecteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Secteur.class);
        Secteur secteur1 = new Secteur();
        secteur1.setId(1L);
        Secteur secteur2 = new Secteur();
        secteur2.setId(secteur1.getId());
        assertThat(secteur1).isEqualTo(secteur2);
        secteur2.setId(2L);
        assertThat(secteur1).isNotEqualTo(secteur2);
        secteur1.setId(null);
        assertThat(secteur1).isNotEqualTo(secteur2);
    }
}
