package com.owlbear.rowcounter;

import java.nio.ByteBuffer;

public class Counter {

    public String name;
    public int repeatRowCount;
    public int rowNumber;
    public int repeatNumber;
    public boolean linkCounters;
    public boolean keepScreenOn;

    public Counter(String name, int repeatRowCount, int rowNumber, int repeatNumber) {
        this.name = name;
        this.repeatRowCount = repeatRowCount;
        this.rowNumber = rowNumber;
        this.repeatNumber = repeatNumber;
        linkCounters = false;
        keepScreenOn = false;
    }

    public Counter(ByteBuffer buffer) {
        name = "";
        char c = buffer.getChar();
        while(c != DataController.ETX) {
            name += c;
            c = buffer.getChar();
        }
        repeatRowCount = buffer.getInt();
        rowNumber = buffer.getInt();
        repeatNumber = buffer.getInt();
        byte b = buffer.get();
        linkCounters = (b & 1) == 1;
        keepScreenOn = (b & 2) == 2;
    }

    public void increment() {
        rowNumber++;
        if(linkCounters && rowNumber > repeatRowCount) {
            rowNumber = 1;
            repeatNumber++;
        }
    }

    public void decrement() {
        if(linkCounters && rowNumber == 1 && repeatNumber == 0) {
            rowNumber--;
        } else if(rowNumber > 0) {
            rowNumber--;
            if (linkCounters && rowNumber == 0) {
                rowNumber = repeatRowCount;
                repeatNumber--;
            }
        }
    }

    public int saveSize() {
        return (name.length() + 1)*2 + 4*3 + 1;
    }

    public void save(ByteBuffer buffer) {
        for(int i = 0; i < name.length(); i++) {
            buffer.putChar(name.charAt(i));
        }
        buffer.putChar(DataController.ETX);
        buffer.putInt(repeatRowCount);
        buffer.putInt(rowNumber);
        buffer.putInt(repeatNumber);
        byte b = 0;
        if(linkCounters) {
            b += 1;
        }
        if(keepScreenOn) {
            b += 2;
        }
        buffer.put(b);
    }

    public void reset() {
        rowNumber = 0;
        repeatNumber = 0;
    }
}
