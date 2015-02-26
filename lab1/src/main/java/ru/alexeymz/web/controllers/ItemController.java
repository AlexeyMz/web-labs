package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.template.ExpressionEvaluator;
import ru.alexeymz.web.core.template.ViewTemplate;
import ru.alexeymz.web.data.RepositoryFactory;
import ru.alexeymz.web.data.SaleItemRepository;
import ru.alexeymz.web.model.SaleItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

@WebServlet("/item/*")
public class ItemController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try (OutputStreamWriter writer = new OutputStreamWriter(
                resp.getOutputStream(), "UTF-8")) {
//            writer.write("Hello, Servlets! Привет!\r\n");
            ResourceBundle bundle = ResourceBundle.getBundle(
                    "i18n.text", Locale.forLanguageTag("ru"));
//            writer.write(bundle.getString("item.review.rate.label") + "\r\n");

            ViewTemplate template = ViewTemplate.fromResource(getClass(), "templates/item.html");

            SaleItemRepository itemRepository = RepositoryFactory.instance().getSaleItemRepository();
            SaleItem item = itemRepository.findItem(1001);

            Map<String, Object> bag = new HashMap<>();
            bag.put("item.name", item.getName());

            String rendered = template.render(bundle, evaluatorFromMap(bag));
            writer.write(rendered);
        }
    }

    private ExpressionEvaluator evaluatorFromMap(Map<String, Object> elements) {
        return key -> elements.getOrDefault(key, "").toString();
    }
}
