package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Contact;
import play.Logger;
import play.modules.excel.RenderExcel;
import play.mvc.Controller;
import play.mvc.With;

@With(ExcelControllerHelper.class)
public class Application extends Controller {

    public static void index() {
    	List<Contact> contacts = Contact.findAll();
        render(contacts);
    }
    
    public static void generateNameCard(Long id) {
        Logger.info("generateNameCard");
    	Contact person = Contact.findById(id);
    	request.format = "xlsx";
        renderArgs.put("__EXCEL_FILE_NAME__", person.getId() + ".xlsx");
    	render(person);
    }

    public static void grid_template(Long id){
        Logger.info("generateNameCard");
        renderArgs.put(RenderExcel.RA_DYNAMIC_COLUMNS, true);
        List headers = Arrays.asList( "First name", "Last name", "Address", "Amount", "Birthday");
        renderArgs.put(RenderExcel.RA_DYNAMIC_HEADERS, headers);
        String propertyNames = "firstName,lastName,address,amount,birthday";
        renderArgs.put(RenderExcel.RA_DYNAMIC_PROPERTY_NAMES, propertyNames);
        Contact person = Contact.findById(id);
        renderArgs.put(RenderExcel.RA_FILENAME, person.getId() + ".xlsx");
        request.format = "xlsx";
        List<Contact> contacts = Contact.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.US);
        for(Contact c : contacts){
            c.birthday = new Date();
        }
        renderArgs.put("data", contacts);
        render(contacts);
    }

    public static void generateAddressBook() {
        Logger.info("generateAddressBook");
    	List<Contact> contacts = Contact.findAll();
    	Date date = new Date();
    	String __EXCEL_FILE_NAME__ = "address_book.xlsx";
    	renderArgs.put(RenderExcel.RA_ASYNC, true);
    	request.format = "xlsx";
        render(__EXCEL_FILE_NAME__, contacts, date);
        Logger.info("generateAddressBook exit");
    }

}
