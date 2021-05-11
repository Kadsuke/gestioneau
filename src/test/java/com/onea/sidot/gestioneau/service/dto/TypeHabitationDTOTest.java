package com.onea.sidot.gestioneau.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeHabitationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeHabitationDTO.class);
        TypeHabitationDTO typeHabitationDTO1 = new TypeHabitationDTO();
        typeHabitationDTO1.setId(1L);
        TypeHabitationDTO typeHabitationDTO2 = new TypeHabitationDTO();
        assertThat(typeHabitationDTO1).isNotEqualTo(typeHabitationDTO2);
        typeHabitationDTO2.setId(typeHabitationDTO1.getId());
        assertThat(typeHabitationDTO1).isEqualTo(typeHabitationDTO2);
        typeHabitationDTO2.setId(2L);
        assertThat(typeHabitationDTO1).isNotEqualTo(typeHabitationDTO2);
        typeHabitationDTO1.setId(null);
        assertThat(typeHabitationDTO1).isNotEqualTo(typeHabitationDTO2);
    }
}
