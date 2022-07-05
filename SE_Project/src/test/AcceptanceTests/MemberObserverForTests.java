package AcceptanceTests;

import Domain.Market.Notifications.IMemberObserver;
import Domain.Market.Notifications.Notification;

import java.util.LinkedList;
import java.util.List;

public class MemberObserverForTests implements IMemberObserver {
    private List<Notification> notifications;

    public MemberObserverForTests(){
        notifications = new LinkedList<>();
    }
    @Override
    public void sendRealTimeNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getNotifications(){
        return notifications;
    }
}
