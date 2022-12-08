package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

public class SequentialRAMWriteBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Sequential write";
    private final int longCount, totalMillisWriting;
    private volatile long[] longs;

    public SequentialRAMWriteBenchmark(int longCount, int totalMillisWriting) {
        this.longCount = longCount;
        this.totalMillisWriting = totalMillisWriting;
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
        return benchmarkSeqWrite(this.longCount, this.totalMillisWriting);
    }

    private int benchmarkSeqWrite(int longCount, int totalMillisWriting) {
        int runs = 0;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisWriting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            long[] copyLongs = ArrayGenerators.generateLongs(longCount);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            for(int i = 0; i < longCount; i++){
                longs[i] = copyLongs[i];
            }
            runs++;
        }
        return runs;
    }
}
