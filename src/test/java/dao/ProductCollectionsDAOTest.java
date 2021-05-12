/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Product;
import java.math.BigDecimal;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author dugwi731
 */
public class ProductCollectionsDAOTest {
	
	private ProductDAO dao;
	
	// private ProductCollectionsDAO dao;
   private Product item1;
   private Product item2;
   private Product item3;
	
	@BeforeEach
	public void setUp() {
		
		dao = new ProductJdbcDAO("jdbc:h2:mem:tests;INIT=runscript from 'src/main/java/dao/schema.sql'");

		//dao = new ProductCollectionsDAO();

      item1 = new Product();
      item1.setName("Polkadot Widget");
      item1.setListPrice(new BigDecimal("0.1"));
      item1.setQuantityInStock(new BigDecimal("0.2"));
		item1.setProductID("123");
		item1.setDescription("Pretty colours");
		item1.setCategory("Rabbits");
		
		item2 = new Product();
      item2.setName("Polkadot Widget");
      item2.setListPrice(new BigDecimal("0.1"));
      item2.setQuantityInStock(new BigDecimal("0.2"));
		item2.setProductID("456");
		item2.setDescription("Pretty colours");
		item2.setCategory("Rabbits");
		
		item3 = new Product();
      item3.setName("Polkadot Widget");
      item3.setListPrice(new BigDecimal("0.1"));
      item3.setQuantityInStock(new BigDecimal("0.2"));
		item3.setProductID("WillGraham");
		item3.setDescription("Pretty colours");
		item3.setCategory("Rabbits");
		
      dao.saveProduct(item1);
      dao.saveProduct(item2);
		// dao.saveProduct(item3);
	}
	
	@AfterEach
	public void tearDown() {
		dao.removeProduct(item1);
		dao.removeProduct(item2);
		dao.removeProduct(item3);
	}

	@Test
	public void testSaveProduct() {
		dao.saveProduct(item3);
		assertThat(dao.getProducts(), hasItem(item3));
	}

	@Test
	public void testGetProducts() {
		assertThat(dao.getProducts(), hasItem(item1));
		assertThat(dao.getProducts(), hasItem(item2));
		assertThat(dao.getProducts(), hasSize(2));
	}

	@Test
	public void testRemoveProduct() {
		assertThat(dao.getProducts(), hasItem(item1));
		dao.removeProduct(item1);
		assertThat(dao.getProducts(), not(hasItem(item1)));
	}
	
	@Test
	public void testGetCategories() {
		assertThat(dao.getCategories(), hasItem(item1.getCategory()));
		assertThat(dao.getCategories(), hasItem(item2.getCategory()));
	}
	
	@Test
	public void testSearchByID() {
		assertThat(dao.searchById(item1.getProductID()), is(item1));
		assertThat(dao.searchById(item2.getProductID()), is(item2));
	}

	@Test
	public void testFilterByCategory() {
		assertThat(dao.filterByCategory(item1.getCategory()), hasItem(item1));
		assertThat(dao.filterByCategory(item2.getCategory()), hasItem(item2));
	}
	
}
