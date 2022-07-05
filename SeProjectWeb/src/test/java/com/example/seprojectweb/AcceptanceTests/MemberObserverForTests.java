package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;

import java.util.LinkedList;
import java.util.List;

public class MemberObserverForTests implements IMemberObserver {
    private List<Notification> notifications;
    private int dataNoticeCounter;

    public MemberObserverForTests(){
        notifications = new LinkedList<>();
        dataNoticeCounter = 0;
    }
    @Override
    public void sendRealTimeNotification(Notification notification, String ... memberName) {
        notifications.add(notification);
    }

    @Override
    public void sendDataUpdateNotice(String... adminUserName) {
        dataNoticeCounter++;
    }

    public List<Notification> getNotifications(){
        return notifications;
    }
    public int getDataNoticeCounter(){
        return dataNoticeCounter;
    }
}
