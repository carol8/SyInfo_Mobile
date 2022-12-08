package com.carol8.syinfo.model.benchmark.cpu;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.Arrays;

public class StringSortingBenchmark implements Benchmark {
    private final String category = "CPU";
    private final String name = "String sorting";
    private final int stringCount, stringSize, totalMillisSorting;

    public StringSortingBenchmark(int stringCount, int stringSize, int totalMillisSorting) {
        this.stringCount = stringCount;
        this.stringSize = stringSize;
        this.totalMillisSorting = totalMillisSorting;
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
        return benchmarkSort(this.stringCount, this.stringSize, this.totalMillisSorting);
    }

    private int benchmarkSort(int stringCount, int stringSize, int totalMillisSorting) {
        int runs = 0;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();

        while(initialTime + totalMillisSorting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            String[] strings = ArrayGenerators.generateStrings(stringCount, stringSize);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "sorting, run " + runs + ", time remaining " + (initialTime + totalMillisSorting - System.currentTimeMillis()));
            Arrays.sort(strings);
            runs++;
        }
        return runs;
    }
}
