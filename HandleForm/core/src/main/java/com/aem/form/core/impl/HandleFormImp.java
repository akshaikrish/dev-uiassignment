package com.aem.form.core.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.day.cq.mailer.MessageGatewayService;
import com.aem.form.core.services.HandleForm;


@Component(service = HandleForm.class, immediate = true)
public class HandleFormImp implements HandleForm {
	
	 protected final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	 @Reference
	    private MessageGatewayService messageGatewayService;
     
    @Reference
    private DataSourcePool source;

	@Override
	public void injestFormDataDB(String Subject, String Who, String Country, String State, String City, String Name,
			String Message, String ContactNo) {
		
		log.info("DB Data posted from an AEM adaptive form - Subject: "+Subject +" I am a : "+Who +" Country : "+Country +" State: "+State +" City:"+ City +" Name:"+Name +" Contact No:"+ContactNo ) ;
		sendmail(Subject,Who,Country, State,City,Name,Message,ContactNo);
        Connection c = null;
//        c =  getConnection();
//        int rowCount= 0; 
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date(); 
        log.info(formatter.format(date));
        try {
                          
              // Create a Connection object
//              c =  getConnection();
        	
        	String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = " jdbc:mysql://localhost:3306/aemforms?allowMultiQueries=true";
            Class.forName(myDriver);
            c = DriverManager.getConnection(myUrl, "root", "password");
          
              log.info("try1");
//               ResultSet rs = null;
//               Statement s = c.createStatement();
//               Statement scount = c.createStatement();
                   
               //Use prepared statements to protected against SQL injection attacks
               PreparedStatement pstmt = null;
               PreparedStatement ps = null; 
                             
//               int pk = Integer.parseInt(customer_ID);    
//               int intZIP =Integer.parseInt(customer_ZIPCode);    
                 
//               log.info("****** THe PK IS is "+pk); 
                 
               String insert = "INSERT INTO complaints(Subject, Who, Country, State, City, Name, Message, ContactNo) VALUES(?, ?,?,?,?,?);";
               ps = c.prepareStatement(insert);
                 
               ps.setString(1,Subject); 
               ps.setString(2, Who);
               ps.setString(3, Country);
               ps.setString(4, State);
               ps.setString(5, City);
               ps.setString(6, Name);
               ps.setString(7, Message);
               ps.setString(8, ContactNo);
//               ps.setDate(9, formatter.format(date));
               ps.execute();
               log.info(formatter.format(date));
                
        }
        catch (Exception e) {
          e.printStackTrace();
          log.info("First catch");
         }
        finally {
          try
          {
            c.close();
          }
            
            catch (SQLException e) {
              e.printStackTrace();
            }
		
	}

}
	private Connection getConnection()
    {
             DataSource dataSource = null;
             Connection con = null;
             try
             {
                 //Inject the DataSourcePool right here! 
                 dataSource = (DataSource) source.getDataSource("AEMform");
                 con = dataSource.getConnection();
                 return con;
                   
               }
             catch (Exception e)
             {
                 e.printStackTrace(); 
                 log.info("Connection error");
             }
                 return null; 
    }
	
	
	@Override
	public void sendmail(String Subject, String Who, String Country, String State, String City, String Name,
			String Message1, String ContactNo) {
		log.info("sendmail called");
		Properties props = new Properties();    
        props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");    
        //get Session   
        log.info("prop success");
        Session session = Session.getInstance(props,    
         new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication("akshaikrishna@gmail.com","rouwellstat");  
         }    
        });  
        
        
        try {    
        	log.info("mail try");
            MimeMessage message = new MimeMessage(session);    
            message.addRecipient(Message.RecipientType.TO,new InternetAddress("akshaikrishna@gmail.com"));     
            message.setSubject("New Entry");    
            message.setText("Subject : "+Subject +"\nI am a : "+Who +"\nCountry : "+Country +"\nState : "+State +"\nCity :"+ City +"\nName :"+Name +"\nContact No :"+ContactNo+"\nMessage : "+Message1);    
            //send message  
            Transport.send(message);    
            log.info("message sent successfully");    
           } catch (MessagingException e) {
        	   log.info("mail not send");
        	   throw new RuntimeException(e);
           } 
        
		
		
	}
           
  }
