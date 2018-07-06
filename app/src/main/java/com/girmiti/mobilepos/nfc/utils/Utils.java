package com.girmiti.mobilepos.nfc.utils;

import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;

public class Utils {

    private static final Logger logger = Logger.getNewLogger("com.girmiti.mobilepos.nfc.utils.Utils");

    private static final int TWO = 2;

    private static final int FOUR = 4;

    private static final int SIXTEEN = 16;

    private Utils() {

    }

	public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / TWO];
        for (int i = 0; i < len; i += TWO) {
            data[i / TWO] = (byte) ((Character.digit(s.charAt(i), SIXTEEN) << FOUR)
                    + Character.digit(s.charAt(i+1), SIXTEEN));
        }
        return data;
    }
	
	public static String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * TWO];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * TWO] = hexArray[v >>> FOUR];
            hexChars[j * TWO + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int hexToDec(String hexadecimal) throws GenericException {
        int h = hexadecimal.length() - 1;
        int d = 0;
        int n = 0;
        final int three = 3;
        final int five = 5;
        final int six = 6;
        final int seven = 7;
        final int eight = 8;
        final int nine = 9;
        final int ten = 10;
        final int eleven = 11;
        final int twelve = 12;
        final int thirteen = 13;
        final int fourteen = 14;
        final int fifteen = 15;

        for(int i = 0; i < hexadecimal.length(); i++) {
            char ch = hexadecimal.charAt(i);
            boolean flag = false;
            switch(ch) {
                case '0':
                    n = 0;
                    break;
                case '1':
                    n = 1;
                    break;
                case '2':
                    n = TWO;
                    break;
                case '3':
                    n = three;
                    break;
                case '4':
                    n = FOUR;
                    break;
                case '5':
                    n = five;
                    break;
                case '6':
                    n = six;
                    break;
                case '7':
                    n = seven;
                    break;
                case '8':
                    n = eight;
                    break;
                case '9':
                    n = nine;
                    break;
                case 'A':
                    n = ten;
                    break;
                case 'B':
                    n = eleven;
                    break;
                case 'C':
                    n = twelve;
                    break;
                case 'D':
                    n = thirteen;
                    break;
                case 'E':
                    n = fourteen;
                    break;
                case 'F':
                    n = fifteen;
                    break;
                default:
                    flag = true;
            }
            if(flag) {
                logger.debug("Wrong Entry");
                throw new GenericException("Invalid hexadecimal value " + hexadecimal);

            }
            d = (int) (n * (Math.pow(SIXTEEN, h))) + d;
            h--;
        }
        logger.debug("The decimal form of hexadecimal number " + hexadecimal + " is " + d);
        return d;
    }

    public static String paddingWithZeros(String stringToBePadded, int length) {
        String bres = stringToBePadded;
        String b = stringToBePadded;
        if(stringToBePadded.length() != length) {
            for(int i = stringToBePadded.length(); i < length; i++) {
                b = "0" + b;
            }
            bres = b;
        }
        return bres;
    }

    public static String rightPaddingWithZeros(String stringToBePadded, int length) {
        String bres = stringToBePadded;
        String b = stringToBePadded;
        if(stringToBePadded.length() != length) {
            for(int i = stringToBePadded.length(); i < length; i++) {
                b = b + "0";
            }
            bres = b;
        }
        return bres;
    }
}
