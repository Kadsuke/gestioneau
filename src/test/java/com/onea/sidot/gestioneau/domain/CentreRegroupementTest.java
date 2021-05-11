package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreRegroupementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreRegroupement.class);
        CentreRegroupement centreRegroupement1 = new CentreRegroupement();
        centreRegroupement1.setId(1L);
        CentreRegroupement centreRegroupement2 = new CentreRegroupement();
        centreRegroupement2.setId(centreRegroupement1.getId());
        assertThat(centreRegroupement1).isEqualTo(centreRegroupement2);
        centreRegroupement2.setId(2L);
        assertThat(centreRegroupement1).isNotEqualTo(centreRegroupement2);
        centreRegroupement1.setId(null);
        assertThat(centreRegroupement1).isNotEqualTo(centreRegroupement2);
    }
}
