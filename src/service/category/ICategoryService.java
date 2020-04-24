package service.category;

import model.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {
    public List<Category> findAll() throws SQLException;

    public Integer findIdByCategoryName(String categoryName) throws SQLException;
}
