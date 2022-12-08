package com.carol8.syinfo.model.information;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;

import com.carol8.syinfo.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RAMInformation implements ComponentInformation{
    private final Item tag;
    private final List<Item> informations;

    public RAMInformation(Context context) {
        this.tag = new Item("RAM", new ArrayList<>());
        this.informations = getRAMInfo(context);
    }

    @Override
    public Item getTag() {
        return this.tag;
    }

    @Override
    public List<Item> getInformations() {
        return this.informations;
    }

    private List<Item> getRAMInfo(Context context){
        List<Item> result = new ArrayList<>();

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        double totalMegs = (double)memoryInfo.totalMem / 0x100000L;
        double availableMegs = (double) memoryInfo.availMem / 0x100000L;
        double percentAvail = memoryInfo.availMem / (double) memoryInfo.totalMem * 100.0;
        result.add(new Item("Total RAM", List.of(Math.round(totalMegs) + " MB")));
        result.add(new Item("Availiable RAM", List.of(Math.round(availableMegs) + " MB (" + Math.round(percentAvail) + "%)")));

        return result;
    }
}
