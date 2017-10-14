package com.adactin.hotelapp.util;



public class OrderNoException extends Exception {

	/*
	 * The class Exception implements the Serializable Interface. The compiler
	 * requires the serialVersionUID to be set to a Type Long value The
	 * Serializable interface is used to to convert the data and the status of
	 * an object into a byte stream. And then write to output as
	 * ObjectOutputStream Class
	 */
	private static final long serialVersionUID = 1L;

	public OrderNoException() {

	}

	// if you extend Exception you need to override the toString() and
	// getMessage()
	@Override
	public String toString() {
		return "OrderNoException Generated ... OMG";
	}

	@Override
	public String getMessage() {
		return "Message : OrderNoException Generated";
	}

}
