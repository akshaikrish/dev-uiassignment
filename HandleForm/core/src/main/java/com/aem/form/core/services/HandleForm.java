package com.aem.form.core.services;

public interface HandleForm {
	
	
	 public void injestFormDataDB(String Subject, String Who, String Country, String State, String City, String Name, String Message, String ContactNo);
	 
	 public void sendmail(String Subject, String Who, String Country, String State, String City, String Name, String Message, String ContactNo);
}
