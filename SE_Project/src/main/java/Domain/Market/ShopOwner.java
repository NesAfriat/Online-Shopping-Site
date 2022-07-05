package Domain.Market;

import Domain.InnerLogicException;

import java.util.LinkedList;
import java.util.List;

public class ShopOwner extends Role {

    private List<String> AssigneeUsernames;

    public void setAssigneeUsernames(List<String> assigneeUsernames) {
        AssigneeUsernames = assigneeUsernames;
    }

    public ShopOwner(String assignorUsername) {
        super(assignorUsername);
        AssigneeUsernames = new LinkedList<>();
    }
    public String removeAssignee(String assigneeUserName) throws InnerLogicException {
        if(AssigneeUsernames.remove(assigneeUserName)){
            return assigneeUserName;
        }
        throw new InnerLogicException("assignee: "+ assigneeUserName + "did not found");
    }


    public List<String> getAssigneeUsernames(){
        return AssigneeUsernames;
    }

    public void addAssignee(String assigneeUsername) {
        AssigneeUsernames.add(assigneeUsername);
    }


}
