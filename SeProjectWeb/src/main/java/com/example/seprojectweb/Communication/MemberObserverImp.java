package com.example.seprojectweb.Communication;

import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;

import java.util.LinkedList;
import java.util.List;

public class MemberObserverImp implements IMemberObserver {
    private List<Notification> notifications;

    public MemberObserverImp(){
        notifications = new LinkedList<>();
    }

    public List<Notification> getNotifications(){
        return notifications;
    }

    @Override
    public void sendRealTimeNotification(Notification notification, String ... memberName) {
        notifications.add(notification);
    }

    @Override
    public void sendDataUpdateNotice(String... adminUserName) {
        //TODO: implement method
    }
}
