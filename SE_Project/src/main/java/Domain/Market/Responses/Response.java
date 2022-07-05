package Domain.Market.Responses;

public class Response<T> {
    private T value;
    private final String ErrorMessage;

    public Response(String errorMessage) {
       this.ErrorMessage = errorMessage;
    }

    public Response(T value) {
        this.value = value;
        ErrorMessage = null;
    }

    public T getValue() {
        return value;
    }

    public boolean isErrorOccurred(){
        return ErrorMessage != null;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}
