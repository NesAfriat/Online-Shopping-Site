package com.example.seprojectweb.Domain.Market;

import java.util.Map;
import java.util.Set;

public class VisitorSessionHandler implements Runnable {

    public static final int SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes
    public static final int INTERVAL_TIME = 10 * 60 * 1000; // 10 minute
    private final Map<Integer, Visitor> visitors;

    public VisitorSessionHandler(Map<Integer, Visitor> visitors) {
        this.visitors = visitors;
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            Set<Map.Entry<Integer, Visitor>> entrySet = visitors.entrySet();
            for (Map.Entry<Integer, Visitor> visitorEntry : entrySet) {
                long currentTime = System.currentTimeMillis();
                Visitor visitor = visitorEntry.getValue();
                if (currentTime - visitor.getLastActivityTime() > SESSION_TIMEOUT)
                    visitors.remove(visitorEntry.getKey());
            }
            try {
                Thread.sleep(INTERVAL_TIME);
            } catch (InterruptedException e) {
                run = false;
            }
        }
    }
}
