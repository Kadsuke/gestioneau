package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectionDTO.class);
        SectionDTO sectionDTO1 = new SectionDTO();
        sectionDTO1.setId(1L);
        SectionDTO sectionDTO2 = new SectionDTO();
        assertThat(sectionDTO1).isNotEqualTo(sectionDTO2);
        sectionDTO2.setId(sectionDTO1.getId());
        assertThat(sectionDTO1).isEqualTo(sectionDTO2);
        sectionDTO2.setId(2L);
        assertThat(sectionDTO1).isNotEqualTo(sectionDTO2);
        sectionDTO1.setId(null);
        assertThat(sectionDTO1).isNotEqualTo(sectionDTO2);
    }
}
