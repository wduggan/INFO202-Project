/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import dao.ProductDAO;
import domain.Product;
import helpers.SimpleListModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import static junit.framework.Assert.assertTrue;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import static org.assertj.swing.core.matcher.DialogMatcher.withTitle;
import static org.assertj.swing.core.matcher.JButtonMatcher.withText;
import org.assertj.swing.fixture.DialogFixture;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author dugwi731
 */
public class ProductViewerTest {

	private ProductDAO dao;
	private DialogFixture fixture;
	private Robot robot;

	Product p1234 = new Product("1234", "1234", "1234", "c1", BigDecimal.ZERO, BigDecimal.TEN);
	Product p4321 = new Product("4321", "4321", "4321", "c2", BigDecimal.ZERO, BigDecimal.TEN);

	public ProductViewerTest() {
	}

	@BeforeEach
	public void setUp() {
		robot = BasicRobot.robotWithNewAwtHierarchy();

		// Slow down the robot a little bit - default is 30 (milliseconds).
		// Do NOT make it less than 10 or you will have thread-race problems.
		robot.settings().delayBetweenEvents(75);

		// add some majors for testing with
		Collection<String> categories = new ArrayList<>();
		categories.add("Art");
		categories.add("Music");

		Collection<Product> products = new ArrayList<>();
		products.add(p1234);
		products.add(p4321);

		// create a mock for the DAO
		dao = mock(ProductDAO.class);

		// stub the getMajors method to return the test majors
		when(dao.getCategories()).thenReturn(categories);
		when(dao.getProducts()).thenReturn(products);

		when(dao.searchById("1234")).thenReturn(p1234);

		// stub the removeProduct method
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				// remove the product from the collection that getProducts() uses
				products.remove(p1234);
				return null;
			}
		}).when(dao).removeProduct(p1234);
		
	}

	@AfterEach
	public void tearDown() {
		// clean up fixture so that it is ready for the next test
		fixture.cleanUp();
	}

	@org.junit.jupiter.api.Test
	public void testSearch() {
		// create the dialog passing in the mocked DAO
		ProductViewer dialog = new ProductViewer(null, true, dao);

		// use AssertJ to control the dialog
		fixture = new DialogFixture(robot, dialog);

		// show the dialog on the screen, and ensure it is visible
		fixture.show().requireVisible();

		// click the dialog to ensure the robot is focused in the correct window
		// (can get confused by multi-monitor and virtual desktop setups)
		fixture.click();

		// enter some details into the UI components
		fixture.textBox("txtSearch").enterText("1234");

		// click the search button
		fixture.button("buttonSearch").click();

		// verify that the DAO.save method was called, and capture the passed student
		verify(dao).searchById("1234");

		// get the model
		SimpleListModel model = (SimpleListModel) fixture.list("listViewProducts").target().getModel();

		// check the contents
		assertTrue("list contains the expected product", model.contains(p1234));
		assertEquals("list contains the correct number of products", 1, model.getSize());
	}

	@org.junit.jupiter.api.Test
	public void testView() {
		// create the dialog passing in the mocked DAO
		ProductViewer dialog = new ProductViewer(null, true, dao);

		// use AssertJ to control the dialog
		fixture = new DialogFixture(robot, dialog);

		// show the dialog on the screen, and ensure it is visible
		fixture.show().requireVisible();

		// click the dialog to ensure the robot is focused in the correct window
		// (can get confused by multi-monitor and virtual desktop setups)
		fixture.click();

		// verify that the DAO.save method was called, and capture the passed student
		verify(dao).getProducts();

		// get the model
		SimpleListModel model = (SimpleListModel) fixture.list("listViewProducts").target().getModel();

		// check the contents
		assertTrue("list contains the expected product", model.contains(p1234));
		assertTrue("list contains the expected product", model.contains(p4321));
		assertEquals("list contains the correct number of products", 2, model.getSize());
	}

	@org.junit.jupiter.api.Test
	public void testDelete() {
		// create the dialog passing in the mocked DAO
		ProductViewer dialog = new ProductViewer(null, true, dao);

		// use AssertJ to control the dialog
		fixture = new DialogFixture(robot, dialog);

		// show the dialog on the screen, and ensure it is visible
		fixture.show().requireVisible();

		// click the dialog to ensure the robot is focused in the correct window
		// (can get confused by multi-monitor and virtual desktop setups)
		fixture.click();

		// enter some details into the UI components
		fixture.list("listViewProducts").selectItem(p1234.toString());

		// click the search button
		fixture.button("buttonDelete").click();

		// ensure a confirmation dialog is displayed
		DialogFixture confirmDialog = fixture.dialog(withTitle("Confirm Deletion").andShowing()).requireVisible();

		// click the Yes button on the confirmation dialog
		confirmDialog.button(withText("Yes")).click();

		// Pause.pause(3, TimeUnit.SECONDS);
		// verify that the DAO.save method was called, and capture the passed student
		verify(dao).removeProduct(p1234);

		// get the model
		SimpleListModel model = (SimpleListModel) fixture.list("listViewProducts").target().getModel();

		// check the contents
		assertTrue("list contains the expected product", !model.contains(p1234));
		assertEquals("list contains the correct number of products", 1, model.getSize());
	}

}
