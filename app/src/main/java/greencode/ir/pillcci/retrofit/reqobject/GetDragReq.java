package greencode.ir.pillcci.retrofit.reqobject;

public class GetDragReq {
    String user_id;

    public GetDragReq(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
