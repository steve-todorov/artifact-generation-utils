package org.carlspring.artifactgeneration.io;

import java.io.*;
import java.util.Random;

public class RandomOutputStream extends OutputStream {

    private RandomAccessFile file = null;

    public RandomOutputStream(String file){
        try {
            this.file = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void write(int b) throws IOException {
        file.write(b);
    }
}