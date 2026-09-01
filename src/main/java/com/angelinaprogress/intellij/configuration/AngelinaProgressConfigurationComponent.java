package com.angelinaprogress.intellij.configuration;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.components.DefaultLinkButtonUI;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.FormBuilder;
import com.angelinaprogress.intellij.AngelinaProgressBarUi;
import com.angelinaprogress.intellij.AngelinaProgressChangenotesDialog;
import com.angelinaprogress.intellij.AngelinaProgressListener;
import com.angelinaprogress.intellij.model.Angelina;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import org.jetbrains.annotations.NotNull;

public class AngelinaProgressConfigurationComponent {
    private JPanel mainPanel;
    final JLabel title = new JLabel("Angelina Progress Bar");
    final JProgressBar determinateProgressBar = new JProgressBar(0, 2);
    final JProgressBar indeterminateProgressBar = new JProgressBar();
    private AngelinaProgressBarUi determinateUi;
    private AngelinaProgressBarUi indeterminateUi;
    private final JComboBox<Angelina> character = new ComboBox<>(Angelina.values());
    private final JBCheckBox drawSprites = new JBCheckBox("Draw character");
    private final JBCheckBox addToolTips = new JBCheckBox("Add tool tips");
    private final JBCheckBox indeterminateTransparency = new JBCheckBox("Transparency on indeterminate");
    private final JBCheckBox determinateTransparency = new JBCheckBox("Transparency on determinate");
    private final JBCheckBox showUpdateNotification = new JBCheckBox("Show update notification");
    private final JSlider initialVelocity = new JSlider(1, 500, 100);
    private final JSlider acceleration = new JSlider(1, 500, 40);
    private final JBCheckBox restrictMaxHeight = new JBCheckBox("Restrict max height");
    private final JSlider maxHeight = new JSlider(8, 64, 20);
    private final JBCheckBox restrictMinHeight = new JBCheckBox("Restrict min height");
    private final JSlider minHeight = new JSlider(8, 64, 20);

    public AngelinaProgressConfigurationComponent(final AngelinaProgressState state) {
        createUi();
        updateUi(state);
    }

    void createUi() {
        final FormBuilder formBuilder = FormBuilder.createFormBuilder();
        formBuilder.addComponent(createTitlePanel());
        formBuilder.addVerticalGap(5);
        formBuilder.addLabeledComponent("Character", character, true);
        formBuilder.addLabeledComponent("Preview", createPreviewPanel(), true);
        formBuilder.addSeparator();
        formBuilder.addComponent(createIndeterminatePanel());
        formBuilder.addSeparator();
        formBuilder.addComponent(createCheckboxPanel());
        formBuilder.addComponent(createHeightPanel());

        character.addActionListener(event -> refreshPreview());
        mainPanel = formBuilder.getPanel();
    }

    private JPanel createTitlePanel() {
        final JButton changenotes = new JButton("Changenotes");
        changenotes.setUI(DefaultLinkButtonUI.createUI(changenotes));
        changenotes.addActionListener(a -> new AngelinaProgressChangenotesDialog(null).show());
        final JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        final GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        left.weightx = 0.5;
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        titlePanel.add(title, left);
        final GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.EAST;
        right.weightx = 0.5;
        titlePanel.add(changenotes, right);
        return titlePanel;
    }

    private JPanel createHeightPanel() {
        final JPanel heightPanel = new JPanel();
        heightPanel.setLayout(new GridLayout(2, 2));
        heightPanel.add(restrictMaxHeight);
        heightPanel.add(restrictMinHeight);
        heightPanel.add(maxHeight);
        heightPanel.add(minHeight);
        setupHeightConfig(restrictMaxHeight, maxHeight, "Restrict max height");
        setupHeightConfig(restrictMinHeight, minHeight, "Restrict min height");
        maxHeight.addChangeListener(c -> {
            if (maxHeight.getValue() < minHeight.getValue()) {
                minHeight.setValue(maxHeight.getValue());
            }
        });
        minHeight.addChangeListener(c -> {
            if (minHeight.getValue() > maxHeight.getValue()) {
                maxHeight.setValue(minHeight.getValue());
            }
        });
        return heightPanel;
    }

