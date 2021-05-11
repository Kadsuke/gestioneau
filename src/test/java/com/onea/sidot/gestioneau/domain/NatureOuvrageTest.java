package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NatureOuvrageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NatureOuvrage.class);
        NatureOuvrage natureOuvrage1 = new NatureOuvrage();
        natureOuvrage1.setId(1L);
        NatureOuvrage natureOuvrage2 = new NatureOuvrage();
        natureOuvrage2.setId(natureOuvrage1.getId());
        assertThat(natureOuvrage1).isEqualTo(natureOuvrage2);
        natureOuvrage2.setId(2L);
        assertThat(natureOuvrage1).isNotEqualTo(natureOuvrage2);
        natureOuvrage1.setId(null);
        assertThat(natureOuvrage1).isNotEqualTo(natureOuvrage2);
    }
}
