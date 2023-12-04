package com.tiskel.website.domain;

import static com.tiskel.website.domain.SalesLeadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.website.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesLeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesLead.class);
        SalesLead salesLead1 = getSalesLeadSample1();
        SalesLead salesLead2 = new SalesLead();
        assertThat(salesLead1).isNotEqualTo(salesLead2);

        salesLead2.setId(salesLead1.getId());
        assertThat(salesLead1).isEqualTo(salesLead2);

        salesLead2 = getSalesLeadSample2();
        assertThat(salesLead1).isNotEqualTo(salesLead2);
    }
}
