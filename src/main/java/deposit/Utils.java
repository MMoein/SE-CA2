package deposit;

import java.util.Collections;

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

    public static String encodeAmount(int amount){
        String result = "";
        if(amount==0){
            return "0";
        }
        int i = 0;
        while(amount>0){
            result+=(char)(amount%10+'0');
            amount/=10;
            i++;
            if(i%3==0)
                result+=",";
        }
        return new StringBuilder(result).reverse().toString();
    }
}
