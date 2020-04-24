package service.product;

import model.Category;
import model.Product;
import model.ProductResponse;
import service.DatabaseInit;
import service.category.CategoryServiceImpl;
import service.category.ICategoryService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductImpl extends DatabaseInit implements IProduct {

    String selectAll = "select p.id, p.product_name, p.price, p.quantity, p.color, p.description, c.category_name " +
            "from product as p inner join category c on p.category_id = c.id";

    String insertProduct = "insert into product (product_name, price, quantity, color, description, category_id) " +
            "values (?,?,?,?,?,?) ";

    String updateProduct = "update product set product_name = ?, price = ?, quantity = ?, color = ?, description = ?, " +
            "category_id = ? where id = ?";

    String deleteProduct = "delete from product where id = ?";

    String findByName = "select p.id, p.product_name, p.price, p.quantity, p.color, p.description, c.category_name " +
            "from product as p inner join category c on p.category_id = c.id where product_name like ? ";

    String selectAllById = "select p.id, p.product_name, p.price, p.quantity, p.color, p.description, c.category_name " +
            "from product as p inner join category c on p.category_id = c.id where p.id";


    public ProductImpl(){

    }

    CategoryServiceImpl categoryService = new CategoryServiceImpl();

    @Override
    public List<ProductResponse> findAll() throws SQLException {
        List<ProductResponse> productList = new ArrayList<>();
        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(selectAll)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){

                int id = resultSet.getInt("id");
                String name = resultSet.getString("product_name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category_name");

                ProductResponse product = new ProductResponse(id, name, price, quantity, color, description, category);
                productList.add(product);
            }
        }
        return productList;
    }

    @Override
    public Product selectProduct(int id) {
        Product product = null;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(selectAllById)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){

                String name = resultSet.getString("product_name");
                int price =  resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String des = resultSet.getString("description");
                String category = resultSet.getString("category_name");

                product = new Product(name, price, quantity, color, des, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void insertProduct(Product product, Category category) throws SQLException {
        String categoryName = category.getCategory_name();
        Integer categoryId = categoryService.findIdByCategoryName(categoryName);
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(insertProduct)) {

            statement.setString(1, product.getProduct_name());
            statement.setInt(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getColor());
            statement.setString(5, product.getDescription());
            statement.setInt(6, categoryId); //Category trong bảng Product.

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean updateProduct(Product product, Category category) throws SQLException {
        boolean rowUpdate = false;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(updateProduct)) {

             statement.setInt(1, product.getId());
             statement.setString(2, product.getProduct_name());
             statement.setInt(3, product.getPrice());
             statement.setInt(4, product.getQuantity());
             statement.setString(5, product.getColor());
             statement.setString(6, product.getDescription());

             rowUpdate = statement.executeUpdate() > 0;
        }
        return rowUpdate;
    }

    @Override
    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted = false ;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(deleteProduct)) {

            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public List<Product> findAllByName(String name) throws SQLException {

        List<Product> productList = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(findByName)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category_name");

                productList.add(new Product(id, name, price, quantity, color, description, category));

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }

}

