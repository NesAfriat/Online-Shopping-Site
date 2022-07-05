package com.example.seprojectweb.UnitTests;

import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.jupiter.api.BeforeAll;

import java.util.LinkedList;
import java.util.List;

public class MemberObserverForTests implements IMemberObserver {
    private List<Notification> notifications;
    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }

    public MemberObserverForTests(){
        notifications = new LinkedList<>();
    }
    @Override
    public void sendRealTimeNotification(Notification notification, String ... memberName) {
        notifications.add(notification);
    }

    @Override
    public void sendDataUpdateNotice(String... adminUserName) {

    }

    public List<Notification> getNotifications(){
        return notifications;
    }
}
