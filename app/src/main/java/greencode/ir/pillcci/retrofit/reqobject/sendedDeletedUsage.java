package greencode.ir.pillcci.retrofit.reqobject;

import greencode.ir.pillcci.utils.ToJsonClass;

public class sendedDeletedUsage extends ToJsonClass {
    int drug_id ;
    String localId ;
    int user_id ;

    public sendedDeletedUsage(int drug_id, String local_id, int user_id) {
        this.drug_id = drug_id;
        this.localId = local_id;
        this.user_id = user_id;
    }
}
