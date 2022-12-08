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
    private final int totalMillisEncrypting;

    public HashingBenchmark(int messageSize, int keySize, int totalMillisEncrypting){
        this.messageSize = messageSize;
        this.keySize = keySize;
        this.totalMillisEncrypting = totalMillisEncrypting;
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
    public int run() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return benchmarkHashing(this.messageSize, this.keySize, this.totalMillisEncrypting);
    }

    private int benchmarkHashing(int messageSize, int keySize, int totalMillisEncrypting) throws NoSuchAlgorithmException, InvalidKeyException {
        int runs = 0;
        long initialGenerateTime;
        long initialTime = System.currentTimeMillis();
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec;
        byte[] key, message;

        while(initialTime + totalMillisEncrypting > System.currentTimeMillis()) {
            initialGenerateTime = System.currentTimeMillis();
            message = ArrayGenerators.generateBytes(messageSize);
            key = ArrayGenerators.generateBytes(keySize);
            initialTime += System.currentTimeMillis() - initialGenerateTime;
//            Log.d("status", "encrypting, run " + runs + ", time remaining " + (initialTime + totalMillisEncrypting - System.currentTimeMillis()));
            secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            sha256HMAC.doFinal(message);

            runs++;
        }
        return runs;
    }
}
