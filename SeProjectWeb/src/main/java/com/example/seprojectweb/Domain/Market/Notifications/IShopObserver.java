package com.example.seprojectweb.Domain.Market.Notifications;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;

public interface IShopObserver {

    void sendNotification(Notification notification);

    String getUsername();
    void sendNotificationWithConnection(Notification notification, DBConnection dbConnection) throws InnerLogicException;


}
