package service.category;

import model.Category;
import service.DatabaseInit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl extends DatabaseInit implements ICategoryService {
    String selectAll = "select id, category_name from category";
    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectAll)){

             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()){
                 int id = resultSet.getInt("id");
                 String name = resultSet.getString("category_name");

                 Category category = new Category(id, name);
                 categories.add(category);
             }
        }
        return categories;
    }

    public Integer findIdByCategoryName(String categoryName) throws SQLException {
        String findByName = "select c.id from category c where c.category_name like ?";
        Integer id = null;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(findByName)
        ) {
            statement.setString(1, "%" + categoryName + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        }
        return id;
    }
}
