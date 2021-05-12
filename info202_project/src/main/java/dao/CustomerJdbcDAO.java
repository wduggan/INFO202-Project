/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import domain.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dugwi731
 */
public class CustomerJdbcDAO implements CustomerDAO {

	private String uri = DbConnection.getDefaultConnectionUri();

	public CustomerJdbcDAO() {
	}

	public CustomerJdbcDAO(String uri) {
		this.uri = uri;
	}

	@Override
	public void saveCustomer(Customer customer) {
		String sql = "insert into Customer (Customer_ID, Username, FirstName, SurName, Email_Address, Shipping_Address, Password) values (?,?,?,?,?,?,?)";

		try (
				  // get connection to databaseurl
				  Connection dbCon = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
			// copy the data from the product domain object into the SQL parameters
			stmt.setObject(1, null);
			stmt.setString(2, customer.getUsername());
			stmt.setString(3, customer.getFirstName());
			stmt.setString(4, customer.getSurname());
			stmt.setString(5, customer.getEmailAddress());
			stmt.setString(6, customer.getShippingAddress());
			stmt.setString(7, customer.getPassword());

			stmt.executeUpdate();  // execute the statement

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}
	}

	@Override
	public Customer getCustomer(String username) {
		String sql = "select * from Customer where username = ?";

		try (
				  // get connection to database
				  Connection connection = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = connection.prepareStatement(sql);) {
			// set the parameter
			stmt.setString(1, username);

			// execute the query
			ResultSet rs = stmt.executeQuery();

			// query only returns a single result, so use 'if' instead of 'while'
			if (rs.next()) {
				Integer customerID = rs.getInt("Customer_ID");
				String userName = rs.getString("Username");
				String firstname = rs.getString("FirstName");
				String surname = rs.getString("SurName");
				String email = rs.getString("Email_Address");
				String shippingAddress = rs.getString("Shipping_Address");
				String password = rs.getString("Password");

				return new Customer(customerID, userName, firstname, surname, email, shippingAddress, password);

			} else {
				// no Product matches given ID so return null
				return null;
			}

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}

	}

	@Override
	public Boolean validateCredentials(String username, String password) {
		String sql = "select * from Customer where username = ? and password = ?";

		try (
				  // get connection to database
				  Connection connection = DbConnection.getConnection(uri);
				  // create the statement
				  PreparedStatement stmt = connection.prepareStatement(sql);) {
			// set the parameter
			stmt.setString(1, username);
			stmt.setString(2, password);

			// execute the query
			ResultSet rs = stmt.executeQuery();

			// query only returns a single result, so use 'if' instead of 'while'
			if (rs.next()) {
				return true;
			} else {
				// no Product matches given ID so return null
				return false;
			}

		} catch (SQLException ex) {  // we are forced to catch SQLException
			// don't let the SQLException leak from our DAO encapsulation
			throw new DAOException(ex.getMessage(), ex);
		}

	}

}
