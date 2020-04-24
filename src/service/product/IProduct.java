package service.product;

import model.Category;
import model.Product;
import model.ProductResponse;

import java.sql.SQLException;
import java.util.List;

public interface IProduct {
    public List<ProductResponse> findAll() throws SQLException;

    public Product selectProduct(int id);

    public void insertProduct(Product product, Category category) throws SQLException;

    public boolean updateProduct(Product product, Category category) throws SQLException;

    public boolean deleteProduct(int id) throws SQLException;

    public List<Product> findAllByName(String name) throws SQLException;
}
