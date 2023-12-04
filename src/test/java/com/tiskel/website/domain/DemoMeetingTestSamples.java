package com.tiskel.website.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DemoMeetingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DemoMeeting getDemoMeetingSample1() {
        return new DemoMeeting().id(1L).email("email1");
    }

    public static DemoMeeting getDemoMeetingSample2() {
        return new DemoMeeting().id(2L).email("email2");
    }

    public static DemoMeeting getDemoMeetingRandomSampleGenerator() {
        return new DemoMeeting().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString());
    }
}
