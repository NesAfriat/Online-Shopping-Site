package com.example.seprojectweb.Domain;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.security.PublicKey;
import java.util.List;

public class PersistenceManager {
    public static String connection = "default";
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(connection);
    public static EntityManagerService entityManagerService = new EntityManagerService();


    public static void setDBConnection(String newConnection) {
        try {
            connection = newConnection;
            if (entityManagerFactory != null){
              entityManagerFactory.close();
            }
            entityManagerFactory = Persistence.createEntityManagerFactory(connection);
        }catch (Exception e){
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }
    public static EntityManagerFactory getEntityManagerFactory(){
        return entityManagerFactory;
    }
    public static void setEntityManagerService(EntityManagerService newEntityManagerService){
        entityManagerService = newEntityManagerService;
    }
    public static String getConnectionString(){
        return connection;
    }


    public static void persist(Object m)  {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(m);
            transaction.commit();
        }catch (Exception e){
            rollBackAndClose(transaction,entityManager);
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }

    public static void remove(Object m){
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.remove(entityManager.contains(m) ? m : entityManager.merge(m));
            transaction.commit();
        } catch (Exception e){
            rollBackAndClose(transaction,entityManager);
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }


    public static void update(Object m){
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(m);
            transaction.commit();
        }
        catch (Exception e){
            rollBackAndClose(transaction,entityManager);
            throw new IllegalAccessError("server on maintenance please try again later");
        }

    }


    public static void updateSession(Object m) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        Session session = null;
        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);
            session = entityManager.unwrap(Session.class);
            transaction = session.getTransaction();
            transaction.begin();
            session.update(m);
            transaction.commit();
        }catch (Exception e){
            rollBackAndClose(transaction,entityManager);
            if (session != null){
                session.close();
            }
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }


    public static Object find(Class<?> toFindClass, Object key) {
        EntityManager entityManager = null;
        Object result = null;
        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);

            result = entityManager.find(toFindClass, key);
        }
        catch (Exception e){
            if(entityManager != null){
                entityManager.close();
            }
            throw new IllegalAccessError("server on maintenance please try again later");
        }

        return result;
    }

    public static List findAll(Class<?> toFindClass) {
        EntityManager entityManager = null;
        List result = null;
        String[] subNames = toFindClass.getName().split("\\.");
        String className = subNames[subNames.length-1];
        String query = className + ".findAll";
        try{
            entityManager = entityManagerService.createEntityManager(entityManagerFactory);
            result =  entityManager.createNamedQuery(query).getResultList();
        }
        catch (Exception e){
            if(entityManager != null){
                entityManager.close();
            }
            throw new IllegalAccessError("server on maintenance please try again later");
        }
        return result;
    }

    public static DBConnection beginTransaction() throws InnerLogicException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();
        return dbConnection;
    }

    public static void commitTransaction(DBConnection dbConnection) throws InnerLogicException {
        dbConnection.commitTransaction();
        dbConnection.closeConnections();
    }
    private static void rollBackAndClose(EntityTransaction transaction,EntityManager entityManager){
        if(transaction != null && transaction.isActive()){
            transaction.rollback();
        }
        if(entityManager != null){
            entityManager.close();
        }
    }

    public static void clearAlllRowsFromAllTables(){


    }
}
