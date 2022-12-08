package com.carol8.syinfo.model.information;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import com.carol8.syinfo.model.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SystemInformation implements ComponentInformation {
    private final Item tag;
    private final List<Item> informations;

    public SystemInformation(Context context) {
        this.tag = new Item("System Information", new ArrayList<>());
        this.informations = getSystemInfo(context);
    }

    @Override
    public Item getTag() {
        return tag;
    }

    @Override
    public List<Item> getInformations() {
        return informations;
    }

    private List<Item> getSystemInfo(Context context){
        List<Item> result = new ArrayList<>();

        result.add(new Item("Board", List.of(Build.BOARD)));
        result.add(new Item("Bootloader", List.of(Build.BOOTLOADER)));
        result.add(new Item("Brand", List.of(Build.BRAND)));
        result.add(new Item("Codename", List.of(Build.DEVICE)));
        result.add(new Item("Display", List.of(Build.DISPLAY)));
        result.add(new Item("Fingerprint", List.of(Build.FINGERPRINT)));
        result.add(new Item("Hardware", List.of(Build.HARDWARE)));
        result.add(new Item("Host", List.of(Build.HOST)));
        result.add(new Item("ID", List.of(Build.ID)));
        result.add(new Item("Manifacturer", List.of(Build.MANUFACTURER)));
        result.add(new Item("Model", List.of(Build.MODEL)));
        result.add(new Item("Product", List.of(Build.PRODUCT)));
        result.add(new Item("Radio", List.of(Build.getRadioVersion().split(",")[0])));
        result.add(new Item("Tags", List.of(Build.TAGS)));
        result.add(new Item("OS Author", List.of(Build.USER)));

        Date date = new Date(Build.TIME);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        result.add(new Item("OS Build date", List.of(format.format(date))));

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        result.add(new Item("Screen size", List.of(metrics.widthPixels + " x " + metrics.heightPixels)));
        result.add(new Item("Screen density", List.of((int)Math.ceil(metrics.xdpi) + " dpi")));
        double xInch = metrics.widthPixels / metrics.xdpi;
        double yInch = metrics.heightPixels / metrics.ydpi;
        result.add(new Item("Screen Size", List.of(String.format(Locale.getDefault(),"%.2f", Math.sqrt(xInch * xInch + yInch * yInch)) + " inches")));

        Collections.sort(result);
        return result;
    }
}
