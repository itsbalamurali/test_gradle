package com.girmiti.mobilepos.nfc.parser;

import java.util.LinkedList;
import java.util.List;

public class TLV {

    private static final int TAG_TOPLEVEL = 0xFFFF;

    private byte[] mValue;

    private int mIndex;

    private int mLength;

    private int mTag;

    private List<TLV> mChildren;

    private static final int EIGHT = 8;

    private static final int THREE = 3;

    private int index;

    public TLV(byte[] value) throws TLVException {
        this(value, 0, value.length, TAG_TOPLEVEL);
    }

    private TLV(byte[] value, int index, int length, int tag) throws TLVException {
        if (value == null)
            throw new IllegalArgumentException("value must not be null");

        mValue = value;
        mIndex = index;
        mLength = length;
        mTag = tag;
        mChildren = new LinkedList<>();

        if (isConstructed()) {
            parse();
        }
    }

    public int getTag() {
        return mTag;
    }

    public byte[] getValue() {
        byte[] newArray = new byte[mLength];
        System.arraycopy(mValue, mIndex, newArray, 0, mLength);
        return newArray;
    }

    public List<TLV> getChildren() {
        return mChildren;
    }

    public boolean isConstructed() {
        final int constructedBit = 0x20;
        return (getFirstTagByte(mTag) & constructedBit) != 0;
    }

    private void parse() throws TLVException {
        index = mIndex;
        int endIndex = mIndex + mLength;

        while (index < endIndex) {
            int tag = getNext(index++);
            if (tag == 0x00 || tag == 0xFF)
                continue;

            if (tagHasMultipleBytes(tag)) {
                tag <<= EIGHT;
                tag |= getNext(index++);
                if (tagHasAnotherByte(tag)) {
                    tag <<= EIGHT;
                    tag |= getNext(index++);
                }
                if (tagHasAnotherByte(tag))
                    throw new TLVException();
            }

           int length = calculateLength();

            TLV tlv = new TLV(mValue, index, length, tag);
            mChildren.add(tlv);
            index += tlv.getLength();
        }
    }

    private int getLength() {
        return mLength;
    }

    private int getNext(int index) throws TLVException {
        if (index < mIndex || index >= mIndex + mLength)
            throw new TLVException();

        return mValue[index] & 0xFF;
    }

    private static int getFirstTagByte(int tag) {
        int tagInput = tag;
        while (tagInput > 0xFF)
            tagInput >>= EIGHT;

        return tagInput;
    }

    private static boolean tagHasMultipleBytes(int tag) {
        final int multibyteTagMask = 0x1F;
        return (tag & multibyteTagMask) == multibyteTagMask;
    }

    private static boolean tagHasAnotherByte(int tag) {
        final int nextByte = 0x80;
        return (tag & nextByte) != 0;
    }

    private int calculateLength() throws TLVException {
        int length = getNext(index++);
        if (length >= 0x80) {
            int numLengthBytes = length & 0x7F;
            if (numLengthBytes > THREE)
                throw new TLVException();

            length = 0;
            for (int i = 0; i < numLengthBytes; i++) {
                length <<= EIGHT;
                length |= getNext(index++);
            }
        }
        return length;
    }
}
