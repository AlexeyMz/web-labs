package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.template.ExpressionEvaluator;
import ru.alexeymz.web.core.template.TemplateCache;
import ru.alexeymz.web.core.template.Unescaped;
import ru.alexeymz.web.core.template.ViewTemplate;
import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.model.Card;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/item/*", initParams = {
    @WebInitParam(name="default_tab", value="1")
})
public class ItemController extends BaseAppController {
    private static final String PAGE_TEMPLATE = "templates/item.html";
    private static final String IMAGE_TEMPLATE = "templates/item_image.html";
    private static final String REVIEW_TEMPLATE = "templates/item_review.html";
    private static final Pattern URI_PATTERN = Pattern.compile(
            "^/item/([0-9]+)/?$", Pattern.CASE_INSENSITIVE);

    private final CardRepository cardRepository;
    private final TemplateCache templateCache;

    public ItemController() throws IOException {
        this.cardRepository = repositoryFactory.getCardRepository();
        this.templateCache = new TemplateCache(getClass().getClassLoader(), false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);

        Long itemId = matchItemId(req.getRequestURI());
        if (itemId == null) {
            resp.sendError(400);
            return;
        }

        String langParam = getLanguageParam(req);

        Card item = cardRepository.findById(itemId);
        if (item == null) {
            resp.sendError(404);
            return;
        }

        ViewTemplate pageTemplate = templateCache.get(PAGE_TEMPLATE);
        ViewTemplate imageTemplate = templateCache.get(IMAGE_TEMPLATE);
        ViewTemplate reviewTemplate = templateCache.get(REVIEW_TEMPLATE);

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
            Map<String, Object> bag = new HashMap<>();
            putLanguage(bag, langParam);
            bag.put("page.default_tab_page", getInitParameter("default_tab"));
            bag.put("item.id", item.getId());
            bag.put("item.name", item.getName());
            bag.put("item.set", item.getSet());
            bag.put("item.number_in_set", item.getNumberInSet());
            bag.put("item.mana_cost", item.getManaCost());
            bag.put("item.card_text", Unescaped.escapedWithNewLines(item.getCardText()));
            bag.put("item.flavor_text", Unescaped.escapedWithNewLines(item.getFlavorText()));
            bag.put("item.first_image_id", item.getImageIds().get(0));
            bag.put("item.images", imageTemplate.forEach(item.getImageIds(),
                (id, subBag) -> {
                    subBag.put("image.id", id);
                }));
            bag.put("item.reviews", reviewTemplate.forEach(item.getReviews(),
                (review, subBag) -> {
                    subBag.put("review.author", review.getAuthorName());
                    subBag.put("review.text", Unescaped.escapedWithNewLines(review.getText()));
                    int stars = review.getRate() / 2;
                    subBag.put("review.rate", repeat("\u2605", stars) + repeat("\u2606", 5 - stars));
                }));

            ResourceBundle localization = ResourceBundle.getBundle(
                    "i18n.text", Locale.forLanguageTag(langParam), LANGUAGE_SELECTOR);

            String rendered = pageTemplate.render(localization, ExpressionEvaluator.fromMap(bag));
            writer.write(rendered);
        }
    }

    private Long matchItemId(String uri) {
        Matcher matcher = URI_PATTERN.matcher(uri);
        if (!matcher.find()) { return null; }
        try {
            return Long.valueOf(matcher.group(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String repeat(String s, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) { builder.append(s); }
        return builder.toString();
    }

    private void putLanguage(Map<String, Object> bag, String langCode) {
        bag.put("lang.code", langCode);
        bag.put("lang.en.selected", langCode.equals("en") ? "selected" : "");
        bag.put("lang.ru.selected", langCode.equals("ru") ? "selected" : "");
        bag.put("lang.ja.selected", langCode.equals("ja") ? "selected" : "");
    }
}
