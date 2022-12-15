package com.carol8.syinfo.presenter.benchmark;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.carol8.syinfo.model.Item;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BenchmarksRunner {
    public static final int SET_PROGRESS = 0;
    public static final int SET_PROGRESS_TEXT = 1;
    public static final int INCREMENT_PROGRESS = 2;
    public static final int DISPLAY_RESULTS = 3;

    private final Handler handler;
    private final List<Benchmark> benchmarksSelected;

    private final Map<String, Double> benchmarkMultipliers;
    private final Map<String, Double> categoryMultipliers;

    public BenchmarksRunner(Handler handler, List<Benchmark> benchmarksSelected, Map<String, Double> benchmarkMultipliers, Map<String, Double> categoryMultipliers) {
        this.handler = handler;
        this.benchmarksSelected = benchmarksSelected;
        this.benchmarkMultipliers = benchmarkMultipliers;
        this.categoryMultipliers = categoryMultipliers;
    }

    public void runBenchmarks() {
        List<Item> results = new ArrayList<>();
        String category = null;
        int insertIndex = 0, categoriesCount = 0;
        BigDecimal benchmarkScore, adjustedBenchmarkScore, categoryScore = BigDecimal.ZERO, totalScore = BigDecimal.ZERO;
        DecimalFormat decimalFormat = new DecimalFormat("#");

        Message message = handler.obtainMessage();
        message.what = SET_PROGRESS;
        message.obj = "Starting";
        message.arg1 = 0;
        handler.sendMessage(message);

        for (Benchmark benchmark : benchmarksSelected) {
            message = handler.obtainMessage();
            message.what = SET_PROGRESS_TEXT;
            message.obj = benchmark.getFullName();
            handler.sendMessage(message);

            try {
                benchmarkScore = new BigDecimal(String.valueOf(benchmark.run()));
                adjustedBenchmarkScore = benchmarkScore
                        .multiply(BigDecimal.valueOf(categoryMultipliers.get(benchmark.getCategory())))
                        .multiply(BigDecimal.valueOf(benchmarkMultipliers.get(benchmark.getFullName())));
                Log.d("BENCHMARK" , benchmark.getFullName() + ": " + adjustedBenchmarkScore);
                totalScore = totalScore.add(adjustedBenchmarkScore);
                if (category == null) {
                    category = benchmark.getCategory();
                    insertIndex = 0;
                    categoryScore = categoryScore.add(adjustedBenchmarkScore);
                    categoriesCount++;
                } else if (!category.equals(benchmark.getCategory())) {
                    results.add(insertIndex, new Item(category, List.of(decimalFormat.format(categoryScore))));
                    categoryScore = adjustedBenchmarkScore;
                    insertIndex = benchmarksSelected.indexOf(benchmark) + categoriesCount;
                    category = benchmark.getCategory();
                    categoriesCount++;
                } else {
                    categoryScore = categoryScore.add(adjustedBenchmarkScore);
                }
                if(benchmark.getName().contains("Sequential") && benchmark.getCategory().equals("RAM")){
                    BigDecimal adjustedMBps = benchmarkScore.multiply(BigDecimal.valueOf(benchmark.getMultipler()).multiply(BigDecimal.valueOf(1)).divide(BigDecimal.valueOf(1048576)));
                    results.add(new Item(benchmark.getFullName(), List.of(decimalFormat.format(adjustedMBps) + " MB/s")));
                }
                else {
                    results.add(new Item(benchmark.getFullName(), List.of(decimalFormat.format(adjustedBenchmarkScore))));
                }
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                results.add(new Item(benchmark.getFullName(), List.of(e.toString())));
            }

            message = handler.obtainMessage();
            message.what = INCREMENT_PROGRESS;
            message.arg1 = 1;
            handler.sendMessage(message);
        }

        results.add(insertIndex, new Item(category, List.of(decimalFormat.format(categoryScore))));
        results.add(0, new Item("Total score", List.of(decimalFormat.format(totalScore))));

        message = handler.obtainMessage();
        message.what = DISPLAY_RESULTS;
        message.obj = results;
        handler.sendMessage(message);
    }

    public static BigInteger bigIntSqRootFloor(BigInteger x) throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }
        if (x .equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
            return x;
        }
        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        for (y = x.divide(two);
             y.compareTo(x.divide(y)) > 0;
             y = ((x.divide(y)).add(y)).divide(two));
        return y;
    }
}
