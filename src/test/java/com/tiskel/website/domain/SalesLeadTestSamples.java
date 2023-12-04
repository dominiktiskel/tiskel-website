package com.tiskel.website.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SalesLeadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SalesLead getSalesLeadSample1() {
        return new SalesLead().id(1L).phoneNumber("phoneNumber1").email("email1").note("note1");
    }

    public static SalesLead getSalesLeadSample2() {
        return new SalesLead().id(2L).phoneNumber("phoneNumber2").email("email2").note("note2");
    }

    public static SalesLead getSalesLeadRandomSampleGenerator() {
        return new SalesLead()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString());
    }
}
