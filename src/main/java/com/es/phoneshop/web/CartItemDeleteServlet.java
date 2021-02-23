package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.utils.CartLoader;
import com.es.phoneshop.utils.DataParser;
import com.es.phoneshop.utils.impl.HttpSessionCartLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartItemDeleteServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataParser dataParser = new DataParser();
        Long productId = dataParser.parseProductId(request);
        CartLoader cartLoader = new HttpSessionCartLoader(request);
        Cart cart = cartService.getCart(cartLoader);
        cartService.delete(cart, productId);
        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }
}
