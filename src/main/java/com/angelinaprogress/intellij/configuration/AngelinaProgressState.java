package com.angelinaprogress.intellij.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.angelinaprogress.intellij.model.Angelina;
import org.jetbrains.annotations.NotNull;

@State(
    name = "com.angelinaprogress.intellij.configuration.AngelinaProgressState",
    storages = {@Storage("AngelinaProgress.xml")}
)
public class AngelinaProgressState implements PersistentStateComponent<AngelinaProgressState> {
    public String version;

    public float initialVelocity = 1.0f;
    public float acceleration = 0.4f;

    public String selectedCharacter = Angelina.BROOM_RIDE.getId();
    public boolean drawSprites = true;
    public boolean addToolTips = true;
    public boolean transparencyOnIndeterminate = true;
    public boolean transparencyOnDeterminate = false;
    public boolean showUpdateNotification = false;

    public boolean restrictMaximumHeight = false;
    public int maximumHeight = 20;
    public boolean restrictMinimumHeight = false;
    public int minimumHeight = 20;

    public void setHeightLimits(final int newMaxHeight, final int newMinHeight) {
        if (newMinHeight > newMaxHeight) {
            minimumHeight = newMaxHeight;
            maximumHeight = newMaxHeight;
        } else {
            minimumHeight = newMinHeight;
            maximumHeight = newMaxHeight;
        }
    }

    public static AngelinaProgressState getInstance() {
        return ApplicationManager.getApplication().getService(AngelinaProgressState.class);
    }

    @Override
    public AngelinaProgressState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull final AngelinaProgressState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
