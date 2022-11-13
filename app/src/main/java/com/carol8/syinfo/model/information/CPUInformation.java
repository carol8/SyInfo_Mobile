package com.carol8.syinfo.model.information;

import com.carol8.syinfo.model.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPUInformation implements ComponentInformation {
    private final String PROC_CPUINFO = "/proc/cpuinfo";
    private final String CPU_PRESENT = "/sys/devices/system/cpu/present";
    private final String CPU_ONLINE = "/sys/devices/system/cpu/online";
    private final String[] CPU_FREQ = {"/sys/devices/system/cpu/cpu", "/cpufreq/"};
    private final Map<String, String> CPU_FREQ_TYPES = Map.of("min", "cpuinfo_min_freq"
            ,"max", "cpuinfo_max_freq"
            ,"tis", "stats/time_in_state"
            ,"sched", "scaling_governor");

    private final double TO_GHZ = 1e6;

    private final Item tag;
    private final List<Item> informations;

    private static final Map<Integer, Map<Integer, Integer>> lastTises = new HashMap<>();

    public CPUInformation() {
        this.tag = new Item("CPU", new ArrayList<>());
        this.informations = getCPUInfo();
    }

    private List<Item> getCPUInfo(){
        List<Item> result = new ArrayList<>();

        try {
            result.addAll(getCPUInfoProc());
            result.addAll(getCPUInfoCores());
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private List<Item> getCPUInfoProc() throws IOException {
        List<Item> result = new ArrayList<>();
        String read;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PROC_CPUINFO));

        while((read = bufferedReader.readLine()) != null){
            String[] pair = read.trim().split(":");
            if (pair.length > 1) {
                switch (pair[0].trim()){
                    case "Hardware":
                    case "Processor":
                        result.add(new Item(pair[0].trim(), List.of(pair[1].trim())));
                }
            }
        }

        return result;
    }

    private List<Item> getCPUInfoCores() throws IOException {
        boolean tisFound;
        boolean[] processors = new boolean[0];
        List<Item> result = new ArrayList<>();
        String read;
        String[] split;
        double minfreq, maxfreq, currentFreq;
        String scheduler;
        Map<Integer, Integer> tisMap, deltaTisMap;
        BufferedReader bufferedReaderMin;
        BufferedReader bufferedReaderMax;
        BufferedReader bufferedReaderTIS;
        BufferedReader bufferedReaderSched;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(CPU_PRESENT));
        while((read = bufferedReader.readLine()) != null){
            processors = new boolean[Integer.parseInt(read.split("-")[1]) + 1];
        }
        result.add(new Item("Cores", List.of(String.valueOf(processors.length))));

        bufferedReader = new BufferedReader(new FileReader(CPU_ONLINE));
        while((read = bufferedReader.readLine()) != null){
            for(String s : read.split(",")){
                split = s.split("-");
                if(split.length == 2){
                    for(int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++){
                        processors[i] = true;
                    }
                }
                else{
                    processors[Integer.parseInt(split[0])] = true;
                }
            }
        }

        result.add(new Item("Core number \\ Property", List.of("Status", "Min freq", "Max freq", "Current freq", "Scheduler")));

        for(int i = 0; i < processors.length; i++){
            bufferedReaderMin = new BufferedReader(new FileReader(CPU_FREQ[0] + i + CPU_FREQ[1] + CPU_FREQ_TYPES.get("min")));
            bufferedReaderMax = new BufferedReader(new FileReader(CPU_FREQ[0] + i + CPU_FREQ[1] + CPU_FREQ_TYPES.get("max")));
            bufferedReaderTIS = new BufferedReader(new FileReader(CPU_FREQ[0] + i + CPU_FREQ[1] + CPU_FREQ_TYPES.get("tis")));
            bufferedReaderSched = new BufferedReader(new FileReader(CPU_FREQ[0] + i + CPU_FREQ[1] + CPU_FREQ_TYPES.get("sched")));

            minfreq = Double.parseDouble(bufferedReaderMin.readLine()) / TO_GHZ;
            maxfreq = Double.parseDouble(bufferedReaderMax.readLine()) / TO_GHZ;
            scheduler = bufferedReaderSched.readLine().trim();

            tisMap = new HashMap<>();
            deltaTisMap = new HashMap<>();
            while((read = bufferedReaderTIS.readLine()) != null) {
                split = read.trim().split(" ");
                tisMap.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }

            tisFound = false;
            for(Map.Entry<Integer, Map<Integer, Integer>> lastTisMap : lastTises.entrySet()){
                if(lastTisMap.getKey() == i){
                    tisFound = true;
                    for(Map.Entry<Integer, Integer> tis : tisMap.entrySet()){
                        deltaTisMap.put(tis.getValue() - lastTisMap.getValue().get(tis.getKey()), tis.getKey());
                    }
                    for(Map.Entry<Integer, Integer> tis : tisMap.entrySet()){
                        lastTisMap.getValue().replace(tis.getKey(), tis.getValue());
                    }
                }
            }
            if(!tisFound){
                lastTises.put(i, tisMap);
                for(Map.Entry<Integer, Integer> tis : tisMap.entrySet()){
                    deltaTisMap.put(tis.getValue(), tis.getKey());
                }
            }
            currentFreq = ((float) deltaTisMap.get(deltaTisMap.keySet().stream().max(Integer::compare).get()))/TO_GHZ;
            result.add(new Item("Core " + i, List.of(processors[i] ? "Active" : "Inactive"
                                                        , String.valueOf(minfreq)
                                                        , String.valueOf(maxfreq)
                                                        , String.valueOf(currentFreq)
                                                        , scheduler)));
        }

        return result;
    }

    @Override
    public Item getTag() {
        return this.tag;
    }

    @Override
    public List<Item> getInformations() {
        return this.informations;
    }
}
