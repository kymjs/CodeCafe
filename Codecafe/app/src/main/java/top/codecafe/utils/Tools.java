package top.codecafe.utils;

/**
 * Created by kymjs on 3/2/16.
 */
public class Tools {

    public static int toInt(String num, int defualt) {
        try {
            return Integer.valueOf(num);
        } catch (NumberFormatException e) {
            return defualt;
        }
    }

    public static long toLong(String num, long defualt) {
        try {
            return Long.valueOf(num);
        } catch (NumberFormatException e) {
            return defualt;
        }
    }
}
