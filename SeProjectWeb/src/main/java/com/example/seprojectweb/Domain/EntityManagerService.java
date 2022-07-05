package com.example.seprojectweb.Domain;

import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerService {
    public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory){
        EntityManager entityManager =  entityManagerFactory.createEntityManager();
        return entityManager;
    }
}

