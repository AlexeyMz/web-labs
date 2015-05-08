package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.data.OrderRepository;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.CartEntry;
import ru.alexeymz.web.model.Order;
import ru.alexeymz.web.model.OrderEntry;
import ru.alexeymz.web.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/orders")
public class OrderHistoryController extends BaseAppController {
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.cardRepository = (CardRepository)context.getAttribute(CardRepository.ATTRIBUTE);
        this.userRepository = (UserRepository)context.getAttribute(UserRepository.ATTRIBUTE);
        this.orderRepository = (OrderRepository)context.getAttribute(OrderRepository.ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUserOrError(userRepository, req, resp);
        if (user == null) { return; }
        renderView(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUserOrError(userRepository, req, resp);
        if (user == null) { return; }

        Order order = new Order();
        order.setUsername(user.getUsername());

        Set<OrderEntry> entries = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartEntry cartEntry : ensureEntries(req)) {
            BigDecimal price = cartEntry.getCard().getPrice().multiply(new BigDecimal(cartEntry.getQuantity()));
            total = total.add(price);
            entries.add(new OrderEntry(order, cartEntry.getCard().getId(), cartEntry.getQuantity()));
        }

        order.setTotal(total);
        order.setEntries(entries);
        order.setPurchaseDate(Calendar.getInstance());
        orderRepository.save(order);

        renderView(req, resp, user);
    }


    private void renderView(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("orders", orderRepository.findByUsername(user.getUsername()));
        req.setAttribute("cardRepo", cardRepository);
        req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
    }
}
