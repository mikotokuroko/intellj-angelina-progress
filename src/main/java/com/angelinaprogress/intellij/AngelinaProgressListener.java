package com.angelinaprogress.intellij;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import java.util.Objects;
import java.util.Optional;
import javax.swing.UIManager;
import org.jetbrains.annotations.NotNull;

public class AngelinaProgressListener implements LafManagerListener, DynamicPluginListener {
    private static final String PLUGIN_ID_STRING = "com.angelinaprogress.intellij";
    private static final String PROGRESS_BAR_UI_KEY = "ProgressBarUI";
    private static final String ANGELINA_PROGRESS_BAR_UI_IMPLEMENTATION_NAME = AngelinaProgressBarUi.class.getName();
    private volatile static Object previousProgressBar = null;
    private volatile static PluginId pluginId = null;
    private static boolean initialized = false;

    public AngelinaProgressListener() {
        initializePlugin();
    }

    private void initializePlugin() {
        if (!initialized) {
            updateProgressBarUi();
            pluginId = PluginId.getId(PLUGIN_ID_STRING);
            initialized = true;
        }
    }

    @Override
    public void lookAndFeelChanged(@NotNull final LafManager lafManager) {
        updateProgressBarUi();
    }

    @Override
    public void pluginLoaded(@NotNull final IdeaPluginDescriptor pluginDescriptor) {
        if (Objects.equals(pluginId, pluginDescriptor.getPluginId())) {
            updateProgressBarUi();
        }
    }

    @Override
    public void beforePluginUnload(@NotNull final IdeaPluginDescriptor pluginDescriptor, final boolean isUpdate) {
        if (Objects.equals(pluginId, pluginDescriptor.getPluginId())) {
            resetProgressBarUi();
        }
    }

    static void updateProgressBarUi() {
        ApplicationManager.getApplication().invokeLater(() -> {
            final Object prev = UIManager.get(PROGRESS_BAR_UI_KEY);
            if (!Objects.equals(ANGELINA_PROGRESS_BAR_UI_IMPLEMENTATION_NAME, prev)) {
                previousProgressBar = prev;
            }
            UIManager.put(PROGRESS_BAR_UI_KEY, ANGELINA_PROGRESS_BAR_UI_IMPLEMENTATION_NAME);
            UIManager.getDefaults().put(ANGELINA_PROGRESS_BAR_UI_IMPLEMENTATION_NAME, AngelinaProgressBarUi.class);
        });
    }

    static void resetProgressBarUi() {
        ApplicationManager.getApplication().invokeLater(() -> {
            UIManager.put(PROGRESS_BAR_UI_KEY, previousProgressBar);
        });
    }

    public static IdeaPluginDescriptor getPluginDescriptor() {
        return Optional.ofNullable(pluginId)
            .map(PluginManagerCore::getPlugin)
            .orElseGet(() -> PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID_STRING)));
    }
}
