package com.angelinaprogress.intellij.model;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.util.Map;

/** The three selectable Angelina progress-bar animations. */
public enum Angelina {
    BROOM_RIDE("broom-ride", "Broom Ride", "broom.svg", -29, 32,
        9, 223, 950, 591, "#F4BDC9", "#EE99AC", "#9B6470"),
    DELIVERY_RUN("delivery-run", "Delivery Run", "run.svg", -21, 32,
        141, 218, 802, 700, "#C6C6A7", "#A8A878", "#6D6D4E"),
    DIVING("diving", "Diving", "dive.svg", -22, 32,
        37, 136, 926, 710, "#9DB7F5", "#6890F0", "#445E9C");

    private static final Map<String, Angelina> BY_ID = Map.of(
        BROOM_RIDE.id, BROOM_RIDE,
        DELIVERY_RUN.id, DELIVERY_RUN,
        DIVING.id, DIVING);

    private final String id;
    private final String name;
    private final String resourceName;
    private final int xShift;
    private final int height;
    private final int cropX;
    private final int cropY;
    private final int cropWidth;
    private final int cropHeight;
    private final Color[] colors;

    Angelina(final String id, final String name, final String resourceName,
            final int xShift, final int height,
            final int cropX, final int cropY, final int cropWidth, final int cropHeight,
            final String light, final String middle, final String dark) {
        this.id = id;
        this.name = name;
        this.resourceName = resourceName;
        this.xShift = xShift;
        this.height = height;
        this.cropX = cropX;
        this.cropY = cropY;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.colors = new Color[]{Color.decode(light), Color.decode(middle), Color.decode(dark)};
    }

    public static Angelina getById(final String id) {
        return BY_ID.getOrDefault(id, BROOM_RIDE);
    }

    public Paint getPaint(final int height) {
        return new LinearGradientPaint(0, 2, 0, height - 2,
            new float[]{0, 0.5f, 1}, colors);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResourceName() {
        return resourceName;
    }

    public int getXShift() {
        return xShift;
    }

    public int getHeight() {
        return height;
    }

    public int getCropX() {
        return cropX;
    }

    public int getCropY() {
        return cropY;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    @Override
    public String toString() {
        return name;
    }
}
