package fcm.test.sockettest.model.data;

public class Message {
    private String name;
    private String message;
    private String localDateTime;
    private int viewType;

    public Message(String name, String message, String localDateTime, int viewType) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.message = message;
        this.viewType = viewType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
