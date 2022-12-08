package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

public class RandomRAMWriteBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Random write";
    private final int longCount, totalMillisWriting;
    private volatile long[] longs;

    public RandomRAMWriteBenchmark(int longCount, int totalMillisWriting) {
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
        return 1;
    }

    @Override
    public int run() {
        return benchmarkRndWrite(this.longCount, this.totalMillisWriting);
    }

    private int benchmarkRndWrite(int longCount, int totalMillisWriting) {
        int runs = 0;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisWriting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            long[] copyLongs = ArrayGenerators.generateLongs(longCount);
            int[] indexes = ArrayGenerators.generateRandomIndexes(longCount);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
            for(int i = 0; i < longCount; i++){
                longs[indexes[i]] = copyLongs[i];
            }
            runs++;
        }
        return runs;
    }
}
