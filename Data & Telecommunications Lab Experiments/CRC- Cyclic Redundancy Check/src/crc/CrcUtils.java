package crc;

public class CrcUtils {
    
    static String divisor1 = "10001000000100001";

    public static String getEncodedStringWithCrc(String data) {
        int data_bits = 26;
        int divisor_bits = 17;
        int tot_length = data_bits + divisor_bits - 1, i;

        int[] div = new int[tot_length];
        int[] rem = new int[tot_length];
        int[] crc = new int[tot_length];
        int[] num = new int[data.length()];
        int[] divisor = new int[divisor1.length()];

        for (i = 0; i < data.length(); i++) {
            num[i] = data.charAt(i) - '0';
        }
        for (i = 0; i < divisor1.length(); i++) {
            divisor[i] = divisor1.charAt(i) - '0';
        }


        for (i = 0; i < data.length(); i++) {
            div[i] = num[i];
        }
        for (i = data.length(); i < divisor1.length() + data.length() - 1; i++) {
            div[i] = 0;
        }

        //System.out.print("Dividend (after appending 0's) are : ");
        for (i = 0; i < div.length; i++) //System.out.print(div[i]);  
        //System.out.println("\n");
        {
            for (int j = 0; j < div.length; j++) {
                rem[j] = div[j];
            }
        }

        rem = divide(div, divisor, rem);

        for (i = 0; i < div.length; i++) //append dividend and ramainder
        {
            //crc[i]=(div[i]^rem[i]);
            crc[i] = (div[i] ^ rem[i]);
        }

        String p = "";
        //System.out.println("CRC code : ");    
        for (i = 0; i < crc.length; i++) {
            p += crc[i];
        }

        return p;

    }

    static int[] divide(int div[], int divisor[], int rem[]) {
        int cur = 0;
        while (true) {
            for (int i = 0; i < divisor.length; i++) {
                rem[cur + i] = (rem[cur + i] ^ divisor[i]);
            }

            while (rem[cur] == 0 && cur != rem.length - 1) {
                cur++;
            }

            if ((rem.length - cur) < divisor.length) {
                break;
            }
        }
        return rem;
    }

    public static String verifyCRC(String crcCode) {
        int tot_length = 26 + 17 - 1;
        String tt = "";
        int i = 0;
        int[] div = new int[tot_length];
        int[] rem = new int[tot_length];
        int[] crc = new int[tot_length];
        //int[] num = new int[data.length()];
        int[] divisor = new int[divisor1.length()];
        for (i = 0; i < crcCode.length(); i++) {
            crc[i] = crcCode.charAt(i) - '0';
        }
        for (int j = 0; j < crc.length; j++) {
            rem[j] = crc[j];
        }
        for (i = 0; i < divisor1.length(); i++) {
            divisor[i] = divisor1.charAt(i) - '0';
        }

        rem = divide(crc, divisor, rem);

        for (i = 0; i < rem.length; i++) {
            if (rem[i] != 0) {
                //System.out.println("Error");
                tt = "Error";
                break;
            }
            if (i == rem.length - 1) {
                tt = "No Error";
            }
        }
        return tt;
    }

}