    private void setupHeightConfig(final JBCheckBox checkbox, final JSlider slider, final String text) {
        checkbox.addItemListener(c -> {
            if (c.getStateChange() == ItemEvent.SELECTED) {
                slider.setEnabled(true);
                checkbox.setText(text + ": " + slider.getValue() + "px");
                if (determinateUi != null) {
                    determinateUi.computeScaledIcons();
                }
                if (indeterminateUi != null) {
                    indeterminateUi.computeScaledIcons();
                }
            } else if (c.getStateChange() == ItemEvent.DESELECTED) {
                slider.setEnabled(false);
                checkbox.setText(text);
            }
            determinateProgressBar.setUI(determinateUi);
            indeterminateProgressBar.setUI(indeterminateUi);
        });
        slider.setEnabled(false);
        slider.addChangeListener(c -> {
            if (slider.isEnabled()) {
                checkbox.setText(text + ": " + slider.getValue() + "px");
            }
            if (determinateUi != null) {
                determinateUi.computeScaledIcons();
                determinateProgressBar.setUI(determinateUi);
            }
            if (indeterminateUi != null) {
                indeterminateUi.computeScaledIcons();
                indeterminateProgressBar.setUI(indeterminateUi);
            }
        });
    }

    @NotNull
    private JPanel createCheckboxPanel() {
        final JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(3, 2));

        checkboxPanel.add(drawSprites);
        drawSprites.setToolTipText("If disabled, progress bars show only the character color");
        checkboxPanel.add(indeterminateTransparency);

        checkboxPanel.add(determinateTransparency);
        checkboxPanel.add(addToolTips);
        addToolTips.setToolTipText("Whether to show the selected character name on progress bars");
        showUpdateNotification.setToolTipText("Turn on or off the notification when the plugin has been updated");
        checkboxPanel.add(showUpdateNotification);

