package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewedProductsService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedProductsService;
import com.es.phoneshop.utils.CartLoader;
import com.es.phoneshop.utils.RecentlyViewedProductsLoader;
import com.es.phoneshop.utils.impl.HttpSessionCartLoader;
import com.es.phoneshop.utils.impl.HttpSessionRecentlyViewedProductsLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService viewedService;
    private CartLoader cartLoader;
    private RecentlyViewedProductsLoader viewedProductsLoader;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
        viewedService = HttpSessionRecentlyViewedProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cartLoader = new HttpSessionCartLoader(request);
        viewedProductsLoader = new HttpSessionRecentlyViewedProductsLoader(request);
        Long productId = parseProductId(request);
        request.setAttribute("product", productDao.getProduct(productId));
        request.setAttribute("cart", cartService.getCart(cartLoader));
        request.getRequestDispatcher("/WEB-INF/pages/main/product.jsp").forward(request, response);
        viewedService.addToList(viewedProductsLoader, productId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cartLoader = new HttpSessionCartLoader(request);
        Long productId = parseProductId(request);
        int quantity = parseQuantity(request, response);
        if (quantity == Integer.MAX_VALUE || !addToCart(request, response, productId, quantity)) {
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Added to cart successfully");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }

    private int parseQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quantity;
        String quantityString = request.getParameter("quantity");
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            Number parsed = format.parse(quantityString);
            if (parsed.doubleValue() - parsed.intValue() != 0) {
                throw new ParseToIntegerException();
            }
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException | ParseToIntegerException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return Integer.MAX_VALUE;
        }
        return quantity;
    }

    private boolean addToCart(HttpServletRequest request, HttpServletResponse response, Long productId, int quantity) throws ServletException, IOException {
        Cart cart = cartService.getCart(cartLoader);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Not enough stock. Available: " + e.getStockAvailable());
            doGet(request, response);
            return false;
        } catch (OutOfQuantityException e) {
            request.setAttribute("error", "Insufficient quantity");
            doGet(request, response);
            return false;
        }
        return true;
    }
}
