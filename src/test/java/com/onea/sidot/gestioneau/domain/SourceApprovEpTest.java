package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SourceApprovEpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceApprovEp.class);
        SourceApprovEp sourceApprovEp1 = new SourceApprovEp();
        sourceApprovEp1.setId(1L);
        SourceApprovEp sourceApprovEp2 = new SourceApprovEp();
        sourceApprovEp2.setId(sourceApprovEp1.getId());
        assertThat(sourceApprovEp1).isEqualTo(sourceApprovEp2);
        sourceApprovEp2.setId(2L);
        assertThat(sourceApprovEp1).isNotEqualTo(sourceApprovEp2);
        sourceApprovEp1.setId(null);
        assertThat(sourceApprovEp1).isNotEqualTo(sourceApprovEp2);
    }
}
