package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest {
    private DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();
    private String ip = "192.168.0.3";
    private static final long THRESHOLD = 20;
    private static final long TIME = 60000;

    @Test
    public void testIsAllowedTrue() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(TIME);
        assertTrue(dosProtectionService.isAllowed(ip));
    }

    @Test
    public void testIsAllowedFalse() {
        for (int i = 0; i <= THRESHOLD; i++) {
            dosProtectionService.isAllowed(ip);
        }
        assertFalse(dosProtectionService.isAllowed(ip));
    }

    @Test
    public void testIsAllowedTrueTime() throws InterruptedException {
        for (int i = 0; i <= THRESHOLD; i++) {
            dosProtectionService.isAllowed(ip);
        }
        TimeUnit.MILLISECONDS.sleep(TIME);
        assertTrue(dosProtectionService.isAllowed(ip));
    }
}
