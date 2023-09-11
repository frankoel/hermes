package com.application.hermesteamsphere.util;

import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component("constantesBean")
public class Constantes {

	static final ResourceBundle CONSTANTES_BUNDLE;

	static {
		CONSTANTES_BUNDLE = ResourceBundle.getBundle("constantes");
	}

	public static final String HEADER = CONSTANTES_BUNDLE.getString("HEADER");
    public static final String PREFIX = CONSTANTES_BUNDLE.getString("PREFIX");
    public static final String SECRET = CONSTANTES_BUNDLE.getString("SECRET");
    public static final String TOKEN_ID = CONSTANTES_BUNDLE.getString("TOKEN_ID");

}