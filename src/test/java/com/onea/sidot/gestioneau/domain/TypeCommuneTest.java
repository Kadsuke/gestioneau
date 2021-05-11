package com.onea.sidot.gestioneau.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.onea.sidot.gestioneau.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeCommuneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeCommune.class);
        TypeCommune typeCommune1 = new TypeCommune();
        typeCommune1.setId(1L);
        TypeCommune typeCommune2 = new TypeCommune();
        typeCommune2.setId(typeCommune1.getId());
        assertThat(typeCommune1).isEqualTo(typeCommune2);
        typeCommune2.setId(2L);
        assertThat(typeCommune1).isNotEqualTo(typeCommune2);
        typeCommune1.setId(null);
        assertThat(typeCommune1).isNotEqualTo(typeCommune2);
    }
}
