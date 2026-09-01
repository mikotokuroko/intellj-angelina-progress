package com.angelinaprogress.intellij;

import com.angelinaprogress.intellij.model.Angelina;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class AngelinaResourceLoader {
    private static final String SPRITE_RESOURCE_PATH =
        "com/angelinaprogress/intellij/characters/";

    private static final Map<Angelina, Icon> CACHE = new ConcurrentHashMap<>();

    private AngelinaResourceLoader() {
    }

    public static Icon getIcon(final Angelina angelina) {
        return CACHE.computeIfAbsent(angelina, AngelinaResourceLoader::loadCharacterIcon);
    }

    private static String getIconPath(final Angelina angelina) {
        return SPRITE_RESOURCE_PATH + angelina.getResourceName();
    }

    private static Optional<URL> getResource(final String resourceName) {
        return Optional.ofNullable(AngelinaResourceLoader.class.getClassLoader().getResource(resourceName));
    }

    public static Optional<InputStream> getResourceAsStream(final String resourceName) {
        return Optional.ofNullable(AngelinaResourceLoader.class.getClassLoader()
            .getResourceAsStream(resourceName));
    }

    private static Icon loadCharacterIcon(final Angelina angelina) {
        final Optional<URL> resource = getResource(getIconPath(angelina));
        if (resource.isEmpty()) {
            return new ImageIcon();
        }
        try {
            return AnimatedSvgIcon.load(resource.get(), angelina);
        } catch (final java.io.IOException ignored) {
            return new ImageIcon();
        }
    }
}
