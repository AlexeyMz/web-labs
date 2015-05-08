package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.data.DeliveryPointRepository;
import ru.alexeymz.web.data.OrderRepository;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.*;

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

import static java.util.stream.Collectors.toList;

@WebServlet("/orders")
public class OrderHistoryController extends BaseAppController {
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private DeliveryPointRepository deliveryPointRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.cardRepository = (CardRepository)context.getAttribute(CardRepository.ATTRIBUTE);
        this.userRepository = (UserRepository)context.getAttribute(UserRepository.ATTRIBUTE);
        this.orderRepository = (OrderRepository)context.getAttribute(OrderRepository.ATTRIBUTE);
        this.deliveryPointRepository = (DeliveryPointRepository)context.getAttribute(DeliveryPointRepository.ATTRIBUTE);
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

        String deliveryType = req.getParameter("deliveryType");
        String deliveryPointKey = req.getParameter("deliveryPoint");
        String deliveryAddress = req.getParameter("deliveryAddress");

        Order order = new Order();
        order.setUsername(user.getUsername());

        if (deliveryType == null) { resp.sendError(400); return; }
        else if (deliveryType.equals("byCourier")) {
            if (deliveryAddress != null) { deliveryAddress = deliveryAddress.trim(); }
            if (deliveryAddress == null || deliveryAddress.isEmpty()) {
                resp.sendError(400);
                return;
            }
            order.setDeliveryAddress(deliveryAddress);
        } else {
            DeliveryPoint point = deliveryPointKey == null ? null : deliveryPointRepository.findByKey(deliveryPointKey);
            if (point == null) {
                resp.sendError(400);
                return;
            }
            order.setDeliveryPoint(point.getKey());
        }

        Set<OrderEntry> entries = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartEntry cartEntry : ensureEntries(req)) {
            BigDecimal price = cartEntry.getCard().getPrice().multiply(new BigDecimal(cartEntry.getQuantity()));
            total = total.add(price);
            entries.add(new OrderEntry(order, cartEntry.getCard().getId(), cartEntry.getQuantity()));
        }

        if (entries.isEmpty()) {
            log(String.format("User <%s> trying to submit an empty order.", user.getUsername()));
            resp.sendError(400);
            return;
        }

        order.setTotal(total);
        order.setEntries(entries);
        order.setPurchaseDate(Calendar.getInstance());
        orderRepository.save(order);

        log(String.format("User <%s> submitted a new order: %s", user.getUsername(), order.toString()));
        // clear shopping cart
        ensureEntries(req).clear();

        renderView(req, resp, user);
    }


    private void renderView(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("orders", orderRepository.findByUsername(user.getUsername())
                .stream().sorted((a, b) -> b.getPurchaseDate().compareTo(a.getPurchaseDate()))
                .collect(toList()));
        req.setAttribute("cardRepo", cardRepository);
        req.setAttribute("deliveryPointRepo", deliveryPointRepository);
        req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
    }
}
