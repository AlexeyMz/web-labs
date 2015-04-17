package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.model.Card;
import ru.alexeymz.web.model.CartEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/cart")
public class CartController extends BaseAppController {
    private CardRepository cardRepository;

    public CartController() {
        cardRepository = repositoryFactory.getCardRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);
        configureLanguage(req);
        req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
    }

    private static enum Command {
        NONE, ADD, REMOVE
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configureForHtmlUtf8(req, resp);
        configureLanguage(req);

        Command command = Command.NONE;
        String param = req.getParameter("add");
        if (param != null) {
            command = Command.ADD;
        } else {
            param = req.getParameter("remove");
            if (param != null) { command = Command.REMOVE; }
        }

        if (command == Command.NONE) {
            resp.sendError(400);
            return;
        }
        Long id = matchId(param);
        if (id == null) {
            resp.sendError(400);
            return;
        }
        Card card = cardRepository.findById(id);
        if (card == null) {
            resp.sendError(404);
            return;
        }

        List<CartEntry> entries = ensureEntries(req);

        if (command == Command.ADD) {
            Optional<CartEntry> entry = entries.stream()
                    .filter(ce -> ce.getCard().getId() == id)
                    .findFirst();
            if (entry.isPresent()) {
                entry.get().setQuantity(entry.get().getQuantity() + 1);
            } else {
                entries.add(new CartEntry(card, 1));
            }
        } else if (command == Command.REMOVE) {
            Optional<CartEntry> entry = entries.stream()
                    .filter(ce -> ce.getCard().getId() == id)
                    .findFirst();
            if (entry.isPresent()) {
                int quantity = entry.get().getQuantity();
                if (quantity > 1) {
                    entry.get().setQuantity(quantity - 1);
                } else {
                    entries.remove(entry.get());
                }
            }
        }
        req.getSession().setAttribute("total", entries.stream()
                .map(ce -> ce.getCard().getPrice().multiply(
                        BigDecimal.valueOf(ce.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
    }

    List<CartEntry> ensureEntries(HttpServletRequest req) {
        List<CartEntry> entries = (List<CartEntry>)req
                .getSession().getAttribute("entries");
        if (entries == null) {
            entries = new ArrayList<>();
            req.getSession().setAttribute("entries", entries);
        }
        return entries;
    }
}