/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import domain.Product;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author dugwi731
 */
public class ProductCollectionsDAO implements ProductDAO {
	
	private static final Collection<Product> productsList = new HashSet<Product>();
	private static final Collection<String> categoryList = new HashSet<String>();
	private static final Map<String, Product> productIds = new HashMap<String, Product>();
	private static final Multimap<String, Product> filterCategories = HashMultimap.create();

	
	@Override
	public void saveProduct(Product product){
		productsList.add(product);
		categoryList.add(product.getCategory());
		productIds.put(product.getProductID(), product);
		filterCategories.put(product.getCategory(), product);
	}
	
	@Override
	public Collection<Product> getProducts(){
		return productsList;
	}
	
	@Override
	public void removeProduct(Product product){
		productsList.remove(product);
	}
	
	@Override
	public Collection<String> getCategories() {
		return categoryList;
	}
	
	@Override
	public Product searchById(String productId) {
		// return productIds.get(productId);
		for (Product product : productsList) {
			if (product.getProductID().equals(productId)) {
				return product;
			}
		}
		return null;
	}

	@Override
	public Collection<Product> filterByCategory(String category) {
		return filterCategories.get(category);
	}
}
