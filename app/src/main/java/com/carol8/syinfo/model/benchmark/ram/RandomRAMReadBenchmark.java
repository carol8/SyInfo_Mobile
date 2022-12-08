package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

public class RandomRAMReadBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Random read";
    private final int longCount, totalMillisReading;
    private volatile long[] longs;

    public RandomRAMReadBenchmark(int longCount, int totalMillisReading) {
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
        return 1;
    }

    @Override
    public int run() {
        return benchmarkRndRead(this.longCount, this.totalMillisReading);
    }

    private int benchmarkRndRead(int longCount, int totalMillisWriting) {
        int runs = 0;
        long read;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisWriting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            longs = ArrayGenerators.generateLongs(longCount);
            int[] indexes = ArrayGenerators.generateRandomIndexes(longCount);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            for(int i = 0; i < longCount; i++){
                read = longs[indexes[i]];
            }
            runs++;
        }
        return runs;
    }
}
