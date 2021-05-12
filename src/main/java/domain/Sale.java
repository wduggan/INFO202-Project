/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 *
 * @author dugwi731
 */
public class Sale {
	
	private Integer saleID;
	private LocalDateTime date;
	private String status;
	private Collection<SaleItem> items;
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}
	
	public Collection<SaleItem> getItems() {
		return items;
	}

	public Integer getSaleID() {
		return saleID;
	}

	public void setSaleID(Integer saleID) {
		this.saleID = saleID;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public BigDecimal getTotal() {
		BigDecimal totalPrice = new BigDecimal(0);
		for (SaleItem item : items){
			totalPrice = totalPrice.add(item.getItemTotal());
		}
		return totalPrice;
	}
	
	public void addItem(SaleItem saleItem){
		items.add(saleItem);
	}

	@Override
	public String toString() {
		return "Sale{" + "saleID=" + saleID + ", date=" + date + ", status=" + status + ", items=" + items + ", customer=" + customer + '}';
	}
	
	
	
}
