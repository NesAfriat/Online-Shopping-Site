package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQuery(name="ShopOwner.findAll", query="SELECT s FROM ShopOwner s")
public class ShopOwner extends Role {

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> AssigneeUsernames;



    public ShopOwner(String assignorUsername, int shopID, String memberID) {
        super(assignorUsername, shopID, memberID);
        AssigneeUsernames = new LinkedList<>();
    }

    public ShopOwner() {

    }

    @Transactional
    public String removeAssignee(String assigneeUserName) throws InnerLogicException {
        if (AssigneeUsernames.remove(assigneeUserName)) {
            return assigneeUserName;
        }
        throw new InnerLogicException("assignee: " + assigneeUserName + "did not found");
    }
    @Transactional
    public void addAssignee(String assigneeUsername) {
        AssigneeUsernames.add(assigneeUsername);
    }

    public List<String> getAssigneeUsernames() {
        return AssigneeUsernames;
    }

    public void setAssigneeUsernames(List<String> assigneeUsernames) {
        AssigneeUsernames = assigneeUsernames;
    }




}
