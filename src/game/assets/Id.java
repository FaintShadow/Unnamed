package game.assets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Id {
    /**
      Since there are problems with the types Double and Float
      and there is no way to fix it. I decided to make this class which basically makes and ID.
      The problem with those types Is that for example if I define "A" as "129.52" in code and print it
      I will get something like "129.5200000006" which is not what I want.
    */
    private Integer ID = 0;
    private Integer subID = 0;

    public Id(Integer ID, Integer subID) {
        this.ID = ID;
        this.subID = subID;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSubID() {
        return subID;
    }

    public void setSubID(Integer subID) {
        this.subID = subID;
    }

    public String getFullID(){
        return ID.toString() + ":" + subID.toString();
    }
}
