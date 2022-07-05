package com.example.seprojectweb.Domain;

import com.example.seprojectweb.PersistenceFailure;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DBConnection {
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private EntityTransaction transaction;
    private Stack<PersistenceFailure> failures;
    private Session session;
    Transaction sessionTransaction;

    public DBConnection() {
        failures = new Stack<>();
    }

    public void setConnection() {
        try {
            entityManagerFactory = PersistenceManager.getEntityManagerFactory();
            entityManager = entityManagerFactory.createEntityManager();
            session = entityManager.unwrap(Session.class);
        }catch (Exception e){
            onFailure();
            closeConnections();
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }
    public EntityTransaction beginTransaction() {
        try {
            transaction = entityManager.getTransaction();
            sessionTransaction =  session.getTransaction();

            transaction.begin();
            return transaction;
        }catch (Exception e){
            onFailure();
            closeConnections();
            throw new IllegalAccessError("server on maintenance please try again later");
        }

    }
    public void commitTransaction() {
        try {
            boolean isOpen = transaction.isActive();
            transaction.commit();
        }catch (Exception e){
            execFail();
        }
    }
    public boolean isActiveTransaction(){
        return transaction.isActive();
    }
    public void closeConnections() {
        try {

            entityManager.close();
            session.close();
        }catch (Exception e){
            execFail();
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }
    public void persist(Object o)  {
        try {
            entityManager.persist(o);
        }catch (Exception e){
            execFail();
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }
    public void update(Object o) {
        try {
            entityManager.merge(o);
        }catch (Exception e){
            execFail();
            throw new IllegalAccessError("server on maintenance please try again later");
        }

    }
    public void remove(Object o){
        try {
            entityManager.remove(entityManager.contains(o) ? o : entityManager.merge(o));
        }catch (Exception e){

            if(transaction.isActive()){
                transaction.rollback();
            }
            execFail();
            throw new IllegalAccessError("server on maintenance please try again later");
        }
    }
    public void onFailure() {
        while (!failures.isEmpty()){
            failures.pop().onFailure();
        }
    }

    public void addFailure(PersistenceFailure persistenceFailure){
        failures.push(persistenceFailure);
    }
    public void updateSession(Object m) {

        try{
            session.update(m);
        }
        catch (Exception e){
            execFail();
            throw new IllegalAccessError("server update is on maintain please try again later");
        }
    }


    public Object find(Class<?> toFindClass, Object key) {

        Object result;
        try{
            result = entityManager.find(toFindClass, key);
        }catch (Exception e){
            execFail();
            throw new IllegalAccessError("server update is on maintain please try again later");
        }

        return result;
    }
    private void rollbackTransactionIfNeed(){
        if(transaction.isActive()){
            transaction.rollback();
        }
    }
    private void execFail() {
        rollbackTransactionIfNeed();
        onFailure();
        closeConnections();
    }

}
