package com.carol8.syinfo.model.benchmark.cpu;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.Arrays;

public class StringSortingBenchmark implements Benchmark {
    private final String category = "CPU";
    private final String name = "String sorting";
    private final int stringCount, stringSize, totalNanosSorting;

    public StringSortingBenchmark(int stringCount, int stringSize, int totalMillisSorting) {
        this.stringCount = stringCount;
        this.stringSize = stringSize;
        this.totalNanosSorting = totalMillisSorting * 1000000;
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
        return benchmarkSort(this.stringCount, this.stringSize, this.totalNanosSorting);
    }

    private int benchmarkSort(int stringCount, int stringSize, int totalNanosSorting) {
        int runs = 0;
        long initialGenerateTime;
        long initialTime = System.nanoTime();

        while(initialTime + totalNanosSorting > System.nanoTime()) {
            initialGenerateTime = System.nanoTime();
            String[] strings = ArrayGenerators.generateStrings(stringCount, stringSize);
            initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "sorting, run " + runs + ", time remaining " + (initialTime + totalMillisSorting - System.currentTimeMillis()));
            Arrays.sort(strings);
            runs++;
        }
        return runs;
    }
}
