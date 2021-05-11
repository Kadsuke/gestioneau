package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParcelleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParcelleDTO.class);
        ParcelleDTO parcelleDTO1 = new ParcelleDTO();
        parcelleDTO1.setId(1L);
        ParcelleDTO parcelleDTO2 = new ParcelleDTO();
        assertThat(parcelleDTO1).isNotEqualTo(parcelleDTO2);
        parcelleDTO2.setId(parcelleDTO1.getId());
        assertThat(parcelleDTO1).isEqualTo(parcelleDTO2);
        parcelleDTO2.setId(2L);
        assertThat(parcelleDTO1).isNotEqualTo(parcelleDTO2);
        parcelleDTO1.setId(null);
        assertThat(parcelleDTO1).isNotEqualTo(parcelleDTO2);
    }
}
