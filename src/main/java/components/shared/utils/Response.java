package components.shared.utils;

public class Response {
    private boolean success;
    private String message;

    // Constructor
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters và Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
