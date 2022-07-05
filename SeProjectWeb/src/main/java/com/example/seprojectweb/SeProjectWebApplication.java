package com.example.seprojectweb;

import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.AndCondition;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.IfThenCondition;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.OrCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.AtLeastProductQuantityCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.AtMostProductQuantityCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.TotalBasketPriceCondition;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.CategoryDiscount;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.SingleProductDiscount;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.TotalShopDiscount;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.XorDiscount;
import com.example.seprojectweb.Domain.Market.Notifications.BidNotification;
import com.example.seprojectweb.Domain.Market.Notifications.ShopNotification;
import com.example.seprojectweb.Domain.Market.PurchasePolicies.PurchasePolicy;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.Market.SystemData.DailySystemData;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@SpringBootApplication
public class SeProjectWebApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        PersistenceManager.setDBConnection("load");
        //cleanDB();
        SpringApplication.run(SeProjectWebApplication.class, args);
    }

    private static void removeAllInstances(final Class<?> clazz, CriteriaBuilder builder, Session session) {

        CriteriaQuery<?> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        List<?> data = session.createQuery(criteria).getResultList();
        for (Object obj : data) {
            session.delete(obj);
        }
    }

    private static void cleanDB() {
        String connection = "default";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(connection);
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        Transaction transaction = session.getTransaction();
        transaction.begin();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            removeAllInstances(AndCondition.class, builder, session);
            removeAllInstances(AssignAgreement.class, builder, session);
            removeAllInstances(AtLeastProductQuantityCondition.class,builder, session);
            removeAllInstances(AtMostProductQuantityCondition.class,builder, session);
            removeAllInstances(SingleProductDiscount.class,builder, session);
            removeAllInstances(Bid.class,builder, session);
            removeAllInstances(BidNotification.class,builder, session);
            removeAllInstances(ShopNotification.class,builder, session);
            removeAllInstances(CategoryDiscount.class,builder, session);
            removeAllInstances(DailySystemData.class,builder, session);
            removeAllInstances(IfThenCondition.class,builder, session);
            removeAllInstances(OrCondition.class,builder, session);
            removeAllInstances(PurchaseHistory.class,builder, session);
            removeAllInstances(Member.class,builder, session);
            removeAllInstances(PurchasePolicy.class,builder, session);
            removeAllInstances(Shop.class,builder, session);
            removeAllInstances(ShopManager.class,builder, session);
            removeAllInstances(ShopOwner.class,builder, session);
            removeAllInstances(ShoppingBasket.class,builder, session);
            removeAllInstances(ShoppingCart.class,builder, session);
            removeAllInstances(Product.class,builder, session);
            removeAllInstances(TotalBasketPriceCondition.class,builder, session);
            removeAllInstances(TotalShopDiscount.class,builder, session);
            removeAllInstances(XorDiscount.class,builder, session);
            transaction.commit();
        }
        catch (Exception e){
            throw new IllegalAccessError("FAILED CLEAR DB\n"+e.getMessage());
        }
        finally {
            session.close();
        }
    }

}


