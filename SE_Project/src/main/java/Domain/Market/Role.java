package Domain.Market;

public abstract class Role {
    private final String assignorUsername;

    protected Role(String assignorUsername){
        this.assignorUsername = assignorUsername;
    }

    public String getAssignorUsername(){
        return assignorUsername;
    }
}
