package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirectionRegionaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectionRegionale.class);
        DirectionRegionale directionRegionale1 = new DirectionRegionale();
        directionRegionale1.setId(1L);
        DirectionRegionale directionRegionale2 = new DirectionRegionale();
        directionRegionale2.setId(directionRegionale1.getId());
        assertThat(directionRegionale1).isEqualTo(directionRegionale2);
        directionRegionale2.setId(2L);
        assertThat(directionRegionale1).isNotEqualTo(directionRegionale2);
        directionRegionale1.setId(null);
        assertThat(directionRegionale1).isNotEqualTo(directionRegionale2);
    }
}
