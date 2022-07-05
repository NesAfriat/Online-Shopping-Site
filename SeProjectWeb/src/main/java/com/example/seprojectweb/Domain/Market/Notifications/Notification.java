package com.example.seprojectweb.Domain.Market.Notifications;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Notification {
    private String message;
    private Date createdAt;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;


    protected Notification(String message) {
        this.message = message;
        this.createdAt = new Date();
    }

    public Notification() {

    }

    public abstract String toString();


    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }
}
