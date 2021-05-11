package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnneeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annee.class);
        Annee annee1 = new Annee();
        annee1.setId(1L);
        Annee annee2 = new Annee();
        annee2.setId(annee1.getId());
        assertThat(annee1).isEqualTo(annee2);
        annee2.setId(2L);
        assertThat(annee1).isNotEqualTo(annee2);
        annee1.setId(null);
        assertThat(annee1).isNotEqualTo(annee2);
    }
}
