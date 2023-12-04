package com.tiskel.website.domain;

import static com.tiskel.website.domain.DemoMeetingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.website.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemoMeetingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemoMeeting.class);
        DemoMeeting demoMeeting1 = getDemoMeetingSample1();
        DemoMeeting demoMeeting2 = new DemoMeeting();
        assertThat(demoMeeting1).isNotEqualTo(demoMeeting2);

        demoMeeting2.setId(demoMeeting1.getId());
        assertThat(demoMeeting1).isEqualTo(demoMeeting2);

        demoMeeting2 = getDemoMeetingSample2();
        assertThat(demoMeeting1).isNotEqualTo(demoMeeting2);
    }
}
