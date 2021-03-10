package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ParseToIntegerException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.TypeSearch;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ErrorService;
import com.es.phoneshop.service.ParserService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.utils.CartLoader;
import com.es.phoneshop.utils.impl.HttpSessionCartLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;
    Map<String, String> errors;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        errors = new HashMap<>();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String typeSearch = request.getParameter("typeSearch");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        request.setAttribute("products", productDao.findSearchAdvancedProducts(description, typeSearch,
                checkPrice(request, minPrice, errors), checkPrice(request, maxPrice, errors)));
        request.getRequestDispatcher("/WEB-INF/pages/main/advancedSearch.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkPrice(request, "minPrice", errors);
        checkPrice(request, "maxPrice", errors);
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/advancedSearch");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private BigDecimal checkPrice(HttpServletRequest request, String parameterName, Map<String, String> errors) {
        String value = request.getParameter(parameterName);
        if (value == null) {
            value = "";
        }
        try {
            int price = ParserService.getInstance().parseNumber(value, request);
            return BigDecimal.valueOf(price);
        } catch (ParseException | ParseToIntegerException e) {
            errors.put(parameterName, ErrorService.getInstance().handleError(e));
            return null;
        }
    }
}
