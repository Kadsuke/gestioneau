package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrefabricantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrefabricantDTO.class);
        PrefabricantDTO prefabricantDTO1 = new PrefabricantDTO();
        prefabricantDTO1.setId(1L);
        PrefabricantDTO prefabricantDTO2 = new PrefabricantDTO();
        assertThat(prefabricantDTO1).isNotEqualTo(prefabricantDTO2);
        prefabricantDTO2.setId(prefabricantDTO1.getId());
        assertThat(prefabricantDTO1).isEqualTo(prefabricantDTO2);
        prefabricantDTO2.setId(2L);
        assertThat(prefabricantDTO1).isNotEqualTo(prefabricantDTO2);
        prefabricantDTO1.setId(null);
        assertThat(prefabricantDTO1).isNotEqualTo(prefabricantDTO2);
    }
}
