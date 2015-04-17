package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.model.Card;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/item-list")
public class ItemListController extends BaseAppController {
    private static final String SELECTED_SET_COOKIE = "selectedCardSet";

    private CardRepository cardRepository;

    public ItemListController() {
        cardRepository = repositoryFactory.getCardRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);
        configureLanguage(req);

        Optional<String> selectedSet = Optional.ofNullable(req.getParameter("filter"));
        if (selectedSet.isPresent()) {
            Cookie cookie = new Cookie(SELECTED_SET_COOKIE, selectedSet.get());
            cookie.setMaxAge(10); /* seconds */
            resp.addCookie(cookie);
        } else {
            selectedSet = Arrays.stream(req.getCookies())
                    .filter(c -> c.getName().equals(SELECTED_SET_COOKIE))
                    .map(Cookie::getValue)
                    .findFirst();
        }

        Set<String> sets = cardRepository.findAllBySet(Optional.<String>empty())
                .stream().map(Card::getSet).collect(Collectors.toSet());
        String setToFind = selectedSet.orElse(null);
        if (setToFind != null && setToFind.equals("none")) { setToFind = null; }

        req.setAttribute("cards", cardRepository.findAllBySet(Optional.ofNullable(setToFind)));
        req.setAttribute("sets", sets);
        req.setAttribute("selectedSet", selectedSet.orElse(null));

        req.getRequestDispatcher("/WEB-INF/jsp/item-list.jsp").forward(req, resp);
    }
}
