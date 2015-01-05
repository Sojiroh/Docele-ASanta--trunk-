package cl.facele.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
	 
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.apache.log4j.Logger;
	 
public class SetCopiesNumberForPrintJob {
	static Logger logger = Logger.getLogger(SetCopiesNumberForPrintJob.class);
     
    public static void main(String[] args) throws Exception {
    	String impresora = "HP LaserJet M3035 mfp PCL6";
    	InputStream is = null;
    	try {
    		 logger.debug("Start...");
    		 DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    		 PrintService printServices[] = PrintServiceLookup.lookupPrintServices(flavor, null);
    		 int num = printServices.length;
    		 PrintService service = null;
    		 for (int i=0; i<num; i++) {
    			 logger.debug("Nombre Impresora: " + printServices[i].getName());
    			 if (printServices[i].getName().toLowerCase().contains(impresora.toLowerCase())) {
    				 service = printServices[i];
    				 break;
    			 }
    		 }
    		 
    		 if (service == null)
    			 throw new Exception("Impresora [" +
    			 		impresora + "] no esta registrada en servidor. No se imprime el documento");
    		 
    		  is = new BufferedInputStream(new FileInputStream("C:\\Users\\Usuario\\FACELE\\tmp\\PDF_1361310781461.pdf"));
    		  
    		  // Create and return a PrintJob capable of handling data from
    		  // any of the supported document flavors.
    		  DocPrintJob printJob = service.createPrintJob();
    		  
    		  // Construct a SimpleDoc with the specified
    		  // print data, doc flavor and doc attribute set.
    		  Doc doc = new SimpleDoc(is, flavor, null);

    		  // set up the attributes
    		  int numCopies = 1;    		  
    		  PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();    		 
    		  attributes.add(new Copies(numCopies));
    		  
    		  // Print a document with the specified job attributes.    		 
    		  printJob.print(doc, attributes);
    		  System.out.println("Exiting app");
    	         
    	} catch (PrintException e) {
    		logger.error(e, e);
    	} catch (Exception e) {
    		logger.error(e, e);
    	} finally {
    		is.close();
    	}
         
    } 
}