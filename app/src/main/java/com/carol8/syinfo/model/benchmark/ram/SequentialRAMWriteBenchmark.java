package com.carol8.syinfo.model.benchmark.ram;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequentialRAMWriteBenchmark implements Benchmark {
    private final String category = "RAM";
    private final String name = "Sequential write";
    private final int threadCount, longCount, totalNanosWriting;
    private volatile long[][] longs;

    public SequentialRAMWriteBenchmark(int threadCount, int longCount, int totalMillisWriting) {
        this.longCount = longCount;
        this.totalNanosWriting = totalMillisWriting * 1000000;
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
        return benchmarkSeqWrite(this.longCount, this.totalNanosWriting);
    }

    private int benchmarkSeqWrite(int longCount, int totalNanosWriting) throws ExecutionException, InterruptedException {
        int totalRuns = 0;
        List<Future<Integer>> futureList = new ArrayList<>();
        for(int i = 0; i < threadCount; i++){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            int finalI = i;
            futureList.add(executorService.submit(() -> {
                int runs = 0;
                long initialGenerateTime;
                long initialTime = System.nanoTime();

                while(initialTime + totalNanosWriting > System.nanoTime()) {
                    initialGenerateTime = System.nanoTime();
                    long[] copyLongs = ArrayGenerators.generateLongs(longCount);
                    initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "seq copy, run " + runs + ", time remaining " + (initialTime + totalMillisWriting - System.currentTimeMillis()));
                    for(int j = 0; j < longCount; j++){
                        longs[finalI][j] = copyLongs[j];
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
