package com.carol8.syinfo.model.benchmark.storage;

import android.content.Context;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.io.FileOutputStream;
import java.io.IOException;

public class SequentialStorageWriteBenchmark implements Benchmark {
    private final String category = "Storage";
    private final String name = "Sequential write";
    private final int byteCount, totalMillisWriting;
    private final Context context;

    public SequentialStorageWriteBenchmark(int byteCount, int totalMillisWriting, Context context) {
        this.byteCount = byteCount;
        this.totalMillisWriting = totalMillisWriting;
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
        return benchmarkSeqWrite(this.byteCount, this.totalMillisWriting, this.context);
    }

    private int benchmarkSeqWrite(int byteCount, int totalMillisWriting, Context context) throws IOException {
        int runs = 0;
        long initialGenerateTime;
        FileOutputStream fileOutputStream;
        String tempFilename = "writeSeq.txt";
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisWriting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            byte[] bytes = ArrayGenerators.generateBytes(byteCount);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
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
