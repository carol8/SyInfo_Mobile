package com.carol8.syinfo.model.benchmark.storage;

import android.content.Context;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.io.FileOutputStream;
import java.io.IOException;

public class SequentialStorageWriteBenchmark implements Benchmark {
    private final String category = "Storage";
    private final String name = "Sequential write";
    private final int byteCount, totalNanosWriting;
    private final Context context;

    public SequentialStorageWriteBenchmark(int byteCount, int totalMillisWriting, Context context) {
        this.byteCount = byteCount;
        this.totalNanosWriting = totalMillisWriting * 1000000;
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
        return benchmarkSeqWrite(this.byteCount, this.totalNanosWriting, this.context);
    }

    private int benchmarkSeqWrite(int byteCount, int totalNanosWriting, Context context) throws IOException {
        int runs = 0;
        long initialGenerateTime;
        FileOutputStream fileOutputStream;
        String tempFilename = "writeSeq.txt";
        long initialTime = System.nanoTime();

        while(initialTime + totalNanosWriting > System.nanoTime()) {
            initialGenerateTime = System.nanoTime();
            byte[] bytes = ArrayGenerators.generateBytes(byteCount);
            initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            fileOutputStream = context.openFileOutput(tempFilename, Context.MODE_PRIVATE);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            runs++;
        }
        context.deleteFile(tempFilename);
        return runs;
    }
}
