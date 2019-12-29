package com.aem.cust.core;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class ComplaintImpl implements ComplaintInter{
	
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Reference
    private DataSourcePool source;

	@Override
	public String getComplaintDataSQL() {
		DataSource dataSource = null;
        Connection c = null;
        String query = "";
        Complaints complaint = null;
        List<Complaints> complaintList = new ArrayList<Complaints>();
        try{
             
            //Query data from the Employee table located in MySQL
            c =  getConnection();
          
             ResultSet rs = null;
             Statement s = c.createStatement();
             Statement scount = c.createStatement();
                
             //Use prepared statements to protected against SQL injection attacks
             PreparedStatement pstmt = null;
             PreparedStatement ps = null; 
                                                    
            //Specify the SQL Statement to query data from, the empployee table
             query = "Select name,address,job,age,start,salary FROM employee";
             
             pstmt = c.prepareStatement(query); 
             rs = pstmt.executeQuery();
             
             while (rs.next()) 
             {
                 //For each employee record-- create an Employee instance
            	 complaint = new Complaints();
                   
                 //Populate Employee object with data from MySQL
            	 complaint.setSubject(rs.getString(1));
            	 complaint.setWho(rs.getString(2));
            	 complaint.setCountry(rs.getString(3));
            	 complaint.setState(rs.getString(4));
            	 complaint.setCity(rs.getString(5));
            	 complaint.setName(rs.getString(6));
            	 complaint.setMessage(rs.getString(7));
            	 complaint.setContactNo(rs.getString(8));  
                //Push the Employee Object to the list
            	 complaintList.add(complaint);
       
                    
             }            
             
          return convertToString(toXml(complaintList));  
        }
        catch (Exception e)
        {
            e.printStackTrace(); 
        }
        
       return null;
	}
	
	 private Connection getConnection()
     {
              DataSource dataSource = null;
              Connection con = null;
              try
              {
                  //Inject the DataSourcePool right here! 
                  log.info("GET CONNECTION");
                  dataSource = (DataSource) source.getDataSource("complaints");
                  con = dataSource.getConnection();
                   
                  log.info("CONNECTION is returned");
                  return con;
                    
                }
              catch (Exception e)
              {
                  e.printStackTrace(); 
              }
                  return null; 
     }
	 
	 private Document toXml(List<Complaints> complaintList) {
	        try
	        {
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.newDocument();
	                             
	            //Start building the XML to pass back to the AEM client
	            Element root = doc.createElement( "Complaintss" );
	            doc.appendChild( root );
	                          
	            //Get the elements from the collection
	            int custCount = complaintList.size();
	                
	            //Iterate through the collection to build up the DOM           
	             for ( int index=0; index < custCount; index++) {
	             
	                 //Get the Employee object from the collection
	            	 Complaints mycomplaint = (Complaints)complaintList.get(index);
	                                  
	                 Element Complaint = doc.createElement( "Complaint" );
	                 root.appendChild( Complaint );
	                                   
	                 //Add rest of data as child elements to Employee
	                 //Set Name
	                 Element subject = doc.createElement( "Subject" );
	                 subject.appendChild( doc.createTextNode(mycomplaint.getName() ) );
	                 Complaint.appendChild( subject );
	                                                                      
	                 //Set Address
	                 Element who = doc.createElement( "Who" );
	                 who.appendChild( doc.createTextNode(mycomplaint.getWho() ) );
	                 Complaint.appendChild( who );
	                                
	                 //Set position
	                 Element country = doc.createElement( "Country" );
	                 country.appendChild( doc.createTextNode(mycomplaint.getCountry() ) );
	                 Complaint.appendChild( country );
	                               
	                 //Set age
	                 Element state = doc.createElement( "State" );
	                 state.appendChild( doc.createTextNode(mycomplaint.getState()) );
	                 Complaint.appendChild( state );
	                  
	                 //Set Date
	                 Element city = doc.createElement( "City" );
	                 city.appendChild( doc.createTextNode(mycomplaint.getCity()) );
	                 Complaint.appendChild( city );
	                  
	                 //Set sal
	                 Element name = doc.createElement( "Name" );
	                 name.appendChild( doc.createTextNode(mycomplaint.getName()) );
	                 Complaint.appendChild( name );
	                 
	                 Element message = doc.createElement( "Message" );
	                 message.appendChild( doc.createTextNode(mycomplaint.getMessage()) );
	                 Complaint.appendChild( message );
	                  
	                 //Set sal
	                 Element contact = doc.createElement( "ContactNo" );
	                 contact.appendChild( doc.createTextNode(mycomplaint.getContactNo()) );
	                 Complaint.appendChild( contact );
	              }
	                        
	        return doc;
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	            }
	        return null;
	        }
	             
	             
	        private String convertToString(Document xml)
	        {
	        try {
	           Transformer transformer = TransformerFactory.newInstance().newTransformer();
	          StreamResult result = new StreamResult(new StringWriter());
	          DOMSource source = new DOMSource(xml);
	          transformer.transform(source, result);
	          return result.getWriter().toString();
	        } catch(Exception ex) {
	              ex.printStackTrace();
	        }
	          return null;
	         }

}
