package com.example.seprojectweb.Domain.Market.SystemData;

import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.PersistenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class SystemDataLogger {
    static Map<String, DailySystemData> log;
    static AtomicInteger loadBalance;
    static final int SAVE_LIMIT = 50;
    public SystemDataLogger(){
        log = new ConcurrentHashMap<>();
        loadBalance = new AtomicInteger(0);
        loadData();
    }

    public void updateVisitEvent(){
        String toDay = toDayDateString();
        boolean isNewDay = !log.containsKey(toDay);
        log.putIfAbsent(toDay, new DailySystemData(toDay));
        DailySystemData data =log.get(toDay);
        data.incrementVisitorCount();

        if(isNewDay) PersistenceManager.persist(data);
        else if (loadBalance.incrementAndGet() >= SAVE_LIMIT){
            data.updateCounters();
            loadBalance.set(0);
            PersistenceManager.update(data);
        }

        noticeAdmins();


    }

    public void updateLoginEvent(String loggedUserName, boolean isAdmin){
        String toDay = toDayDateString();
        boolean isNewDay = !log.containsKey(toDay);
        log.putIfAbsent(toDay, new DailySystemData(toDay));
        DailySystemData data = log.get(toDay);


        if(isAdmin){
            data.incrementAdminCount();
        }
        List<Role> roles = RoleController.getInstance().getMemberRoles(loggedUserName);

        boolean isShopOwner = false;
        boolean isShopManager = false;

        for(Role role: roles){
            if(role instanceof ShopOwner) isShopOwner = true;
            if(role instanceof ShopManager) isShopManager = true;
        }

        if(isShopOwner){
            data.incrementShopOwnerCount();
        } else if (isShopManager) {
            data.incrementShopManagersCount();
        }else {
            data.incrementRoleLessMembersCount();
        }
        if(isNewDay) {
            PersistenceManager.persist(data);
        }
        else if (loadBalance.incrementAndGet() >= SAVE_LIMIT){
            data.updateCounters();
            loadBalance.set(0);
            PersistenceManager.update(data);
        }
        noticeAdmins();


    }

    public List<DailySystemData> getSystemData(String startDate, String endDate) {
        List<DailySystemData> output = new LinkedList<>();
        for(Map.Entry<String, DailySystemData> entry: log.entrySet()){
            if(isDateInRange(entry.getValue().getDate(), startDate, endDate)){
                output.add(entry.getValue());
            }
        }
        return output;
    }

    private void noticeAdmins() {
        MarketRepresentative.getInstance().noticeAdmins();
    }

    private String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    private String toDayDateString(){
        return dateToString(new Date());
    }

    private boolean formatDateGreaterEqualThan(String operand1, String operand2){
        int year1 = StringToInt(operand1.substring(6,10));
        int year2 = StringToInt(operand2.substring(6,10));

        if(year1>=year2) return true;

        int month1 = StringToInt(operand1.substring(3,5));
        int month2 = StringToInt(operand2.substring(3,5));

        if(month1>=month2) return true;

        int day1 = StringToInt(operand1.substring(0,2));
        int day2 = StringToInt(operand2.substring(0,2));

        if(day1>=day2) return true;

        return false;
    }

    private boolean isDateInRange(String testDate, String startDate, String endDate){
        return formatDateGreaterEqualThan(endDate, testDate) && formatDateGreaterEqualThan(testDate, startDate);
    }


    private int StringToInt(String str){
        return Integer.parseInt(str);
    }

    private void loadData(){
        for(Object o: PersistenceManager.findAll(DailySystemData.class)){
            DailySystemData d = (DailySystemData) o;
            log.putIfAbsent(d.getDate(), d);
        }
    }


}
