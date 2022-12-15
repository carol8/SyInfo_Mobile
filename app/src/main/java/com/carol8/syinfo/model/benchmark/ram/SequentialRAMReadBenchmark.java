package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequentialRAMReadBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Sequential read";
    private final int threadCount, longCount, totalNanosReading;
    private volatile long[][] longs;

    public SequentialRAMReadBenchmark(int threadCount, int longCount, int totalMillisReading) {
        this.longCount = longCount;
        this.totalNanosReading = totalMillisReading * 1000000;
        if(threadCount == -1){
            this.threadCount = Runtime.getRuntime().availableProcessors();
        }
        else{
            this.threadCount = threadCount;
        }
        this.longs = new long[this.threadCount][longCount];
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
    public int run() throws ExecutionException, InterruptedException {
        return benchmarkSeqRead(this.longCount, this.totalNanosReading);
    }

    private int benchmarkSeqRead(int longCount, int totalNanosReading) throws ExecutionException, InterruptedException {
        int totalRuns = 0;
        List<Future<Integer>> futureList = new ArrayList<>();
        for(int i = 0; i < threadCount; i++){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            int finalI = i;
            futureList.add(executorService.submit(() -> {
                int runs = 0;
                long read;
                long initialGenerateTime;
                long initialTime = System.nanoTime();

                while(initialTime + totalNanosReading > System.nanoTime()) {
                    initialGenerateTime = System.nanoTime();
                    longs[finalI] = ArrayGenerators.generateLongs(longCount);
                    initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
                    for(int j = 0; j < longCount; j++){
                        read = longs[finalI][j];
                    }
                    runs++;
                }
                return runs;
            }));
        }
        for(int i = 0; i < threadCount; i++){
            totalRuns += futureList.get(i).get();
        }
        return totalRuns;
    }
}
