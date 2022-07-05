package com.example.seprojectweb.InitData;

import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;

public class InitDataMemberObserver implements IMemberObserver {
    @Override
    public void sendRealTimeNotification(Notification notification, String ... memberName) {
    }

    @Override
    public void sendDataUpdateNotice(String... adminUserName) {

    }
}
