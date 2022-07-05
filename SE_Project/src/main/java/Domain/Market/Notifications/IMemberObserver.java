package Domain.Market.Notifications;

public interface IMemberObserver {

    /***
     * this function must support thread safe!
     * @param notification
     */
    void sendRealTimeNotification(Notification notification);

}
