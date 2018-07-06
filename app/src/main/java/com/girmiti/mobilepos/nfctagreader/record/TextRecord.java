/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.girmiti.mobilepos.nfctagreader.record;

import android.nfc.NdefRecord;

import com.girmiti.mobilepos.logger.Logger;
import com.google.common.base.Preconditions;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


/**
 * An NFC Text Record
 */
public class TextRecord {

    private static final Logger logger = Logger.getNewLogger("com.girmiti.mobilepos.nfcTagReader.record.TextRecord");

    private TextRecord() {
    }

    public static String parse(NdefRecord record) {
        final int seventySeven = 0077;
        final int twoHundred = 0200;
        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_TEXT));
        try {
            byte[] payload = record.getPayload();
            /*
             * payload[0] contains the "Status Byte Encodings" field, per the
             * NFC Forum "Text Record Type Definition" section 3.2.1.
             *
             * bit7 is the Text Encoding Field.
             *
             * if Bit_7 equals 0: The text is encoded in UTF-8
             * if Bit_7 equals 1: The text is encoded in UTF16
             *
             * Bit_6 is reserved for future use and must be set to zero.
             *
             * Bits 5 to 0 are the length of the IANA language code.
             */
            String textEncoding = ((payload[0] & twoHundred) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & seventySeven;
            return new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            // should never happen unless we get a malformed tag.
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isText(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            logger.severe("error "+e);
            return false;
        }
    }
}
