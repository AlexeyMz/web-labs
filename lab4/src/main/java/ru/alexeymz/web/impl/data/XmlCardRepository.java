package ru.alexeymz.web.impl.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.model.Card;
import ru.alexeymz.web.model.Feedback;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class XmlCardRepository implements CardRepository {
    Map<Long, Card> cards;

    public XmlCardRepository(String resourceFileName) {
        ClassLoader classLoader = XmlCardRepository.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(resourceFileName)) {
            Document cardDB = loadXml(is);
            this.cards = loadCards(cardDB);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Card findById(long id) {
        return cards.get(id);
    }

    @Override
    public List<Card> findAllBySet(Optional<String> set) {
        if (!set.isPresent()) { return new ArrayList<>(cards.values()); }
        return cards.values().stream()
                .filter(c -> c.getSet().equals(set.get()))
                .collect(toList());
    }

    private Map<Long, Card> loadCards(Document cardDB) {
        cardDB.getDocumentElement().normalize();
        Map<Long, Card> cards = new HashMap<>();
        NodeList cardNodes = cardDB.getElementsByTagName("card");
        for (int i = 0; i < cardNodes.getLength(); i++) {
            Card card = parseCard((Element) cardNodes.item(i));
            cards.put(card.getId(), card);
        }
        return cards;
    }

    private Card parseCard(Element card) {
        String cardText = textContent(findElementByTag(card, "cardText"));
        String flavorText = textContent(findElementByTag(card, "flavorText"));
        List<String> imageIds = new ArrayList<>();
        NodeList imageNodes = findElementByTag(card, "images").getElementsByTagName("image");
        for (int j = 0; j < imageNodes.getLength(); j++) {
            imageIds.add(textContent(imageNodes.item(j)));
        }
        List<Feedback> feedbacks = new ArrayList<>();
        NodeList feedbackNodes = findElementByTag(card, "feedbacks").getElementsByTagName("feedback");
        for (int j = 0; j < feedbackNodes.getLength(); j++) {
            feedbacks.add(parseFeedback((Element)feedbackNodes.item(j)));
        }
        return new Card(
                Long.valueOf(card.getAttribute("id")),
                card.getAttribute("set"),
                Integer.valueOf(card.getAttribute("numberInSet")),
                card.getAttribute("name"),
                card.getAttribute("manaCost"),
                cardText,
                flavorText,
                imageIds,
                feedbacks,
                new BigDecimal(card.getAttribute("price")));
    }

    private Feedback parseFeedback(Element element) {
        return new Feedback(
                Long.valueOf(element.getAttribute("id")),
                textContent(element),
                element.getAttribute("author"),
                Integer.valueOf(element.getAttribute("rate")));
    }

    private Document loadXml(InputStream is) {
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

    private Element findElementByTag(Node parent, String tagName) {
        return (Element)((Element) parent).getElementsByTagName(tagName).item(0);
    }

    private String textContent(Node node) {
        return node.getTextContent().trim();
    }
}
