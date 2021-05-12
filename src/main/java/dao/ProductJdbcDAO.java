/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dugwi731
 */
public class ProductJdbcDAO implements ProductDAO {
	
	private static String uri = DbConnection.getDefaultConnectionUri();
	
	public ProductJdbcDAO(){};
	
	public ProductJdbcDAO(String uri){
		this.uri = uri;
	}

	@Override
	public Collection<Product> filterByCategory(String category) {
		String sql = "select * from Product where category = ?";

		try (
				  // get connection to database
				  Connection connection = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = connection.prepareStatement(sql);) {
			// set the parameter
			stmt.setString(1, category);

			// execute the query
			ResultSet rs = stmt.executeQuery();

			List<Product> products = new ArrayList<>();
			
			// query only returns a single result, so use 'if' instead of 'while'
			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				BigDecimal quantity = rs.getBigDecimal("quantity_in_stock");
				BigDecimal price = rs.getBigDecimal("list_price");
				
				Product p1 = new Product(id, name, description, category, price, quantity);
				products.add(p1);
			}
			return products;

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public Collection<String> getCategories() {
		String sql = "select distinct category from product";

		try (
				  // get a connection to the database
				  Connection dbCon = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
			// execute the query
			ResultSet rs = stmt.executeQuery();

			// Using a List to preserve the order in which the data was returned from the query.
			List<String> categories = new ArrayList<>();

			// iterate through the query results
			while (rs.next()) {

				// get the data out of the query
				String category = rs.getString("category");
				// and put it in the collection
				categories.add(category);
			}

			return categories;

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public Collection<Product> getProducts() {
		String sql = "select * from Product order by product_id";

		try (
				  // get a connection to the database
				  Connection dbCon = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
			// execute the query
			ResultSet rs = stmt.executeQuery();

			// Using a List to preserve the order in which the data was returned from the query.
			List<Product> products = new ArrayList<>();

			// iterate through the query results
			while (rs.next()) {

				// get the data out of the query
				String id = rs.getString("product_id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				String category = rs.getString("category");
				BigDecimal quantity = rs.getBigDecimal("quantity_in_stock");
				BigDecimal price = rs.getBigDecimal("list_price");

				// use the data to create a product object
				Product p1 = new Product(id, name, description, category, price, quantity);

				// and put it in the collection
				products.add(p1);
			}

			return products;

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public void removeProduct(Product product) {
		String sql = "delete from Product where Product_Id = ?";

		try (
				  // get connection to databaseurl
				  Connection dbCon = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
			// copy the data from the product domain object into the SQL parameters
			stmt.setString(1, product.getProductID());
			stmt.executeUpdate();  // execute the statement

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveProduct(Product product) {
		String sql = "insert into product (product_id, name, description, category, quantity_in_stock, list_price) values (?,?,?,?,?,?)";

		try (
				  // get connection to databaseurl
				  Connection dbCon = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
			// copy the data from the product domain object into the SQL parameters
			stmt.setString(1, product.getProductID());
			stmt.setString(2, product.getName());
			stmt.setString(3, product.getDescription());
			stmt.setString(4, product.getCategory());
			stmt.setBigDecimal(5, product.getQuantityInStock());
			stmt.setBigDecimal(6, product.getListPrice());

			stmt.executeUpdate();  // execute the statement

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public Product searchById(String productId) {
		String sql = "select * from Product where product_id = ?";

		try (
				  // get connection to database
				  Connection connection = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = connection.prepareStatement(sql);) {
			// set the parameter
			stmt.setString(1, productId);

			// execute the query
			ResultSet rs = stmt.executeQuery();

			// query only returns a single result, so use 'if' instead of 'while'
			if (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				String category = rs.getString("category");
				BigDecimal quantity = rs.getBigDecimal("quantity_in_stock");
				BigDecimal price = rs.getBigDecimal("list_price");

				return new Product(id, name, description, category, price, quantity);

			} else {
				// no Product matches given ID so return null
				return null;
			}

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}

	}
	
}