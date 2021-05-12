/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import domain.Product;
import domain.Sale;
import domain.SaleItem;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dugwi731
 */
public class SaleJdbcDAO implements SaleDAO {

	private final String url = DbConnection.getDefaultConnectionUri();

	@Override
	public void save(Sale sale) {

		Connection con = DbConnection.getConnection(url);
		try {
			try (
					  PreparedStatement insertSaleStmt = con.prepareStatement(
								 // SQL for saving Sale goes here
								 "insert into Sale (Date, Status, Customer_ID) values (?,?,?)",
								 Statement.RETURN_GENERATED_KEYS
					  );
					  PreparedStatement insertSaleItemStmt = con.prepareStatement(
								 // SQL for saving SaleItem goes here
								 "insert into SaleItem (Quantity_Purchased, Sale_Price, Product_ID, Sale_ID) values (?,?,?,?)");
					  PreparedStatement updateProductStmt = con.prepareStatement(
								 // SQL for updating product quantity goes here
								 "update Product set Quantity_In_Stock = ? where Product_ID = ?");) {

						  // Since saving a Sale involves multiple statements across
						  // multiple tables we need to control the transaction ourselves
						  // to ensure the DB remains consistent.  Turning off auto-commit
						  // effectively starts a new transaction.
						  con.setAutoCommit(false);

						  Customer customer = sale.getCustomer();

						  // #### save the sale ### //
						  // add a date to the sale if one doesn't already exist
						  if (sale.getDate() == null) {
							  sale.setDate(LocalDateTime.now());
						  }

						  // convert sale date into to java.sql.Timestamp
						  LocalDateTime date = sale.getDate();
						  Timestamp timestamp = Timestamp.valueOf(date);

						  // ****
						  // write code here that saves the timestamp and username in the
						  // sale table using the insertSaleStmt statement.
						  // ****
						  insertSaleStmt.setTimestamp(1, timestamp);
						  insertSaleStmt.setString(2, "Order Completed");
						  insertSaleStmt.setInt(3, customer.getCustomerID());

						  insertSaleStmt.executeUpdate();
						  // get the auto-generated sale ID from the database
						  ResultSet rs = insertSaleStmt.getGeneratedKeys();

						  Integer saleId = null;

						  if (rs.next()) {
							  saleId = rs.getInt(1);
							  sale.setSaleID(saleId);
						  } else {
							  throw new DAOException("Problem getting generated sale ID");
						  }

						  // ## save the sale items ## //
						  Collection<SaleItem> items = sale.getItems();

						  // ****
						  // write code here that iterates through the sale items and
						  // saves them using the insertSaleItemStmt statement.
						  // ****
						  for (SaleItem item : items) {
							  insertSaleItemStmt.setBigDecimal(1, item.getQuantityPurchased());
							  insertSaleItemStmt.setBigDecimal(2, item.getSalePrice());
							  insertSaleItemStmt.setString(3, item.getProduct().getProductID());
							  insertSaleItemStmt.setInt(4, sale.getSaleID());
							  insertSaleItemStmt.executeUpdate();
						  }

						  // ## update the product quantities ## //
						  for (SaleItem item : items) {

							  Product product = item.getProduct();

							  // ****
							  // write code here that updates the product quantity using
							  // the updateProductStmt statement.
							  // ****
							  BigDecimal newQuantity = product.getQuantityInStock().subtract(item.getQuantityPurchased());
							  updateProductStmt.setBigDecimal(1, newQuantity);
							  updateProductStmt.setString(2, item.getProduct().getProductID());
							  updateProductStmt.executeUpdate();
						  }

						  // commit the transaction
						  con.setAutoCommit(true);
					  }
		} catch (SQLException ex) {

			Logger.getLogger(SaleJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);

			try {
				// something went wrong so rollback
				con.rollback();

				// turn auto-commit back on
				con.setAutoCommit(true);

				// and throw an exception to tell the user something bad happened
				throw new DAOException(ex.getMessage(), ex);
			} catch (SQLException ex1) {
				throw new DAOException(ex1.getMessage(), ex1);
			}

		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				Logger.getLogger(SaleJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
