package ru.alexeymz.web.impl.data;

import static ru.alexeymz.web.core.utils.XmlUtils.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.alexeymz.web.data.DeliveryPointRepository;
import ru.alexeymz.web.model.DeliveryPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static ru.alexeymz.web.core.utils.XmlUtils.loadXml;

public class XmlDeliveryPointRepository implements DeliveryPointRepository {
    private Map<String, DeliveryPoint> points;

    public XmlDeliveryPointRepository(String resourceFileName) {
        ClassLoader classLoader = XmlCardRepository.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(resourceFileName)) {
            Document cardDB = loadXml(is);
            this.points = loadPoints(cardDB);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public DeliveryPoint findByKey(String key) {
        return points.get(key);
    }

    @Override
    public List<DeliveryPoint> findAll() {
        return new ArrayList<>(points.values());
    }

    private Map<String, DeliveryPoint> loadPoints(Document document) {
        document.getDocumentElement().normalize();
        Map<String, DeliveryPoint> result = new HashMap<>();
        NodeList pointNodes = document.getElementsByTagName("deliveryPoint");
        for (int i = 0; i < pointNodes.getLength(); i++) {
            DeliveryPoint point = parsePoint((Element) pointNodes.item(i));
            result.put(point.getKey(), point);
        }
        return result;
    }

    private DeliveryPoint parsePoint(Element element) {
        return new DeliveryPoint(
            element.getAttribute("key"),
            element.getAttribute("coords"),
            textContent(findElementByTag(element, "address")));
    }
}
