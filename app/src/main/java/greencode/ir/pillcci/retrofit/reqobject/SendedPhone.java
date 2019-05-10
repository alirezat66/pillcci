package greencode.ir.pillcci.retrofit.reqobject;

import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.utils.ToJsonClass;

public class SendedPhone extends ToJsonClass {

    public SendedPhone(PhoneBook phoneBook) {
        this.id = phoneBook.getid();
        this.fName = phoneBook.getfName();
        this.lName = phoneBook.getlName();
        this.img = phoneBook.getImg();
        this.relation = phoneBook.getRelation();
        this.phone = phoneBook.getPhone();
    }

    long id;
    String fName;
    String lName;
    String img;
    String phone;
    String relation;

}
