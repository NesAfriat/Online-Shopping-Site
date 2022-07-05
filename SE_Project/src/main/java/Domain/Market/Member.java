package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Notifications.IMemberObserver;
import Domain.Market.Notifications.IShopObserver;
import Domain.Market.Notifications.Notification;
import Domain.Market.Notifications.ShopNotification;
import Domain.Security.SecurityRepresentative;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Member implements IShopObserver {
    private final String username;
    private String hashedPassword;
    private String Email;
    private String phoneNumber;

    private AtomicBoolean isLoggedIn;

    private IMemberObserver memberObserver;
    private ConcurrentLinkedQueue<Notification> notifications;


    public Member(String username, String password){
        this.username = username;
        this.hashedPassword = SecurityRepresentative.encryptPassword(password);
        this.isLoggedIn = new AtomicBoolean(false);
        this.notifications = new ConcurrentLinkedQueue<Notification>();
    }

    public String getUsername() {
        return username;
    }

    public boolean verifyPassword(String password){
        return hashedPassword.equals(SecurityRepresentative.encryptPassword(password));
    }

    public void login(IMemberObserver observer) throws InnerLogicException {
        if(isLoggedIn.getAndSet(true)){
            throw new InnerLogicException("tried to login to " + username + " but the member is already logged in");
        }
        memberObserver = observer;
        for (Notification notification: notifications) {
            memberObserver.sendRealTimeNotification(notification);
        }
    }
    public boolean isLoggedIn(){
        return isLoggedIn.get();
    }
    public void logout() throws InnerLogicException {
        if(!isLoggedIn.getAndSet(false)){
            throw new InnerLogicException("tried to logout from " + username + " but the member was not logged in");
        }
        memberObserver = null;
    }

    @Override
    public void sendNotification(ShopNotification shopNotification) {
        this.notifications.add(shopNotification);
        if(memberObserver != null){
            memberObserver.sendRealTimeNotification(shopNotification);
        }
    }
}
