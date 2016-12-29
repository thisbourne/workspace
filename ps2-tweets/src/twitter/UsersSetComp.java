package twitter;

import java.util.Comparator;
import java.util.Map;

public class UsersSetComp implements Comparator<Map.Entry<String, Integer>>{
    public int compare(Map.Entry<String, Integer> user1, Map.Entry<String, Integer> user2){
        if(user1.getValue() > user2.getValue()) return -1;
        //else if(user1.getValue().equals(user2.getValue())) return 0;
        else return 1;
    }
}


