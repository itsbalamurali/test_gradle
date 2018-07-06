package com.girmiti.mobilepos.nfc.reader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.util.Base64;

import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.nfc.parser.TLV;
import com.girmiti.mobilepos.nfc.parser.TLVException;
import com.girmiti.mobilepos.nfc.utils.Utils;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.FacadeApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MposCardReader implements NfcAdapter.ReaderCallback {

    private static final Logger logger = Logger.getNewLogger("com.girmiti.mobilepos.nfc.reader.MposCardReader");

    public static final String COUNTRY_CODE = "0356";
    public static final String CURRENCY_CODE = "0356";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};
    private static final String CMD_SELECT_PPSE = "325041592E5359532E4444463031";
    private static final String CMD_GPO = "80A80000";
    private static final String CMD_READ_RECORD = "00B2";
    private static final String CMD_CRYPTO_CHECKSUM = "802A8E8016";
    private static final int TVR_BYTE_SIZE = 5;
    private static final int TTQ_BYTE_SIZE = 4;
    private static int txnAmount = 0;
    private static int txnCashback = 0;
    private static int txnType = 0;
    public static byte[]  TTQ = new byte[TTQ_BYTE_SIZE];
    public static byte[] TVR = new byte[TVR_BYTE_SIZE];
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;
    private String keyHash = null;
    private String valueHash = null;
    private Date currentDate;
    private Map<String, String> tagData = new HashMap<>();
    private Map<String, String> responseData = new HashMap<>();
    private WeakReference<DataCallback> dataCallback;
    private HashMap<String, String> priorityMap = new HashMap<>();
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int RANDOM_MIN = 10000;
    private static final int RANDOM_MAX = 90000;
    private static final String DATEFORMAT= "yyMMdd";
    private static final String UPREDICTNUM= "unPredictNum";
    private static final String ACQID= "A0000000031010";
    private static final String STRFORMAT= "%012d";
    private static final String EXPIRY= "expiry";
    private String unPredictNum;
    private String highestPriority = null;
    private String aid = null;
    private int count = 0;
    private byte[] command;
    private byte[] payload;
    private IsoDep isoDep;

    public MposCardReader(DataCallback dataCallback) {
        this.dataCallback = new WeakReference<>(dataCallback);
    }

    public static byte[] buildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return Utils.hexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / TWO) + aid);
    }

    public static byte[] buildReadRecordApdu(String data) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return Utils.hexStringToByteArray(CMD_READ_RECORD /*+ String.format("%02X", data.length() / 2)*/ + data);
    }

    public static byte[] buildGPOForVISA(int amount, int cashBack, int txnType, String unPredictNum) {
//         9F66	04 bytes	Terminal Transaction Qualifiers
//         9F02	06 bytes	Amount Authorized
//         9F03	06 bytes	Amount Other
//         9F1A	02 bytes	Issuer Country Code
//         95	05 bytes	Terminal Verification Results
//         5F2A	02 bytes	Transaction Currency Code
//         9A	03 bytes	Transaction date
//         9C	01 byte	    Transaction type
//         9F37	04 bytes	Unpredictable number

       String ttqString;
        SharedPreferences preferences = FacadeApplication.getAppContext().getSharedPreferences("ttqPreference", Context.MODE_PRIVATE);
        ttqString = preferences.getString("ttqBits", null);

        // The TTQ settings screen need to be visited at least once to initialize, otherwise it will be null.
        if (ttqString != null) {
            TTQ = Base64.decode(ttqString, Base64.DEFAULT);
        } else {
            TTQ = Utils.hexStringToByteArray("B0E04000");  // need to set a default value if TTQ settings screen not visited.
        }

        logger.debug("ttqbits::::::" + Utils.byteArrayToHexString(TTQ));
        logger.debug("TVRbits::::::" + Utils.byteArrayToHexString(TVR));

        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        Date date = new Date();
        logger.debug(dateFormat.format(date));

        return Utils.hexStringToByteArray(CMD_GPO
                + "83" // template
                + "21" // LENGTH
                + Utils.byteArrayToHexString(TTQ) //"B0E04000"
                + String.format(STRFORMAT, amount)
                + String.format(STRFORMAT, cashBack)
                + COUNTRY_CODE
                + Utils.byteArrayToHexString(TVR) //"0400008000"
                + CURRENCY_CODE
                + dateFormat.format(date)
                + String.format("%02X", txnType)
                + unPredictNum
                + "00"); // LE
    }

    public static byte[] BuildGPOForMC() {
//        '03' or '0D'
//        PDOL Related Data, formatted as follows:
//        Either:
//        [1] Command Template '83'
//                [2] Command Template Length = '0B'
//                [3 : 10] Terminal Risk Management Data
//        [11 : 12] Terminal Country Code
//                [13] Terminal Type
//        Or:
//        [1] Command Template '83'
//                [2] Command Template Length = '01'
//                [3] Terminal Type

        return Utils.hexStringToByteArray(CMD_GPO
                + "0383012200");
    }

    public static byte[] BuildComputeCryptoChecksumApdu(String unPredictNum, int amount, int txnType) {


        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        Date date = new Date();
        logger.debug(dateFormat.format(date));

        return
                Utils.hexStringToByteArray(CMD_CRYPTO_CHECKSUM
                        + unPredictNum
                        + "01"
                        + String.format(STRFORMAT, amount)
                        + CURRENCY_CODE
                        + COUNTRY_CODE
                        + String.format("%02X", txnType)
                        + dateFormat.format(date)
                        + "0000"
                        + "22"
                        + "00");
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Date toDate(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(pattern);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            logger.severe("Date format error "+e);
        }
        return null;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        isoDep = IsoDep.get(tag);
        if (isIsoDepNull(isoDep)) {
            try {
                // Connect to the remote NFC device
                isoDep.connect();

                updateTVR(BYTE_4, SEVEN);
               checkCvmsetAndUpdateTVR();
                currentDate = Calendar.getInstance().getTime();

                executePPSECommand();

                if (aid != null && !aid.equals("")) {
                   executeSelectAIDCommand();

                    executeGPOCommand();
                    tagData.clear();

                    // @See http://www.openscdp.org/scripts/tutorial/emv/reademv.html
                    // http://www.openscdp.org/scripts/tutorial/emv/readapplicationdata.html
                    parseTLV(payload);
                    String afl = tagData.get("94");
                    logger.debug("AFL " + afl);

                    checkAFL(afl);

                } else logger.debug("PPSE select failed");

            } catch (IOException e) {
                logger.error("Input/Output error "+e);
            } catch (TLVException te) {
                logger.error("TLV error "+te);
            } catch (Exception e) {
                logger.error("error "+e);
            }
        }

        dataCallback.get().onDataReceived(responseData);
    }

    private boolean isIsoDepNull(IsoDep isoDep) {
        return isoDep != null;
    }

    private byte[] sendAndReceive(IsoDep isoDep, byte[] command) throws IOException {
        logger.debug("Sending >>: " + Utils.byteArrayToHexString(command));

        byte[] result = isoDep.transceive(command);
        int resultLength = result.length;

        byte[] statusWord = {result[resultLength - TWO], result[resultLength - 1]};
        byte[] payloadResp = Arrays.copyOf(result, resultLength - TWO);

        logger.debug("Received <<: " + Utils.byteArrayToHexString(payloadResp) + " ::::::::::and statusWord is " + Utils.byteArrayToHexString(statusWord));

        if (Arrays.equals(SELECT_OK_SW, statusWord)) {
            return payloadResp;
        } else throw new IOException("sendAndReceive failed");
    }

    private void parseTLV(byte[] tlvData) throws TLVException {
        TLV data = new TLV(tlvData);
        List<TLV> children = data.getChildren();
        for (TLV tlv : children) {

            String key = Integer.toHexString(tlv.getTag()).toUpperCase();
            String value = Utils.byteArrayToHexString(tlv.getValue());

            if (key.equals("87")) {
                valueHash = value;
            } else if (key.equalsIgnoreCase("4F")) {
                keyHash = value;
            }
            if (keyHash != null)
                priorityMap.put(keyHash, valueHash);

            tagData.put(key, value);

            if (tlv.getChildren().size() > 0) {
                parseTLV(tlv.getValue());
            }
        }
    }

    public interface DataCallback {
        void onDataReceived(Map<String, String> responseData);
    }

    private void checkAFL(String afl) throws GenericException, IOException, TLVException {
        if (afl != null && !afl.equals("")) {
            executeReadRecordCommand(afl);

            computeCryptoChecksum();

        } else logger.debug("No AFLs found");
    }

    private static void updateTVR(int index, int number) {
        TVR[index] |= 1 << number;
    }

    private void selectTag4F(String priorityValue,String priorityKey) {
        if (count == 0 || Integer.parseInt(highestPriority) > Integer.parseInt(priorityValue)) {
            highestPriority = priorityValue;
            aid = priorityKey;
        } else if (highestPriority == null && aid != null) {
            aid = priorityKey;
        }
    }

    private void executePPSECommand() throws IOException, TLVException {
        command = buildSelectApdu(CMD_SELECT_PPSE);
        payload = sendAndReceive(isoDep, command);
        // Response to PPSE select
        parseTLV(payload);

        // Search for AID to select, Tag 4F
        for (Map.Entry<String, String> entry : priorityMap.entrySet()) {
            selectTag4F(entry.getValue(),entry.getKey());
            count++;
        }
        count = 0;

        logger.debug("highest priority aid " + aid);

        if (aid != null && aid.equals(ACQID)) {
            unPredictNum = String.format("%08d", getRandomNumberInRange(RANDOM_MIN, RANDOM_MAX));
            responseData.put(UPREDICTNUM, unPredictNum);
        } else if (aid != null && aid.equals("A0000000041010")) {
            String randomNumber = String.format("%08d", getRandomNumberInRange(RANDOM_MIN, RANDOM_MAX)).substring(FIVE);
            unPredictNum = Utils.paddingWithZeros(randomNumber, EIGHT);
            responseData.put(UPREDICTNUM, unPredictNum);
            logger.debug(":::::::::unPredictNum::::::::" + unPredictNum);
        } else {
            unPredictNum = String.format("%016d", getRandomNumberInRange(RANDOM_MIN, RANDOM_MAX));
            responseData.put(UPREDICTNUM, unPredictNum);
        }
        logger.debug("unPredictNum " + unPredictNum);
    }

    private void executeSelectAIDCommand() throws IOException, TLVException {
        command = buildSelectApdu(aid);
        payload = sendAndReceive(isoDep, command);
        tagData.clear();

        parseTLV(payload);
        String appName = tagData.get("50");
        if (appName != null && !appName.equals("")) {
            //responseData.put("name", new String(Utils.hexStringToByteArray(appName)))
        }
    }

    private void executeGPOCommand() throws IOException, TLVException {
        if (aid.equals(ACQID)) {
            // VISA
            command = buildGPOForVISA(txnAmount, txnCashback, txnType, unPredictNum);
        } else {
            // Master Card
            command = BuildGPOForMC();
        }
        payload = sendAndReceive(isoDep, command);

        parseTLV(payload);

        // Read AIP
        String aip = tagData.get("82");
        if (aip != null)
            responseData.put("aip", aip);

        if (aid.equals(ACQID)) {
            getCardDetailsGPO();
        }
    }

    private void executeReadRecordCommand(String afl) throws GenericException, TLVException {
        if (afl.length() % FOUR != 0)
            throw new GenericException("Incorrect AFL");

        for (int i = 0; i < afl.length(); i = i + EIGHT) {
            byte[] _afl = Utils.hexStringToByteArray(afl.substring(i, i + EIGHT));
            int sfi = (_afl[0] & 0xF8) >> THREE;
            int rec = _afl[1];
            String strRec = String.format("%02X", ((sfi & 0xFF) << THREE) | 0x04);
            command = buildReadRecordApdu(String.format("%02d", rec) + strRec + "00");
            try {
                payload = sendAndReceive(isoDep, command);
            } catch (Exception e) {
                logger.debug("Record not found. "+e);
                continue;
            }
            tagData.clear();
            parseTLV(payload);
            getCardDataReadRecord();
        }
    }

    /**
     * Compute crypto checksum command
     */
    private void computeCryptoChecksum() throws IOException, TLVException {

        if (!aid.equals(ACQID)) {
            command = BuildComputeCryptoChecksumApdu(unPredictNum, txnAmount, txnType);
            payload = sendAndReceive(isoDep, command);

            tagData.clear();
            parseTLV(payload);

            // Read CVC3
            String cvc3 = tagData.get("9F61");
            if (cvc3 != null)
                responseData.put("cvc3", cvc3);

            // Read CryptoATC
            String cryptoATC = tagData.get("9F36");
            if (cryptoATC != null)
                responseData.put("cryptoATC", cryptoATC);

        }
    }

    private void extractName(String track1) {
        if (track1 != null && !track1.equals("")) {
            String name1 = CommonUtils.hexToString(track1);
            String open = "^";
            String close = "^";
            String[] name = CommonUtils.substringsBetween(name1, open, close);
            StringBuilder sb = new StringBuilder();
            for (int a = 0; a < name.length; a++) {
                sb.append(name[a]);
            }
            logger.debug("card holder Name : " + sb.toString());
            responseData.put("name", (sb.toString()));
        }
    }

    private void getCardDetailsGPO() {
        // Read application PAN
        String pan = tagData.get("5A");
        if (pan != null)
            responseData.put("pan", pan);

        // Read Card Exp
        String expiry = tagData.get("5F24");
        if (expiry != null)
            responseData.put(EXPIRY, expiry);

        // Read Card Exp
        String track2 = tagData.get("57");
        if (track2 != null) {
            extractPanExpiry(track2);
        }

        // Read Card Holder Name
        String name = tagData.get("5F20");
        if (name != null)
            responseData.put("name", new String(Utils.hexStringToByteArray(name)));

        // Read PAN Seq Num
        String panSeqNum = tagData.get("5F34");
        if (panSeqNum != null)
            responseData.put("panSeqNum", (panSeqNum));

        // Read ATC
        String atc = tagData.get("9F36");
        if (atc != null)
            responseData.put("atc", atc);

        // Read Card Exp
        String cryptogram = tagData.get("9F26");
        if (cryptogram != null)
            responseData.put("cryptogram", cryptogram);

        // Read Cryptogram Info
        String cryptogramInfo = tagData.get("9F27");
        if (cryptogram != null)
            responseData.put("cryptogramInfo", cryptogramInfo);

        // Read Issuer App data
        String issuerAppData = tagData.get("9F10");
        if (issuerAppData != null)
            responseData.put("issuerAppData", issuerAppData);

        String isNewCard = tagData.get("9F13");
        if (isNewCard != null && isNewCard.equals("0")) {
            updateTVR(BYTE_2, THREE);
        }
    }

    private void extractPanExpiry(String track2) {
        responseData.put("track2", track2);

        // FOR VISA get PAN from TACK 2
        if (aid.equals(ACQID)) {
            responseData.put("pan", track2.substring(0, track2.indexOf('D')));
            responseData.put(EXPIRY, track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + SEVEN));

            Date expiryDate = toDate(track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + SEVEN), DATEFORMAT);
            if (currentDate.compareTo(expiryDate) > 0) {
                updateTVR(BYTE_2, SIX);
            }
        }
    }

    private void getCardDataReadRecord() {
        String pan = tagData.get("5A");
        if (pan != null)
            responseData.put("pan", pan);

        String expiry = tagData.get("5F24");
        if (expiry != null)
            responseData.put(EXPIRY, expiry);

        String cardEffectiveDate = tagData.get("5F25");
        if (cardEffectiveDate != null && currentDate.compareTo(toDate(cardEffectiveDate, DATEFORMAT)) > 0) {
            updateTVR(BYTE_2, FIVE);
        }

        String track1 = tagData.get("56");
        extractName(track1);

        String track2 = tagData.get("57");
        if (track2 != null)
            responseData.put("track2", track2);

        String name = tagData.get("5F20");
        if (name != null)
            responseData.put("name", new String(Utils.hexStringToByteArray(name)));

        String _9f6bValue = tagData.get("9F6B");
        if (_9f6bValue != null)
            responseData.put("_9f6bValue", _9f6bValue);

        String panSeqNum = tagData.get("5F34");
        if (panSeqNum != null)
            responseData.put("panSeqNum", (panSeqNum));
    }

    private void checkCvmsetAndUpdateTVR() {
        SharedPreferences preferences = FacadeApplication.getAppContext().getSharedPreferences("CvmPrefernce", Context.MODE_PRIVATE);
        boolean isCvmSet = preferences.getBoolean("isChecked", false);
        // By default it will be false if we don't visit Terminal capability settings screen
        if (isCvmSet) {
            updateTVR(BYTE_3, SIX);
        }
    }
}
