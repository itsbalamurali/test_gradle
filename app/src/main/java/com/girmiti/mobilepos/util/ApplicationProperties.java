package com.girmiti.mobilepos.util;



import com.girmiti.mobilepos.R;

import java.util.Properties;

public class ApplicationProperties extends Properties {


	
	private static ApplicationProperties instance;
	
	private ApplicationProperties() {
		super();
	}

	public static ApplicationProperties getInstance() {
		if (instance == null) {
			instance = new ApplicationProperties();
			instance.loadProperties();
		}
		return instance;
	}

	private void loadProperties() {
		loadProperties(R.raw.endpoint_uri);
		loadProperties(R.raw.app);
	}
	
	private void loadProperties(int resourceId) {
        Utils.loadProperties(resourceId,this);
	}
}
