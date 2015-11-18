package deposit;

/**
 * Created by Moein on 11/18/15.
 */
public class Utils {
    public static int decodeAmount(String amount) {
        String cleanedAmount = "";
        String[] stringArr = amount.split(",");
        for(int i=0;i<stringArr.length;i++){
            cleanedAmount = cleanedAmount.concat(stringArr[i]);
        }
        return Integer.parseInt(cleanedAmount);
    }
}
