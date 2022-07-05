package Domain.Market.Notifications;

import java.util.Date;

public abstract class Notification {
    protected final String message;
    protected final Date createdAt;

    protected Notification(String message) {
        this.message = message;
        this.createdAt =  new Date();
    }

    public abstract String toString();


    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
