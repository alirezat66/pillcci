package greencode.ir.pillcci.objects;

import greencode.ir.pillcci.utils.ToJsonClass;

public class BotObject extends ToJsonClass {
    String Name;
    String phoneNumber;
    String img;

    public BotObject(String name, String phoneNumber, String img) {
        Name = name;
        this.phoneNumber = phoneNumber;
        this.img = img;
    }

    public String getName() {
        return Name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImg() {
        return img;
    }
}
