package com.angelinaprogress.intellij;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.Test;

public class PluginMetadataTest {
    private static final Path PROJECT_ROOT = Path.of(System.getProperty("user.dir"));

    @Test
    public void pluginIdsStayInSync() throws IOException {
        final Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(PROJECT_ROOT.resolve("gradle.properties"))) {
            properties.load(input);
        }

        final String pluginId = properties.getProperty("pluginId");
        final String pluginXml = Files.readString(
            PROJECT_ROOT.resolve("src/main/resources/META-INF/plugin.xml"));

        assertEquals(pluginId, AngelinaProgressListener.PLUGIN_ID_STRING);
        assertTrue(pluginXml.contains("<id>" + pluginId + "</id>"));
    }

    @Test
    public void overviewUsesPublishedPreviewImages() throws IOException {
        final String description = Files.readString(PROJECT_ROOT.resolve("description.html"));
        for (final String animation : new String[]{"broom", "run", "dive"}) {
            assertTrue(Files.isRegularFile(
                PROJECT_ROOT.resolve("docs/previews/" + animation + ".png")));
            assertTrue(description.contains(
                "mikotokuroko/intellj-angelina-progress/main/docs/previews/"
                    + animation + ".png"));
        }
    }
}
