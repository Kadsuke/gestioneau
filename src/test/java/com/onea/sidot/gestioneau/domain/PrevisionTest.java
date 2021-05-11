package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrevisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prevision.class);
        Prevision prevision1 = new Prevision();
        prevision1.setId(1L);
        Prevision prevision2 = new Prevision();
        prevision2.setId(prevision1.getId());
        assertThat(prevision1).isEqualTo(prevision2);
        prevision2.setId(2L);
        assertThat(prevision1).isNotEqualTo(prevision2);
        prevision1.setId(null);
        assertThat(prevision1).isNotEqualTo(prevision2);
    }
}
