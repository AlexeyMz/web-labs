package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.BaseHttpServlet;
import ru.alexeymz.web.core.template.ExpressionEvaluator;
import ru.alexeymz.web.core.template.TemplateCache;
import ru.alexeymz.web.core.template.ViewTemplate;
import ru.alexeymz.web.data.RepositoryFactory;
import ru.alexeymz.web.data.SaleItemRepository;
import ru.alexeymz.web.model.SaleItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

@WebServlet("/item/*")
public class ItemController extends BaseHttpServlet {
    private static final String PAGE_TEMPLATE = "templates/item.html";
    private static final String IMAGE_TEMPLATE = "templates/item_image.html";
    private static final String REVIEW_TEMPLATE = "templates/item_review.html";

    private final SaleItemRepository itemRepository;
    private final TemplateCache templateCache;

    public ItemController() throws IOException {
        this.itemRepository = RepositoryFactory.instance().getSaleItemRepository();
        this.templateCache = new TemplateCache(getClass().getClassLoader(), false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);

        String langParam = req.getParameter("lang");
        if (langParam == null) {
            langParam = req.getLocale().getLanguage();
        }

        ViewTemplate pageTemplate = templateCache.get(PAGE_TEMPLATE);
        ViewTemplate imageTemplate = templateCache.get(IMAGE_TEMPLATE);
        ViewTemplate reviewTemplate = templateCache.get(REVIEW_TEMPLATE);

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
            SaleItem item = itemRepository.findItem(1001);

            Map<String, Object> bag = new HashMap<>();
            putLanguage(bag, langParam);
            bag.put("page.default_tab_page", 0);
            bag.put("item.id", item.getId());
            bag.put("item.name", item.getName());
            bag.put("item.description", item.getDescription());
            bag.put("item.first_image_id", item.getImageIds().get(0));
            bag.put("item.images", imageTemplate.forEach(item.getImageIds(),
                (id, subBag) -> {
                    subBag.put("image.id", id);
                }));
            bag.put("item.reviews", reviewTemplate.forEach(item.getReviews(),
                (review, subBag) -> {
                    subBag.put("review.author", review.getAuthorName());
                    subBag.put("review.text", review.getText());
                    int stars = review.getRate() / 2;
                    subBag.put("review.rate", repeat("\u2605", stars) + repeat("\u2606", 5 - stars));
                }));

            ResourceBundle localization = ResourceBundle.getBundle(
                    "i18n.text", Locale.forLanguageTag(langParam), LANGUAGE_SELECTOR);

            String rendered = pageTemplate.render(localization, ExpressionEvaluator.fromMap(bag));
            writer.write(rendered);
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
