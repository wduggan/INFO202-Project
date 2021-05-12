/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

"use strict";

class SaleItem {
	constructor(product, quantity) {
		// only set the fields if we have a valid product
		if (product) {
			this.product = product;
			this.quantityPurchased = quantity;
			this.salePrice = product.listPrice;
		}
	}
	getItemTotal() {
		return this.salePrice * this.quantityPurchased;
	}

}

class ShoppingCart {
	constructor() {
		this.items = new Array();
	}
	reconstruct(sessionData) {
		for (let item of sessionData.items) {
			this.addItem(Object.assign(new SaleItem(), item));
		}
	}
	getItems() {
		return this.items;
	}
	addItem(item) {
		this.items.push(item);
	}
	setCustomer(customer) {
		this.customer = customer;
	}
	getTotal() {
		let total = 0;
		for (let item of this.items) {
			total += item.getItemTotal();
		}
		return total;
	}
}



// create a new module, and load the other pluggable modules
var module = angular.module('ShoppingApp', ['ngResource', 'ngStorage']);

module.factory('productAPI', function ($resource) {
	return $resource('/api/products/:id');
});

module.factory('categoryAPI', function ($resource) {
	return $resource('/api/categories/:cat');
});

module.factory('registerAPI', function ($resource) {
	return $resource('/api/register');
});

module.factory('signInAPI', function ($resource) {
   return $resource('/api/customers/:username');
});

module.factory('saleAPI', function ($resource) {
   return $resource('/api/sales');
});

module.factory('cart', function ($sessionStorage) {
    let cart = new ShoppingCart();

    // is the cart in the session storage?
    if ($sessionStorage.cart) {

        // reconstruct the cart from the session data
        cart.reconstruct($sessionStorage.cart);
    }

    return cart;
});




module.controller('ProductController', function (productAPI, categoryAPI) {
	this.products = productAPI.query();
	this.categories = categoryAPI.query();

	// click handler for the category filter buttons
	this.selectCategory = function (selectedCat) {
		this.products = categoryAPI.query({"cat": selectedCat});
	};
	
	// click handler for the category filter buttons
	this.selectAllCategories = function () {
		this.products = productAPI.query();
	};
});


module.controller('CustomerController', function (registerAPI, $window, signInAPI, $sessionStorage) {
	this.signInMessage = "Please sign in to continue.";
	if ($sessionStorage.customer) {
		this.welcome = "Welcome " + $sessionStorage.customer.firstName;
	}
	
	this.registerCustomer = function (customer) {
		registerAPI.save(null, customer,
			// success callback
			function () {
				$window.location = 'signIn.html';
			},
			// error callback
			function (error) {
				console.log(error);
			}
		);
	};
	
// alias 'this' so that we can access it inside callback functions
	let ctrl = this;
	this.signIn = function (username, password) {
		// get customer from web service
		signInAPI.get({'username': username},
		// success callback
		function (customer) {
			// also store the retrieved customer
			$sessionStorage.customer = customer;
				// redirect to home
				$window.location = 'signedInIndex.html';
		},
		// fail callback
		function () {
			ctrl.signInMessage = 'Sign in failed. Please try again.';
		}
	);
	};
	
	this.signOut = function () {
		$sessionStorage.$reset();
		// Redirect to home
		$window.location = 'index.html';
	};
	
	this.checkSignIn = function () {
   // has the customer been added to the session?
   if ($sessionStorage.customer) {
      this.signedIn = true;
      this.welcome = "Welcome " + $sessionStorage.customer.firstName;
    } else {
      this.signedIn = false;
    }
};
	
});

module.controller('CartController', function (cart, $window, $sessionStorage, saleAPI) {
	this.items = cart.getItems();
	this.total = cart.getTotal();
	this.selectedProduct = $sessionStorage.selectedProduct;
	
			  this.storeProduct = function (product) {
				  // also store the retrieved product
				  $sessionStorage.selectedProduct = product;
				  // redirect to home
				  $window.location = 'quantityToPurchase.html';
			  };
			  
				this.addToCart = function (quantity) {
				  // also store the retrieved product
				  let product = $sessionStorage.selectedProduct;
				  
				  let saleItem = new SaleItem (product, quantity);
				  cart.addItem(saleItem);
				  $sessionStorage.cart = cart;
				  
				  // redirect to home
				  $window.location = 'cart.html';
			  };
			  
				this.checkOut = function () {
				  // also store the retrieved product
				  let customer = $sessionStorage.customer;
				  cart.setCustomer(customer);
				  saleAPI.save(cart);
				  delete $sessionStorage.cart;

				  // redirect to 'Thank you for your order page'
				  $window.location = 'orderConfirm.html';
			  };
	
});
