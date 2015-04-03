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
    private CardRepository cardRepository;

    public ItemListController() {
        cardRepository = repositoryFactory.getCardRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);

        String langParam = getLanguageParam(req);
        ResourceBundle localization = ResourceBundle.getBundle(
                "i18n.text", Locale.forLanguageTag(langParam), LANGUAGE_SELECTOR);

        Optional<String> selectedSet = Optional.ofNullable(req.getParameter("filter"));
        if (selectedSet.isPresent()) {
            resp.addCookie(new Cookie("selectedCardSet", selectedSet.get()));
        } else {
            selectedSet = Arrays.stream(req.getCookies())
                    .filter(c -> c.getName().equals("selectedCardSet"))
                    .map(Cookie::getValue)
                    .findFirst();
        }

        Set<String> sets = cardRepository.findAllBySet(Optional.<String>empty())
                .stream().map(Card::getSet).collect(Collectors.toSet());
        String setToFind = selectedSet.orElse(null);
        if (setToFind.equals("none")) { setToFind = null; }

        req.setAttribute("l10n", localization);
        req.setAttribute("languages", LANGUAGES);
        req.setAttribute("langCode", langParam);
        req.setAttribute("cards", cardRepository.findAllBySet(Optional.ofNullable(setToFind)));
        req.setAttribute("sets", sets);
        req.setAttribute("selectedSet", selectedSet.orElse(null));

        req.getRequestDispatcher("/WEB-INF/jsp/item-list.jsp").forward(req, resp);
    }
}
