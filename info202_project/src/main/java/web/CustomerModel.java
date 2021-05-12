/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dao.CustomerCollectionsDAO;
import dao.CustomerDAO;
import domain.Customer;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Status;

/**
 *
 * @author dugwi731
 */
public class CustomerModel extends Jooby {

	CustomerDAO customerDao = new CustomerCollectionsDAO();

	public CustomerModel(CustomerDAO customerDao) {
		get("/api/customers/:username", (req) -> {
			String username = req.param("username").value();
			
			if (customerDao.getCustomer(username) == null) {
				return new Result().status(Status.NOT_FOUND);
			} else {
				return customerDao.getCustomer(username);
			}
		});
		
		post("/api/register", (req, rsp) -> {
			Customer customer = req.body().to(Customer.class);
			customerDao.saveCustomer(customer);
			rsp.status(Status.CREATED);
		});
	}
}
