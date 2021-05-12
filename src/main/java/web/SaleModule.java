/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dao.SaleDAO;
import domain.Sale;
import domain.SaleItem;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.Jooby;
import org.jooby.Status;

/**
 *
 * @author dugwi731
 */
public class SaleModule extends Jooby {

	public SaleModule(SaleDAO saleDao) {
		post("/api/sales", (req, rsp) -> {
			Sale sale = req.body().to(Sale.class);
			System.out.println(sale);
			saleDao.save(sale);
			rsp.status(Status.CREATED);

			StringBuffer message = new StringBuffer("Hi " + sale.getCustomer().getFirstName() + ", you have completed a sale under the ID: " + sale.getSaleID() + ". This included: ");

			for (SaleItem item : sale.getItems()) {
				message.append("\nProduct: ").append(item.getProduct()).append("\nItem price: ").append(item.getSalePrice()).append("\nQuantity: ").append(item.getQuantityPurchased()).append("\nTotal price of items: ").append(item.getItemTotal());
			}
			//Add new line between each new product at some point//////////////////////////////////
			CompletableFuture.runAsync(() -> {
				try {
				SimpleEmail email = new SimpleEmail();
				email.setHostName("localhost");
				email.setSmtpPort(2525);
				email.setFrom("user@gmail.com");
				email.setSubject("TestMail");
				email.setMsg(message.toString());
				email.addTo("foo@bar.com");
				email.send();
				} catch (EmailException ex) {
					System.out.println(ex.getMessage());
				}
			});
		});
	}
}
