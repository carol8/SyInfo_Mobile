package com.carol8.syinfo.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;
import com.carol8.syinfo.presenter.benchmark.BenchmarkAdapter;
import com.carol8.syinfo.presenter.benchmark.BenchmarksRunner;
import com.carol8.syinfo.model.benchmark.cpu.HashingBenchmark;
import com.carol8.syinfo.model.benchmark.cpu.MultithreadingPiBenchmark;
import com.carol8.syinfo.model.benchmark.cpu.StringSortingBenchmark;
import com.carol8.syinfo.model.benchmark.ram.RandomRAMReadBenchmark;
import com.carol8.syinfo.model.benchmark.ram.RandomRAMWriteBenchmark;
import com.carol8.syinfo.model.benchmark.ram.SequentialRAMReadBenchmark;
import com.carol8.syinfo.model.benchmark.ram.SequentialRAMWriteBenchmark;
import com.carol8.syinfo.model.benchmark.storage.RandomStorageReadBenchmark;
import com.carol8.syinfo.model.benchmark.storage.RandomStorageWriteBenchmark;
import com.carol8.syinfo.model.benchmark.storage.SequentialStorageReadBenchmark;
import com.carol8.syinfo.model.benchmark.storage.SequentialStorageWriteBenchmark;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BenchmarksFragment extends Fragment {
    enum State {
        STOPPED,
        RUNNING
    }

    private final Map<String, Double> categoryMultipliers = Map.of(
            "CPU", 1.0
            ,"RAM", 1.0
            ,"Storage", 1.0
    );

    private final Map<String, Double> benchmarkMultipliers = Map.ofEntries(
            Map.entry("Hashing (CPU)", 1.0)
            , Map.entry("Multithreading (CPU)", 1.0)
            , Map.entry("String sorting (CPU)", 1.0)
            , Map.entry("Random read (RAM)", 1.0)
            , Map.entry("Random write (RAM)", 1.0)
            , Map.entry("Sequential read (RAM)", 1.0)
            , Map.entry("Sequential write (RAM)", 1.0)
            , Map.entry("Random read (Storage)", 1.0)
            , Map.entry("Random write (Storage)", 1.0)
            , Map.entry("Sequential read (Storage)", 1.0)
            , Map.entry("Sequential write (Storage)", 1.0)
    );

    private State state = State.STOPPED;
    private ExecutorService executorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_benchmarks, container, false);
        BenchmarkAdapter benchmarkAdapter = new BenchmarkAdapter();

        RecyclerView recyclerView = v.findViewById(R.id.benchmarksRecyclerView);
        recyclerView.setAdapter(benchmarkAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ProgressBar progressBar = v.findViewById(R.id.benchmarksProgressBar);
        TextView progressText = v.findViewById(R.id.benchmarksProgressTextView);

        Button benchmarkSelectAllButton = v.findViewById(R.id.benchmarksSelectAllButton);
        benchmarkSelectAllButton.setOnClickListener(v1 -> benchmarkAdapter.selectAllCheckboxes());

        Button benchmarkDeselectAllButton = v.findViewById(R.id.benchmarksDeselectAllButton);
        benchmarkDeselectAllButton.setOnClickListener(v1 -> benchmarkAdapter.deselectAllCheckboxes());

        Button benchmarksStartButton = v.findViewById(R.id.benchmarksStartButton);
        benchmarksStartButton.setOnClickListener(v1 -> {
            if(benchmarkAdapter.getBenchmarksSelected().size() == 0){
                Toast.makeText(getContext(), "Cannot run, no benchmarks selected...", Toast.LENGTH_SHORT).show();
            } else if (state == State.STOPPED) {
                state = State.RUNNING;
                benchmarksStartButton.setText(R.string.benchmark_start_button_running);
                benchmarksStartButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.benchmark_start_button_running));

                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        if (state != State.STOPPED) {
                            switch (msg.what) {
                                case BenchmarksRunner.SET_PROGRESS:
                                    progressText.setVisibility(View.VISIBLE);
                                    progressText.setText((String) msg.obj);
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(msg.arg1);
                                    break;
                                case BenchmarksRunner.SET_PROGRESS_TEXT:
                                    progressText.setText((String) msg.obj);
                                    break;
                                case BenchmarksRunner.INCREMENT_PROGRESS:
                                    progressBar.incrementProgressBy(msg.arg1);
                                    break;
                                case BenchmarksRunner.DISPLAY_RESULTS:
                                    progressText.setText("All benchmarks done!");
                                    progressBar.setProgress(progressBar.getMax());

                                    state = State.STOPPED;
                                    benchmarksStartButton.setText(R.string.benchmark_start_button_stopped);
                                    benchmarksStartButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.benchmark_start_button_stopped));

                                    Intent switchToResultActivity = new Intent(getContext(), ResultActivity.class);
                                    Bundle args = new Bundle();
                                    args.putSerializable("resultList", (Serializable) msg.obj);
                                    switchToResultActivity.putExtra("bundle", args);
                                    startActivity(switchToResultActivity);
                                    break;
                            }
                        }
                    }
                };

                BenchmarksRunner benchmarksRunner = new BenchmarksRunner(handler, benchmarkAdapter.getBenchmarksSelected(), benchmarkMultipliers, categoryMultipliers);
                executorService = Executors.newSingleThreadExecutor();
                executorService.submit(benchmarksRunner::runBenchmarks);
                progressBar.setMax(benchmarkAdapter.getBenchmarksSelected().size());
            } else {
                state = State.STOPPED;
                benchmarksStartButton.setText(R.string.benchmark_start_button_stopped);
                benchmarksStartButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.benchmark_start_button_stopped));

                progressText.setText("Benchmarks stopped");
                progressBar.setProgress(0);

                executorService.shutdownNow();
            }
        });

        benchmarkAdapter.addBenchmark(new HashingBenchmark(1000, 1000, 1000));
        benchmarkAdapter.addBenchmark(new MultithreadingPiBenchmark(-1, 100, 1000));
        benchmarkAdapter.addBenchmark(new StringSortingBenchmark(1000, 25, 1000));
        benchmarkAdapter.addBenchmark(new RandomRAMReadBenchmark(1048576, 200));
        benchmarkAdapter.addBenchmark(new RandomRAMWriteBenchmark(1048576, 200));
        benchmarkAdapter.addBenchmark(new SequentialRAMReadBenchmark(1048576, 200));
        benchmarkAdapter.addBenchmark(new SequentialRAMWriteBenchmark(1048576, 200));
        benchmarkAdapter.addBenchmark(new RandomStorageReadBenchmark(16 * 1024, 1, 1000, getContext()));
        benchmarkAdapter.addBenchmark(new RandomStorageWriteBenchmark(16 * 1024, 1, 1000, getContext()));
        benchmarkAdapter.addBenchmark(new SequentialStorageReadBenchmark(64 * 1024 * 1024, 1000, getContext()));
        benchmarkAdapter.addBenchmark(new SequentialStorageWriteBenchmark(64 * 1024 * 1024, 10000, getContext()));

        return v;
    }
}
