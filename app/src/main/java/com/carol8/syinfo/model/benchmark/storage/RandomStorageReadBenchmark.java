package com.carol8.syinfo.model.benchmark.storage;

import android.content.Context;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RandomStorageReadBenchmark implements Benchmark {
    private final String category = "Storage";
    private final String name = "Random read";
    private final int byteCount, blockCount, totalNanosReading;
    private final Context context;

    public RandomStorageReadBenchmark(int byteCount, int blockCount, int totalMillisReading, Context context) {
        this.byteCount = byteCount;
        this.blockCount = blockCount;
        this.totalNanosReading = totalMillisReading * 1000000;
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
        return 1;
    }

    @Override
    public int run() throws IOException {
        return benchmarkSeqWrite(this.byteCount, this.blockCount, this.totalNanosReading, this.context);
    }

    private int benchmarkSeqWrite(int byteCount, int blockCount, int totalNanosReading, Context context) throws IOException {
        int runs = 0;
        long initialGenerateTime;
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        String tempFilename = "readSeq";
        long initialTime = System.nanoTime();

        while(initialTime + totalNanosReading > System.nanoTime()) {
            initialGenerateTime = System.nanoTime();
            byte[] bytes = ArrayGenerators.generateBytes(byteCount);
            for(int i = 0; i < blockCount; i++) {
                fileOutputStream = context.openFileOutput(tempFilename + i + ".txt", Context.MODE_PRIVATE);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            }
            initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisReading - System.nanoTime()));
            for(int i = 0; i < blockCount; i++) {
                fileInputStream = context.openFileInput(tempFilename + i + ".txt");
                fileInputStream.read(bytes);
                fileInputStream.close();
            }
            runs++;
        }
        for(int i = 0; i < blockCount; i++) {
            context.deleteFile(tempFilename + i + ".txt");
        }
        return runs;
    }
}
