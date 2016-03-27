package com.contact.product;

import java.util.List;

public interface ProductSearch<P extends Product> {
	
	
	public boolean start();
	
	public List<P> search(String query);
}
