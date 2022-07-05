package com.example.seprojectweb.Domain.Market.SystemData;


import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@NamedQuery(name="DailySystemData.findAll", query="SELECT m FROM DailySystemData m")
public class DailySystemData {

    @Id
    @Column(nullable = false)
    private String date;
    private int visitorCount;
    private int roleLessMembersCount;
    private int shopManagersCount;
    private int shopOwnerCount;
    private int adminCount;

    @Transient
    private AtomicInteger visitorCountAtmoic;
    @Transient
    private AtomicInteger roleLessMembersCountAtmoic;
    @Transient
    private AtomicInteger shopManagersCountAtmoic;
    @Transient
    private AtomicInteger shopOwnerCountAtmoic;
    @Transient
    private AtomicInteger adminCountAtmoic;

    @Transient
    private boolean visitorCountLock;
    @Transient
    private boolean roleLessMembersCountLock;
    @Transient
    private boolean shopManagersCountLock;
    @Transient
    private boolean shopOwnerCountLock;
    @Transient
    private boolean adminCountLock;

    public DailySystemData(String date) {
        this.date = date;
        this.visitorCount = 0;
        this.roleLessMembersCount = 0;
        this.shopManagersCount = 0;
        this.shopOwnerCount = 0;
        this.adminCount = 0;
        this.visitorCountLock = false;
        this.roleLessMembersCountLock = false;
        this.shopManagersCountLock = false;
        this.shopOwnerCountLock = false;
        this.adminCountLock = false;

        this.visitorCountAtmoic = new AtomicInteger(0);
        this.roleLessMembersCountAtmoic = new AtomicInteger(0);
        this.shopManagersCountAtmoic = new AtomicInteger(0);
        this.shopOwnerCountAtmoic = new AtomicInteger(0);
        this.adminCountAtmoic = new AtomicInteger(0);
    }

    public DailySystemData() {
        this.visitorCountLock = false;
        this.roleLessMembersCountLock = false;
        this.shopManagersCountLock = false;
        this.shopOwnerCountLock = false;
        this.adminCountLock = false;

        this.visitorCountAtmoic = new AtomicInteger(0);
        this.roleLessMembersCountAtmoic = new AtomicInteger(0);
        this.shopManagersCountAtmoic = new AtomicInteger(0);
        this.shopOwnerCountAtmoic = new AtomicInteger(0);
        this.adminCountAtmoic = new AtomicInteger(0);
    }


    public void incrementVisitorCount(){
        visitorCountAtmoic.getAndIncrement();
    }
    public void incrementRoleLessMembersCount(){
        roleLessMembersCountAtmoic.getAndIncrement();
    }
    public void incrementShopManagersCount(){
        shopManagersCountAtmoic.getAndIncrement();
    }
    public void incrementShopOwnerCount(){
        shopOwnerCountAtmoic.getAndIncrement();
    }
    public void incrementAdminCount(){
        adminCountAtmoic.getAndIncrement();
    }
//
//    public void incrementVisitorCount(){
//        acquireVisitor();
//        visitorCount++;
//        releaseVisitor();
//    }
//    public void incrementRoleLessMembersCount(){
//        acquireRoleLess();
//        roleLessMembersCount++;
//        releaseRoleLess();
//    }
//    public void incrementShopManagersCount(){
//        acquireManager();
//        shopManagersCount++;
//        releaseManager();
//    }
//    public void incrementShopOwnerCount(){
//        acquireOwner();
//        shopOwnerCount++;
//        releaseOwner();
//    }
//    public void incrementAdminCount(){
//        acquireAdmin();
//        adminCount++;
//        releaseAdmin();
//    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVisitorCount() {
        return visitorCountAtmoic.get();
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
        this.visitorCountAtmoic = new AtomicInteger(visitorCount);
    }

    public int getRoleLessMembersCount() {
        return roleLessMembersCountAtmoic.get();
    }

    public void setRoleLessMembersCount(int roleLessMembersCount) {
        this.roleLessMembersCount = roleLessMembersCount;
        this.roleLessMembersCountAtmoic = new AtomicInteger(roleLessMembersCount);
    }

    public int getShopManagersCount() {
        return shopManagersCountAtmoic.get();
    }

    public void setShopManagersCount(int shopManagersCount) {
        this.shopManagersCount = shopManagersCount;
        this.shopManagersCountAtmoic = new AtomicInteger(shopManagersCount);
    }

    public int getShopOwnerCount() {
        return shopOwnerCountAtmoic.get();
    }

    public void setShopOwnerCount(int shopOwnerCount) {
        this.shopOwnerCount = shopOwnerCount;
        this.shopOwnerCountAtmoic = new AtomicInteger(shopOwnerCount);
    }

    public int getAdminCount() {
        return adminCountAtmoic.get();
    }

    public void setAdminCount(int adminCount) {
        this.adminCount = adminCount;
        this.adminCountAtmoic = new AtomicInteger(adminCount);
    }




    private synchronized void acquireVisitor() {
        while (visitorCountLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        visitorCountLock = true;
    }

    private synchronized void releaseVisitor() {
        visitorCountLock = false;
        notifyAll();
    }

    private synchronized void acquireRoleLess() {
        while (roleLessMembersCountLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        roleLessMembersCountLock = true;
    }

    private synchronized void releaseRoleLess() {
        roleLessMembersCountLock = false;
        notifyAll();
    }

    private synchronized void acquireManager() {
        while (shopManagersCountLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        shopManagersCountLock = true;
    }

    private synchronized void releaseManager() {
        shopManagersCountLock = false;
        notifyAll();
    }

    private synchronized void acquireOwner() {
        while (shopOwnerCountLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        shopOwnerCountLock = true;
    }

    private synchronized void releaseOwner() {
        shopOwnerCountLock = false;
        notifyAll();
    }

    private synchronized void acquireAdmin() {
        while (adminCountLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        adminCountLock = true;
    }

    private synchronized void releaseAdmin() {
        adminCountLock = false;
        notifyAll();
    }

    public void updateCounters() {
        this.visitorCount = visitorCountAtmoic.get();
        this.adminCount = adminCountAtmoic.get();
        this.roleLessMembersCount = roleLessMembersCountAtmoic.get();
        this.shopManagersCount = shopManagersCountAtmoic.get();
        this.shopOwnerCount = shopOwnerCountAtmoic.get();
    }
}
