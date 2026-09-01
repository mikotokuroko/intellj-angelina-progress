package com.angelinaprogress.intellij;

import com.intellij.util.SVGLoader;
import com.intellij.ui.scale.ScaleContext;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.UIUtil;
import com.angelinaprogress.intellij.model.Angelina;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Renders the discrete frame groups in an animated SVG using IntelliJ's SVG renderer. */
final class AnimatedSvgIcon implements Icon {
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final long DEFAULT_FRAME_DURATION_MILLIS = 200;

    private final List<Image> frames;
    private final long frameDurationMillis;

    private AnimatedSvgIcon(final List<Image> frames, final long frameDurationMillis) {
        this.frames = frames;
        this.frameDurationMillis = frameDurationMillis;
    }

    static AnimatedSvgIcon load(final URL resource, final Angelina character) throws IOException {
        try (InputStream input = resource.openStream()) {
            final DocumentBuilder builder = newDocumentBuilder();
            final Document document = builder.parse(input);
            final List<Element> frameElements = findFrameElements(document);
            if (frameElements.isEmpty()) {
                throw new IOException("Animated SVG contains no frame groups: " + resource);
            }

            final long frameDuration = readFrameDurationMillis(frameElements.get(0), frameElements.size());
            final List<Image> frames = new ArrayList<>(frameElements.size());
            for (final Element frameElement : frameElements) {
                try {
                    frames.add(renderFrame(builder, document, frameElement, character));
                } catch (final IllegalStateException exception) {
                    if (!isDocumentLimitExceeded(exception)) {
                        throw exception;
                    }
                }
            }
            if (frames.isEmpty()) {
                throw new IOException("Every SVG frame exceeds IntelliJ's document limits: " + resource);
            }
            return new AnimatedSvgIcon(List.copyOf(frames), frameDuration);
        } catch (final ParserConfigurationException | SAXException | TransformerException e) {
            throw new IOException("Unable to render animated SVG " + resource, e);
        }
    }

    int getFrameCount() {
        return frames.size();
    }

    @Override
    public void paintIcon(final Component component, final Graphics graphics, final int x, final int y) {
        final long animationFrame = System.currentTimeMillis() / frameDurationMillis;
        UIUtil.drawImage(graphics, frames.get((int) (animationFrame % frames.size())), x, y, component);
    }

    @Override
    public int getIconWidth() {
        return ImageUtil.getUserWidth(frames.get(0));
    }

    @Override
    public int getIconHeight() {
        return ImageUtil.getUserHeight(frames.get(0));
    }

    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);
        return factory.newDocumentBuilder();
    }

    private static List<Element> findFrameElements(final Document document) {
        final List<Element> frames = new ArrayList<>();
        final NodeList groups = document.getElementsByTagNameNS(SVG_NAMESPACE, "g");
        for (int index = 0; index < groups.getLength(); index++) {
            final Element group = (Element) groups.item(index);
            if (group.getAttribute("id").matches("frame\\d+")) {
                frames.add(group);
            }
        }
        return frames;
    }

    private static Image renderFrame(final DocumentBuilder builder, final Document source,
                                     final Element frameElement, final Angelina character)
        throws TransformerException, IOException {
        final Document frameDocument = builder.newDocument();
        final Element sourceRoot = source.getDocumentElement();
        final Element root = frameDocument.createElementNS(SVG_NAMESPACE, "svg");
        root.setAttribute("xmlns", SVG_NAMESPACE);
        root.setAttribute("viewBox", character.getCropX() + " " + character.getCropY() + " "
            + character.getCropWidth() + " " + character.getCropHeight());
        root.setAttribute("width", Integer.toString(character.getCropWidth()));
        root.setAttribute("height", Integer.toString(character.getCropHeight()));
        copyAttributeIfPresent(sourceRoot, root, "preserveAspectRatio");
        frameDocument.appendChild(root);

        final Element frame = (Element) frameDocument.importNode(frameElement, true);
        frame.setAttribute("opacity", "1");
        removeAnimationElements(frame);
        root.appendChild(frame);

        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(frameDocument), new StreamResult(output));

        final byte[] svg = output.toString(StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
        final double width = (double) character.getHeight() * character.getCropWidth()
            / character.getCropHeight();
        final double height = character.getHeight();
        final ScaleContext scaleContext = ScaleContext.create();
        final Image image = SVGLoader.load(null, new ByteArrayInputStream(svg), scaleContext, width, height);
        return ImageUtil.ensureHiDPI(image, scaleContext, width, height);
    }

    private static void removeAnimationElements(final Element frame) {
        final NodeList animations = frame.getElementsByTagNameNS(SVG_NAMESPACE, "animate");
        for (int index = animations.getLength() - 1; index >= 0; index--) {
            final Node animation = animations.item(index);
            animation.getParentNode().removeChild(animation);
        }
    }

    private static boolean isDocumentLimitExceeded(final IllegalStateException exception) {
        return exception.getMessage() != null
                && exception.getMessage().startsWith(
                        "Maximum count of rendered element instances exceeded");
    }

    private static long readFrameDurationMillis(final Element frame, final int frameCount) {
        final NodeList animations = frame.getElementsByTagNameNS(SVG_NAMESPACE, "animate");
        if (animations.getLength() == 0) {
            return DEFAULT_FRAME_DURATION_MILLIS;
        }
        final String duration = ((Element) animations.item(0)).getAttribute("dur");
        if (!duration.endsWith("s")) {
            return DEFAULT_FRAME_DURATION_MILLIS;
        }
        try {
            final double totalSeconds = Double.parseDouble(duration.substring(0, duration.length() - 1));
            return Math.max(1, Math.round(totalSeconds * 1000 / frameCount));
        } catch (final NumberFormatException ignored) {
            return DEFAULT_FRAME_DURATION_MILLIS;
        }
    }

    private static void copyAttributeIfPresent(final Element source, final Element target,
                                               final String attribute) {
        if (source.hasAttribute(attribute)) {
            target.setAttribute(attribute, source.getAttribute(attribute));
        }
    }
}
