package com.carol8.syinfo.model.benchmark.storage;

import android.content.Context;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.io.FileOutputStream;
import java.io.IOException;

public class RandomStorageWriteBenchmark implements Benchmark {
    private final String category = "Storage";
    private final String name = "Random write";
    private final int byteCount, blockCount, totalNanosWriting;
    private final Context context;

    public RandomStorageWriteBenchmark(int byteCount, int blockCount, int totalMillisWriting, Context context) {
        this.byteCount = byteCount;
        this.blockCount = blockCount;
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
        return 1;
    }

    @Override
    public int run() throws IOException {
        return benchmarkSeqWrite(this.byteCount, this.blockCount, this.totalNanosWriting, this.context);
    }

    private int benchmarkSeqWrite(int byteCount, int blockCount, int totalNanosWriting, Context context) throws IOException {
        int runs = 0;
        long initialGenerateTime;
        FileOutputStream fileOutputStream;
        String tempFilename = "writeSeq";
        long initialTime = System.nanoTime();

        while(initialTime + totalNanosWriting > System.nanoTime()) {
            initialGenerateTime = System.nanoTime();
            byte[] bytes = ArrayGenerators.generateBytes(byteCount);
            initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.nanoTime()));
            for(int i = 0; i < blockCount; i++) {
                fileOutputStream = context.openFileOutput(tempFilename + i + ".txt", Context.MODE_PRIVATE);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            }
            runs++;
        }
        for(int i = 0; i < blockCount; i++) {
            context.deleteFile(tempFilename + i + ".txt");
        }
        return runs;
    }
}
