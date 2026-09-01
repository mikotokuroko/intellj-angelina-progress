package com.angelinaprogress.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nullable;

public class AngelinaProgressChangenotesDialog extends DialogWrapper {
    public AngelinaProgressChangenotesDialog(@Nullable final Project project) {
        super(project);
        setTitle("Angelina Progress Bar Changenotes");
        init();

    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        String text;
        try (InputStream inputStream = AngelinaResourceLoader.getResourceAsStream(
            "META-INF/changenotes.html").orElse(null)) {
            text = inputStream == null
                ? "Changelog not found"
                : new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            text = "Changelog not found";
        }

        final JBLabel label = new JBLabel(text);
        label.setCopyable(true);
        return new JBScrollPane(label,
            JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
