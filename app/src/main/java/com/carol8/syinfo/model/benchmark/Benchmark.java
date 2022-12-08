package com.carol8.syinfo.model.benchmark;

public interface Benchmark extends Comparable<Benchmark> {
    String getName();
    String getCategory();
    int getMultipler();
    int run() throws Exception;

    default String getFullName(){
        return this.getName() + " (" + this.getCategory() + ")";
    }

    @Override
    default int compareTo(Benchmark o) {
        if (this.getCategory().compareTo(o.getCategory()) != 0){
            return this.getCategory().compareTo(o.getCategory());
        }
        else{
            return this.getName().compareTo(o.getName());
        }
    }
}
