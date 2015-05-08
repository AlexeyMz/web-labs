package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.DeliveryPointRepository;
import ru.alexeymz.web.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order")
public class OrderController extends BaseAppController {
    private DeliveryPointRepository deliveryPointRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.deliveryPointRepository = (DeliveryPointRepository)context.getAttribute(
                DeliveryPointRepository.ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("points", deliveryPointRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(req, resp);
    }
}
