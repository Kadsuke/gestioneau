package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeCommuneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeCommuneDTO.class);
        TypeCommuneDTO typeCommuneDTO1 = new TypeCommuneDTO();
        typeCommuneDTO1.setId(1L);
        TypeCommuneDTO typeCommuneDTO2 = new TypeCommuneDTO();
        assertThat(typeCommuneDTO1).isNotEqualTo(typeCommuneDTO2);
        typeCommuneDTO2.setId(typeCommuneDTO1.getId());
        assertThat(typeCommuneDTO1).isEqualTo(typeCommuneDTO2);
        typeCommuneDTO2.setId(2L);
        assertThat(typeCommuneDTO1).isNotEqualTo(typeCommuneDTO2);
        typeCommuneDTO1.setId(null);
        assertThat(typeCommuneDTO1).isNotEqualTo(typeCommuneDTO2);
    }
}
