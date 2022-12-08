package com.carol8.syinfo.model.information;

import android.content.Context;

import com.carol8.syinfo.model.Item;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StorageInformation implements ComponentInformation{
    private final Item tag;
    private final List<Item> informations;

    public StorageInformation(Context context) {
        this.tag = new Item("Internal Storage", new ArrayList<>());
        this.informations = getStorageInfo(context);
    }

    @Override
    public Item getTag() {
        return this.tag;
    }

    @Override
    public List<Item> getInformations() {
        return this.informations;
    }

    private List<Item> getStorageInfo(Context context){
        List<Item> result = new ArrayList<>();

        File rootFile = new File(context.getFilesDir().getAbsoluteFile().toString());
        double totalSize = rootFile.getTotalSpace();
        double availableSize = rootFile.getFreeSpace();
        double percentageAvailable = availableSize / totalSize * 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        result.add(new Item("Total Storage\n(Excluding OS size)", List.of(decimalFormat.format(totalSize / 0x40000000L) + " GB")));
        result.add(new Item("Availiable Storage", List.of(decimalFormat.format(availableSize / 0x40000000L) + " GB (" + Math.round(percentageAvailable) + "%)")));

        return result;
    }
}
