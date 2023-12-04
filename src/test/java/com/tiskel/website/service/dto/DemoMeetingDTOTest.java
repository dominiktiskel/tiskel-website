package com.tiskel.website.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.website.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemoMeetingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemoMeetingDTO.class);
        DemoMeetingDTO demoMeetingDTO1 = new DemoMeetingDTO();
        demoMeetingDTO1.setId(1L);
        DemoMeetingDTO demoMeetingDTO2 = new DemoMeetingDTO();
        assertThat(demoMeetingDTO1).isNotEqualTo(demoMeetingDTO2);
        demoMeetingDTO2.setId(demoMeetingDTO1.getId());
        assertThat(demoMeetingDTO1).isEqualTo(demoMeetingDTO2);
        demoMeetingDTO2.setId(2L);
        assertThat(demoMeetingDTO1).isNotEqualTo(demoMeetingDTO2);
        demoMeetingDTO1.setId(null);
        assertThat(demoMeetingDTO1).isNotEqualTo(demoMeetingDTO2);
    }
}
