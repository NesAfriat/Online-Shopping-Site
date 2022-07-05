package com.example.seprojectweb;

import com.example.seprojectweb.Domain.EntityManagerService;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class MockDataBase {
    public static boolean mockPersist = false;
    public static boolean mockRemove = false;
    public static boolean mockFind = false;
    public static boolean mockMerge = false;
    public static boolean  mockSessionUpdate = false;
    public static void changeToTestDB(){
        PersistenceManager.setDBConnection("test");
    }
    // 1 = mock persist, 2 = mock remove , 3 = move
    public static void mockDataBase(){
        //mock transaction for seesion
        Transaction transactionMock = Mockito.mock(Transaction.class);
        Mockito.doNothing().when(transactionMock).begin();
        Mockito.doNothing().when(transactionMock).commit();
        Mockito.doNothing().when(transactionMock).rollback();
        Mockito.doReturn(false).when(transactionMock).isActive();
        //mock session
        Session sessionMock = Mockito.mock(Session.class);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);

        if(mockSessionUpdate){
            Mockito.doThrow(new IllegalArgumentException("update mock database")).when(sessionMock).update(Mockito.any());
        }else {
            Mockito.doNothing().when(sessionMock).update(Mockito.any());
        }
        mockSessionUpdate = false;


        //mock entity transaction
        EntityTransaction entityTransactionMock = Mockito.mock(EntityTransaction.class);
        Mockito.doNothing().when(entityTransactionMock).begin();
        Mockito.doNothing().when(entityTransactionMock).commit();
        Mockito.doNothing().when(entityTransactionMock).rollback();
        Mockito.doReturn(false).when(transactionMock).isActive();


        //mock entity manager

        EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
        if(mockMerge){
            Mockito.doThrow(new IllegalArgumentException("merge mock exception")).when(entityManagerMock).merge(Mockito.any());
        }
        else {
            Mockito.doReturn(null).when(entityManagerMock).merge(Mockito.any());
        }
        if(mockRemove){
            Mockito.doThrow(new IllegalArgumentException("remove mock exception")).when(entityManagerMock).remove(Mockito.any());
        }else {
            Mockito.doNothing().when(entityManagerMock).remove(Mockito.any());
        }
        if(mockFind){
            Mockito.doThrow(new IllegalArgumentException("find mock exception")).when(entityManagerMock).find(Mockito.any(),Mockito.any());
        }else {
            Mockito.doReturn(null).when(entityManagerMock).find(Mockito.any(),Mockito.any());
        }
        if(mockPersist){
            Mockito.doThrow(new IllegalArgumentException("mock save exception")).when(entityManagerMock).persist(Mockito.any());
        }
        else {
            Mockito.doNothing().when(entityManagerMock).persist(Mockito.any());
        }
        mockPersist = false;
        mockRemove = false;
        mockFind = false;
        mockMerge = false;
        mockSessionUpdate = false;

        Mockito.when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);
        Mockito.when(entityManagerMock.unwrap(Mockito.any())).thenReturn(sessionMock);
        Mockito.doNothing().when(entityManagerMock).close();
        Mockito.when(entityManagerMock.contains(Mockito.any())).thenReturn(true);


        //mock entity service
        EntityManagerService entityManagerServiceMock = Mockito.mock(EntityManagerService.class);
        Mockito.when(entityManagerServiceMock.createEntityManager(Mockito.any())).thenReturn(entityManagerMock);


        PersistenceManager.setEntityManagerService(entityManagerServiceMock);
    }
    public static void deleteMock(){
        PersistenceManager.setEntityManagerService(new EntityManagerService());
    }
}
