package greencode.ir.pillcci.retrofit.reqobject;

import greencode.ir.pillcci.utils.ToJsonClass;

public class sendedDeletedObject extends ToJsonClass {
    int user_id ;
    String localId ;

    public sendedDeletedObject(int user_id, String localId) {
        this.user_id = user_id;
        this.localId = localId;
    }
}
