package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProvinceDTO.class);
        ProvinceDTO provinceDTO1 = new ProvinceDTO();
        provinceDTO1.setId(1L);
        ProvinceDTO provinceDTO2 = new ProvinceDTO();
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO2.setId(provinceDTO1.getId());
        assertThat(provinceDTO1).isEqualTo(provinceDTO2);
        provinceDTO2.setId(2L);
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO1.setId(null);
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
    }
}
