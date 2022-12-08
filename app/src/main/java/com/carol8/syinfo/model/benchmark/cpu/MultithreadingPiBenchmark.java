package com.carol8.syinfo.model.benchmark.cpu;

import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadingPiBenchmark implements Benchmark {
    private final String category = "CPU";
    private final String name = "Multithreading";
    private final int threadCount, infinity, totalMillisCalculating;

    public MultithreadingPiBenchmark(int threadCount, int infinity, int totalMillisCalculating){
        this.threadCount = threadCount;
        this.infinity = infinity;
        this.totalMillisCalculating = totalMillisCalculating;
    }

    @Override
    public String getName() {
        return name;
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
    public int run() throws ExecutionException, InterruptedException {
        return benchmarkPi(this.threadCount, this.infinity, this.totalMillisCalculating);
    }

    private int benchmarkPi(int threadCount, int infinity, int totalMillisCalculating) throws ExecutionException, InterruptedException {
        int totalRuns = 0;
        List<Future<Integer>> futureList = new ArrayList<>();
        if(threadCount == -1){
            threadCount = Runtime.getRuntime().availableProcessors();
        }
        for(int i = 0; i < threadCount; i++){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            int finalI = i;
            futureList.add(executorService.submit(() -> {
                int runs = 0;
                long initialTime = System.currentTimeMillis();
                while(initialTime + totalMillisCalculating > System.currentTimeMillis()) {
//                    Log.d("status", "pi, thread " + finalI + ", run " + runs + ", time remaining " + (initialTime + totalMillisCalculating - System.currentTimeMillis()));
                    nthPiDigit(runs, infinity);
                    runs++;
                }
                return runs;
            }));
        }
        for(int i = 0; i < threadCount; i++){
            totalRuns += futureList.get(0).get();
        }
        return totalRuns;
    }

    private double nthPiDigit(int n, int infinity){
        double sum = 4 * term(n, infinity, 1)
                - 2 * term(n, infinity, 4)
                - term(n, infinity, 5)
                - term(n, infinity, 6);
        return 16 * (sum - Math.floor(sum));
    }

    private double term(int n, int infinity, int offset){
        double sum = 0.0;
        for(int k = 0; k <= n; k++){
            sum += (double) modularExponentiation(16, n - k, 8 * k + offset) / (8 * k + offset);
        }
        for(int k = n + 1; k <= infinity; k++){
            sum += Math.pow(16, n - k) / (8 * k + offset);
        }
        return sum;
    }

    private int modularExponentiation(int base, int exponent, int modulus){
        if (modulus == 1){
            return 0;
        }
        int c = 1;
        for(int i = 0; i < exponent; i++){
            c = (c * base) % modulus;
        }
        return c;
    }
}
