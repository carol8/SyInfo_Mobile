package com.carol8.syinfo.model.benchmark.cpu;

import com.carol8.syinfo.model.benchmark.ArrayGenerators;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class HashingBenchmark implements Benchmark {
    private final String category = "CPU";
    private final String name = "Hashing";
    private final int messageSize;
    private final int keySize;
    private final int totalNanosEncrypting;

    public HashingBenchmark(int messageSize, int keySize, int totalMillisEncrypting){
        this.messageSize = messageSize;
        this.keySize = keySize;
        this.totalNanosEncrypting = totalMillisEncrypting * 1000000;
    }

    @Override
    public String getName() {
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
    public int run() throws NoSuchAlgorithmException, InvalidKeyException {
        return benchmarkHashing(this.messageSize, this.keySize, this.totalNanosEncrypting);
    }

    private int benchmarkHashing(int messageSize, int keySize, int totalNanosEncrypting) throws NoSuchAlgorithmException, InvalidKeyException {
        int runs = 0;
        long initialGenerateTime;
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec;
        byte[] key, message;
        long initialTime = System.nanoTime();

        while(initialTime + totalNanosEncrypting > System.nanoTime()) {
            initialGenerateTime = System.nanoTime();
            message = ArrayGenerators.generateBytes(messageSize);
            key = ArrayGenerators.generateBytes(keySize);
            initialTime += System.nanoTime() - initialGenerateTime;
//            Log.d("status", "encrypting, run " + runs + ", time remaining " + (initialTime + totalMillisEncrypting - System.currentTimeMillis()));
            secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            sha256HMAC.doFinal(message);

            runs++;
        }
        return runs;
    }
}
