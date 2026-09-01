package com.angelinaprogress.intellij;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.angelinaprogress.intellij.model.Angelina;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import org.junit.Test;

public class AngelinaResourceTest {
    @Test
    public void allAnimationsAreCroppedAndTransparent() {
        for (final Angelina character : Angelina.values()) {
            final Icon icon = AngelinaResourceLoader.getIcon(character);
            assertTrue(character + " should have a visible width", icon.getIconWidth() > 0);
            assertEquals(character.getHeight(), icon.getIconHeight());

            final int width = icon.getIconWidth();
            final int height = icon.getIconHeight();
            final int[] pixels = renderPixels(icon, width, height);

            int visiblePixels = 0;
            int transparentPixels = 0;
            int greenPixels = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int argb = pixels[y * width + x];
                    final int alpha = (argb >>> 24) & 0xff;
                    if (alpha == 0) {
                        transparentPixels++;
                        continue;
                    }
                    visiblePixels++;
                    final int red = (argb >>> 16) & 0xff;
                    final int green = (argb >>> 8) & 0xff;
                    final int blue = argb & 0xff;
                    if (green > 180 && green > red * 2 && green > blue * 2) {
                        greenPixels++;
                    }
                }
            }

            final int minimumCoverage = width * height / 20;
            assertTrue(character + " should contain visible artwork (" + visiblePixels + " pixels)",
                visiblePixels > minimumCoverage);
            assertTrue(character + " should contain transparent background",
                transparentPixels > minimumCoverage);
            assertEquals(character + " should not retain green-screen pixels", 0, greenPixels);
        }
    }

    @Test
    public void allSvgAnimationsUseEveryFrame() {
        assertFrameCount(Angelina.BROOM_RIDE, 5);
        assertFrameCount(Angelina.DELIVERY_RUN, 5);
        assertFrameCount(Angelina.DIVING, 6);
    }

    private static void assertFrameCount(final Angelina animation, final int expected) {
        final Icon icon = AngelinaResourceLoader.getIcon(animation);
        assertTrue(icon instanceof AnimatedSvgIcon);
        assertEquals(expected, ((AnimatedSvgIcon) icon).getFrameCount());
    }

    private static int[] renderPixels(final Icon icon, final int width, final int height) {
        final int[] pixels = new int[width * height];
        final BufferedImage rendered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = rendered.createGraphics();
        icon.paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        rendered.getRGB(0, 0, width, height, pixels, 0, width);
        return pixels;
    }
}
