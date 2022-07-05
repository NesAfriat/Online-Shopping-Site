package com.example.seprojectweb.Communication;

import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController implements IMemberObserver {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void sendRealTimeNotification(@Payload Notification notification, String ... memberName) {
//        try{
//            Thread.sleep(250);
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("sent to "+memberName[0]);
        simpMessagingTemplate.convertAndSend("/user/queue/specific/"+memberName[0], notification.getMessage()+"\nSent at: "+notification.getCreatedAt().toString());

    }

    @Override
    public void sendDataUpdateNotice(String... adminUserName) {
        System.out.println("sent update to "+adminUserName[0]);
        simpMessagingTemplate.convertAndSend("/user/queue/admins/"+adminUserName[0], "An update to daily data has occurred");
    }
}



