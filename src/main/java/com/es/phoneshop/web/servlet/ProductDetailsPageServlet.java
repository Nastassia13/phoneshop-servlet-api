package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ErrorService;
import com.es.phoneshop.service.ParserService;
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
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService viewedService;
    private ParserService parserService;
    private ErrorService errorService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
        viewedService = HttpSessionRecentlyViewedProductsService.getInstance();
        parserService = ParserService.getInstance();
        errorService = ErrorService.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecentlyViewedProductsLoader viewedProductsLoader = new HttpSessionRecentlyViewedProductsLoader(request);
        Long productId = parserService.parseProductId(request);
        request.setAttribute("product", productDao.getItem(productId));
        request.getRequestDispatcher("/WEB-INF/pages/main/product.jsp").forward(request, response);
        viewedService.addToList(viewedProductsLoader, productId);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CartLoader cartLoader = new HttpSessionCartLoader(request);
        Cart cart = cartService.getCart(cartLoader);
        Long productId = parserService.parseProductId(request);
        String quantityString = request.getParameter("quantity");
        try {
            int quantity = parserService.parseNumber(quantityString, request);
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException | OutOfQuantityException | ParseException | ParseToIntegerException e) {
            request.setAttribute("error", errorService.handleError(e));
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Added to cart successfully");
    }
}
