package com.ahwers.shop.services;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class ShoppingApplication extends Application {
	
	public ShoppingApplication(@Context ServletContext servletContext) throws Exception {
		ApplicationResourcePathRepository.instantiateSingleton(servletContext);
	}
//   private Set<Object> singletons = new HashSet<Object>();
//
//   public ShoppingApplication() {
//      singletons.add(new CustomerResource());
//   }
//
//   @Override
//   public Set<Object> getSingletons() {
//      return singletons;
//   }
}
