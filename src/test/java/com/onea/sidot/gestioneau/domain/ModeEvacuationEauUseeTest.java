package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModeEvacuationEauUseeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeEvacuationEauUsee.class);
        ModeEvacuationEauUsee modeEvacuationEauUsee1 = new ModeEvacuationEauUsee();
        modeEvacuationEauUsee1.setId(1L);
        ModeEvacuationEauUsee modeEvacuationEauUsee2 = new ModeEvacuationEauUsee();
        modeEvacuationEauUsee2.setId(modeEvacuationEauUsee1.getId());
        assertThat(modeEvacuationEauUsee1).isEqualTo(modeEvacuationEauUsee2);
        modeEvacuationEauUsee2.setId(2L);
        assertThat(modeEvacuationEauUsee1).isNotEqualTo(modeEvacuationEauUsee2);
        modeEvacuationEauUsee1.setId(null);
        assertThat(modeEvacuationEauUsee1).isNotEqualTo(modeEvacuationEauUsee2);
    }
}
