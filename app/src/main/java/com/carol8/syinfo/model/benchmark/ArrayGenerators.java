package com.carol8.syinfo.model.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArrayGenerators {
    public static int[] generateRandomIndexes(int arraySize){
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < arraySize; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }

    public static long[] generateLongs(int size){
        long[] longs = new long[size];
        Random random = new Random();
        for(int i = 0; i < size; i++){
            longs[i] = random.nextLong();
        }
        return longs;
    }

    public static byte[] generateBytes(int size){
        Random random = new Random();
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String[] generateStrings(int numberOfStrings, int stringSize){
        int leftLimit = 48; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String[] generatedStrings = new String[numberOfStrings];
        for(int i = 0; i < numberOfStrings; i++) {
            generatedStrings[i] = random.ints(leftLimit, rightLimit + 1)
                    .limit(stringSize)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }

        return generatedStrings;
    }
}
