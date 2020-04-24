package controller;

import model.Category;
import model.Product;
import model.ProductResponse;
import service.category.CategoryServiceImpl;
import service.category.ICategoryService;
import service.product.IProduct;
import service.product.ProductImpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = "/product")
public class ProductServlet extends javax.servlet.http.HttpServlet {
    private final IProduct productService = new ProductImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        try {
            switch (action != null ? action : "") {
                case "createProduct":
                    createProduct(request, response);
                    break;
                case "editProduct":
                    editProduct(request, response);
                    break;
                case "deleteProduct":
                    removeProduct(request, response);
                    break;
                case "searchProduct":
                    searchProductByName(request, response);
                    break;
                default:
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("categoryName");

        Product product = new Product(name, price, quantity, color, description);

        Category category = new Category(categoryName);
        productService.insertProduct(product, category);
        listProduct(request,response);
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException,
            ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("categoryName");

        Product product = new Product(id, name, price, quantity, color, description, category);
        Category category = new Category(categoryName);
        productService.updateProduct(product, category);
        response.sendRedirect("/product");
    }

    private void removeProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException,
            ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productService.deleteProduct(id);
        listProduct(request, response);
    }




    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action != null ? action : ""){
                case "createProduct":
                    showCreateForm(request, response);
                    break;
                case "editProduct":
                    showEditForm(request, response);
                    break;
                case "deleteProduct":
                    showRemoveForm(request, response);
                    break;
                default:
                    listProduct(request, response);
                    break;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void listProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException,
            ServletException, IOException {
        List<ProductResponse> productList = productService.findAll();
        request.setAttribute("list", productList);
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        requestDispatcher.forward(request, response);
    }

    private void searchProductByName(HttpServletRequest request, HttpServletResponse response) throws SQLException,
            ServletException, IOException {
        String name = request.getParameter("name");
        List<Product> productList = productService.findAllByName(name);
        request.setAttribute("list", productList);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        requestDispatcher.forward(request, response);
    }

    private void showRemoveForm(HttpServletRequest request, HttpServletResponse response) throws SQLException,
            ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productService.deleteProduct(id);
        List<ProductResponse> productList = productService.findAll();
        request.setAttribute("product", productList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        requestDispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("edit.jsp");
        requestDispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SQLException {

        List<Category> categories = categoryService.findAll();
        request.setAttribute("categories", categories);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("create.jsp");
        requestDispatcher.forward(request, response);
    }
}
