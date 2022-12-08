package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

public class SequentialRAMReadBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Sequential read";
    private final int longCount, totalMillisReading;
    private volatile long[] longs;

    public SequentialRAMReadBenchmark(int longCount, int totalMillisReading) {
        this.longCount = longCount;
        this.totalMillisReading = totalMillisReading;
        this.longs = new long[longCount];
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
        return longCount * 4;
    }

    @Override
    public int run() {
        return benchmarkSeqRead(this.longCount, this.totalMillisReading);
    }

    private int benchmarkSeqRead(int longCount, int totalMillisWriting) {
        int runs = 0;
        long read;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisWriting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            longs = ArrayGenerators.generateLongs(longCount);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            for(int i = 0; i < longCount; i++){
                read = longs[i];
            }
            runs++;
        }
        return runs;
    }
}
