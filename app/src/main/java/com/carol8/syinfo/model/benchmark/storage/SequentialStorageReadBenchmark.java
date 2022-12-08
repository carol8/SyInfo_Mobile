package com.carol8.syinfo.model.benchmark.storage;

import android.content.Context;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SequentialStorageReadBenchmark implements Benchmark {
    private final String category = "Storage";
    private final String name = "Sequential read";
    private final int byteCount, totalMillisReading;
    private final Context context;

    public SequentialStorageReadBenchmark(int byteCount, int totalMillisReading, Context context) {
        this.byteCount = byteCount;
        this.totalMillisReading = totalMillisReading;
        this.context = context;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public String getCategory(){
        return this.category;
    }

    @Override
    public int getMultipler() {
        return byteCount;
    }

    @Override
    public int run() throws IOException {
        return benchmarkSeqRead(this.byteCount, this.totalMillisReading, this.context);
    }

    private int benchmarkSeqRead(int byteCount, int totalMillisReading, Context context) throws IOException {
        int runs = 0;
        long initialGenerateTime;
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        String tempFilename = "readSeq.txt";
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisReading > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            byte[] bytes = ArrayGenerators.generateBytes(byteCount);
            fileOutputStream = context.openFileOutput(tempFilename, Context.MODE_PRIVATE);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            fileInputStream = context.openFileInput(tempFilename);
            fileInputStream.read(bytes);
            fileInputStream.close();
            runs++;
        }
        context.deleteFile(tempFilename);
        return runs;
    }
}
