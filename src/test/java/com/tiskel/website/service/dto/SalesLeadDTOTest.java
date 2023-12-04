package com.tiskel.website.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.website.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesLeadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesLeadDTO.class);
        SalesLeadDTO salesLeadDTO1 = new SalesLeadDTO();
        salesLeadDTO1.setId(1L);
        SalesLeadDTO salesLeadDTO2 = new SalesLeadDTO();
        assertThat(salesLeadDTO1).isNotEqualTo(salesLeadDTO2);
        salesLeadDTO2.setId(salesLeadDTO1.getId());
        assertThat(salesLeadDTO1).isEqualTo(salesLeadDTO2);
        salesLeadDTO2.setId(2L);
        assertThat(salesLeadDTO1).isNotEqualTo(salesLeadDTO2);
        salesLeadDTO1.setId(null);
        assertThat(salesLeadDTO1).isNotEqualTo(salesLeadDTO2);
    }
}
