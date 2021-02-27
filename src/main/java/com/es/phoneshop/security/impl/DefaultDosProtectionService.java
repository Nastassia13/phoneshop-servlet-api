package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static DefaultDosProtectionService instance;
    private static final long THRESHOLD = 20;
    private static final long TIME = 60000;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private long previousTime;

    private DefaultDosProtectionService() {
        previousTime = System.currentTimeMillis();
    }

    public static synchronized DefaultDosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - previousTime > TIME) {
            countMap.clear();
            previousTime = currentTime;
        }
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
