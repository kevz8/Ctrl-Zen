package thunderbirdsonly.nwhackbackend.Pojo;

public class Result {
    private int status;
    private String message;
    private Object data;


    public Result(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public static Result success() {
        return new Result(1, "success", null);
    }

    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    public static Result error(String message) {
        return new Result(0, message, null);
    }

    @Override
    public String toString() {
        return "Result [status=" + status + ", message=" + message + ", data=" + data + "]";
    }



}
