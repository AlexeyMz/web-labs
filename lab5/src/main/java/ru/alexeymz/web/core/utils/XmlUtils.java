package ru.alexeymz.web.core.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public final class XmlUtils {
    private XmlUtils() {}

    public static Document loadXml(InputStream is) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(is);
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Element findElementByTag(Node parent, String tagName) {
        return (Element)((Element) parent).getElementsByTagName(tagName).item(0);
    }

    public static String textContent(Node node) {
        return node.getTextContent().trim();
    }
}
