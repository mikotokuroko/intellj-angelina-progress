package com.angelinaprogress.intellij.configuration;

import com.intellij.openapi.options.Configurable;
import java.util.Objects;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;

public class AngelinaProgressConfigurable implements Configurable {
    private AngelinaProgressConfigurationComponent component;

    @Nls
    @Override
    public String getDisplayName() {
        return "Angelina Progress Bar";
    }

    @Override
    public JComponent createComponent() {
        final AngelinaProgressState state = AngelinaProgressState.getInstance();
        component = new AngelinaProgressConfigurationComponent(state);
        return component.getPanel();
    }

    @Override
    public boolean isModified() {
        final AngelinaProgressState state = AngelinaProgressState.getInstance();
        return component != null && (!Objects.equals(state.selectedCharacter, component.getSelectedCharacterId())
            || state.drawSprites != component.getDrawSprites().isSelected()
            || state.addToolTips != component.getAddToolTips().isSelected()
            || state.transparencyOnIndeterminate != component.getIndeterminateTransparency().isSelected()
            || state.transparencyOnDeterminate != component.getDeterminateTransparency().isSelected()
            || state.initialVelocity != component.getInitialVelocity().getValue() / 100f
            || state.acceleration != component.getAcceleration().getValue() / 100f
            || state.showUpdateNotification != component.getShowUpdateNotification().isSelected()
            || state.restrictMaximumHeight != component.getRestrictMaxHeight().isSelected()
            || state.maximumHeight != component.getMaxHeight().getValue()
            || state.restrictMinimumHeight != component.getRestrictMinHeight().isSelected()
            || state.minimumHeight != component.getMinHeight().getValue());
    }

    @Override
    public void apply() {
        final AngelinaProgressState state = AngelinaProgressState.getInstance();
        state.selectedCharacter = component.getSelectedCharacterId();
        state.drawSprites = component.getDrawSprites().isSelected();
        state.addToolTips = component.getAddToolTips().isSelected();
        state.transparencyOnIndeterminate = component.getIndeterminateTransparency().isSelected();
        state.transparencyOnDeterminate = component.getDeterminateTransparency().isSelected();
        state.initialVelocity = component.getInitialVelocity().getValue() / 100f;
        state.acceleration = component.getAcceleration().getValue() / 100f;
        state.showUpdateNotification = component.getShowUpdateNotification().isSelected();
        state.restrictMaximumHeight = component.getRestrictMaxHeight().isSelected();
        state.restrictMinimumHeight = component.getRestrictMinHeight().isSelected();
        state.setHeightLimits(component.getMaxHeight().getValue(), component.getMinHeight().getValue());
    }

    @Override
    public void reset() {
        final AngelinaProgressState state = AngelinaProgressState.getInstance();
        component.updateUi(state);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }
}