        return checkboxPanel;
    }

    void updateUi(final AngelinaProgressState state) {
        if (state != null) {
            Optional.ofNullable(AngelinaProgressListener.getPluginDescriptor())
                .ifPresent(desc -> title.setText("Angelina Progress Bar " + desc.getVersion()));
            initialVelocity.setValue((int) (state.initialVelocity * 100));
            acceleration.setValue((int) (state.acceleration * 100));
            drawSprites.setSelected(state.drawSprites);
            addToolTips.setSelected(state.addToolTips);
            indeterminateTransparency.setSelected(state.transparencyOnIndeterminate);
            determinateTransparency.setSelected(state.transparencyOnDeterminate);
            character.setSelectedItem(Angelina.getById(state.selectedCharacter));
            showUpdateNotification.setSelected(state.showUpdateNotification);
            maxHeight.setValue(state.maximumHeight);
            minHeight.setValue(state.minimumHeight);
            restrictMaxHeight.setSelected(state.restrictMaximumHeight);
            restrictMinHeight.setSelected(state.restrictMinimumHeight);
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public String getSelectedCharacterId() {
        final Angelina selected = (Angelina) character.getSelectedItem();
        return Optional.ofNullable(selected).orElse(Angelina.BROOM_RIDE).getId();
    }

    public JBCheckBox getDrawSprites() {
        return drawSprites;
    }

    public JBCheckBox getAddToolTips() {
        return addToolTips;
    }

    public JSlider getInitialVelocity() {
        return initialVelocity;
    }

    public JSlider getAcceleration() {
        return acceleration;
    }

    public JBCheckBox getIndeterminateTransparency() {
        return indeterminateTransparency;
    }

    public JBCheckBox getDeterminateTransparency() {
        return determinateTransparency;
    }

    public JBCheckBox getShowUpdateNotification() {
        return showUpdateNotification;
    }

    public JBCheckBox getRestrictMaxHeight() {
        return restrictMaxHeight;
    }

    public JSlider getMaxHeight() {
        return maxHeight;
    }

    public JBCheckBox getRestrictMinHeight() {
        return restrictMinHeight;
    }

    public JSlider getMinHeight() {
        return minHeight;
    }

    private void refreshPreview() {
        if (determinateProgressBar == null || indeterminateProgressBar == null) {
            return;
        }
        determinateUi = createProgressBarUi();
        indeterminateUi = createProgressBarUi();
        determinateProgressBar.setUI(determinateUi);
        indeterminateProgressBar.setUI(indeterminateUi);
    }

    private JPanel createPreviewPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        determinateProgressBar.setIndeterminate(false);
        determinateProgressBar.setValue(1);
        determinateUi = createProgressBarUi();
        determinateProgressBar.setUI(determinateUi);

        indeterminateProgressBar.setIndeterminate(true);
        indeterminateUi = createProgressBarUi();
        indeterminateProgressBar.setUI(indeterminateUi);

        final JButton randomizeButton = new JButton(AllIcons.Actions.Refresh);
        randomizeButton.setToolTipText("Refresh preview");
        randomizeButton.addActionListener(a -> refreshPreview());

        final GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.gridheight = 1;
        buttonConstraints.weightx = 0;
        panel.add(randomizeButton, buttonConstraints);
        final GridBagConstraints progressBarConstraints = new GridBagConstraints();
        progressBarConstraints.gridx = GridBagConstraints.RELATIVE;
        progressBarConstraints.gridy = 0;
        progressBarConstraints.gridwidth = 3;
        progressBarConstraints.gridheight = 1;
        progressBarConstraints.weightx = 0.45;
        progressBarConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(LabeledComponent.create(determinateProgressBar, "Determinate", BorderLayout.NORTH), progressBarConstraints);
        panel.add(LabeledComponent.create(indeterminateProgressBar, "Indeterminate", BorderLayout.NORTH), progressBarConstraints);
        return panel;
    }

    private AngelinaProgressBarUi createProgressBarUi() {
        final Angelina selected = Optional.ofNullable((Angelina) character.getSelectedItem())
            .orElse(Angelina.BROOM_RIDE);
        return new AngelinaProgressBarUi(selected,
            () -> initialVelocity.getValue() / 100f,
            () -> acceleration.getValue() / 100f,
            indeterminateTransparency::isSelected,
            determinateTransparency::isSelected,
            drawSprites::isSelected,
            addToolTips::isSelected,
            restrictMaxHeight::isSelected,
            maxHeight::getValue,
            restrictMinHeight::isSelected,
            minHeight::getValue);
    }

    private JPanel createIndeterminatePanel() {
        final JPanel indeterminatePanel = new JPanel();
        indeterminatePanel.setLayout(new GridLayout(2, 2));
        final LabeledComponent<JSlider> labeledInitVelocity = LabeledComponent
            .create(initialVelocity, String.format("Indeterminate initial velocity (%d/%d)", initialVelocity.getValue(), initialVelocity.getMaximum()));
        indeterminatePanel.add(labeledInitVelocity);
        indeterminatePanel.add(new Spacer());
        final LabeledComponent<JSlider> labeledAccel = LabeledComponent
            .create(acceleration, String.format("Indeterminate acceleration (%d/%d)", acceleration.getValue(), acceleration.getMaximum()));
        indeterminatePanel.add(labeledAccel);
        final JButton resetIndeterminateButton = new JButton("Reset to defaults");
        resetIndeterminateButton.addActionListener(a -> {
            if (a.getID() == ActionEvent.ACTION_PERFORMED) {
                acceleration.setValue(40);
                initialVelocity.setValue(100);
            }
        });
        initialVelocity.addChangeListener(e -> labeledInitVelocity.getLabel()
            .setText(String.format("Indeterminate initial velocity (%d/%d)", initialVelocity.getValue(), initialVelocity.getMaximum())));
        acceleration.addChangeListener(e -> labeledAccel.getLabel()
            .setText(String.format("Indeterminate acceleration (%d/%d)", acceleration.getValue(), acceleration.getMaximum())));
        indeterminatePanel.add(resetIndeterminateButton);
        return indeterminatePanel;
    }

}
