package com.owlbear.rowcounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DataController {

    public static final char ETX = (char)3;

    private static DataController instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public static DataController loadInstance(File file) throws IOException {
        if(instance == null) {
            instance = new DataController(file);
        }
        return instance;
    }

    public static DataController getInstance() {
        if(instance == null) {
            instance = new DataController();
        }
        return instance;
    }

    private ArrayList<Counter> counters;
    public File file;

    private DataController() {
        counters = new ArrayList<>();
        file = null;
    }

    private DataController(File file) throws IOException {
        counters = new ArrayList<>();
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream fin = new FileInputStream(file);
        fin.read(bytes);
        fin.close();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while(buffer.hasRemaining()) {
            counters.add(new Counter(buffer));
        }
        this.file = file;
    }

    public void save() throws IOException {
        int saveSize = 0;
        for(Counter c : counters) {
            saveSize += c.saveSize();
        }
        ByteBuffer buffer = ByteBuffer.allocate(saveSize);
        for(Counter c : counters) {
            c.save(buffer);
        }
        FileOutputStream fOut = new FileOutputStream(file);
        fOut.write(buffer.array());
        fOut.close();
    }

    public void addCounter(Counter counter) {
        counters.add(counter);
    }

    public int size() {
        return counters.size();
    }

    public Counter get(int index) {
        return counters.get(index);
    }

    public void deleteCounter(int index) {
        counters.remove(index);
    }

    public void deleteCounter(Counter counter) {
        counters.remove(counter);
    }
}
