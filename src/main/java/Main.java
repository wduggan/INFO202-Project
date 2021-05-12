/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.ProductJdbcDAO;
import gui.MainMenu;

/**
 *
 * @author dugwi731
 */
public class Main {

	private static final ProductJdbcDAO dao = new ProductJdbcDAO();
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		// create the frame instance
		MainMenu frame = new MainMenu(dao);
		// centre the frame on the screen
		frame.setLocationRelativeTo(null);
		// show the frame
		frame.setVisible(true);
	}

}

