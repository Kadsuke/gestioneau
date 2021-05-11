package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModeEvacExcretaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeEvacExcreta.class);
        ModeEvacExcreta modeEvacExcreta1 = new ModeEvacExcreta();
        modeEvacExcreta1.setId(1L);
        ModeEvacExcreta modeEvacExcreta2 = new ModeEvacExcreta();
        modeEvacExcreta2.setId(modeEvacExcreta1.getId());
        assertThat(modeEvacExcreta1).isEqualTo(modeEvacExcreta2);
        modeEvacExcreta2.setId(2L);
        assertThat(modeEvacExcreta1).isNotEqualTo(modeEvacExcreta2);
        modeEvacExcreta1.setId(null);
        assertThat(modeEvacExcreta1).isNotEqualTo(modeEvacExcreta2);
    }
}
