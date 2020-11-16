package oxygen.api.commons;

import java.util.ArrayList;
import java.util.List;

public class NumberCommons {

    public static int numbersFromString(String str) {
        String numStr = str.replaceAll("\\D+","");
        if (numStr.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(numStr);
    }

    public static List<Integer> toIntegerList(int[] ints) {
        List<Integer> intList = new ArrayList<>(ints.length);
        for (int i : ints) intList.add(i);
        return intList;
    }
}
