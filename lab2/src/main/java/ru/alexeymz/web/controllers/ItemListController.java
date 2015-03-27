package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.CardRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/item-list")
public class ItemListController extends BaseAppController {
    private CardRepository cardRepository;

    public ItemListController() {
        cardRepository = repositoryFactory.getCardRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);

        String langParam = getLanguageParam(req);

        req.setAttribute("store.title", "Store");
        req.getRequestDispatcher("/WEB-INF/jsp/item-list.jsp").forward(req, resp);
    }
}
