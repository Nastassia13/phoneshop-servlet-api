package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.utils.CartLoader;
import com.es.phoneshop.utils.DataParser;
import com.es.phoneshop.utils.ErrorHandler;
import com.es.phoneshop.utils.impl.HttpSessionCartLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/main/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();
        DataParser dataParser = new DataParser();
        CartLoader cartLoader = new HttpSessionCartLoader(request);
        Cart cart = cartService.getCart(cartLoader);
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            try {
                int quantity = dataParser.parseQuantity(quantities[i], request);
                cartService.update(cart, productId, quantity);
            } catch (ParseException | ParseToIntegerException | OutOfQuantityException | OutOfStockException e) {
                ErrorHandler.handleErrors(errors, productId, e);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
