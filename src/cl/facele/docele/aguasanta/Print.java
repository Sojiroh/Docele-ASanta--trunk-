package cl.facele.docele.aguasanta;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

import biz.source_code.base64Coder.Base64Coder;

public class Print {
	Logger logger = Logger.getLogger(Print.class);

	public void savePDF(String pdf) throws Exception {
		
		Path pathPdf = Paths.get(System.getProperty("user.home")).resolve("Facele").resolve("tmp");

		if (Files.notExists(pathPdf.toAbsolutePath()))
			Files.createDirectories(pathPdf.toAbsolutePath());

		pathPdf = pathPdf.resolve("PDF_" + Long.toString(System.currentTimeMillis()) + ".pdf");
		logger.debug("Ruta de archivo PDF: " + pathPdf.toString());
		Files.write(pathPdf, Base64Coder.decode(pdf), StandardOpenOption.CREATE_NEW);
	}
	
	public void printToDevice(byte[] documento, String impresora, int copias) throws Exception {
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
    			 		impresora + "] no est� registrada en servidor. No se imprime el documento");
    		 
    		  is = new ByteArrayInputStream(documento);
    		  
    		  // Create and return a PrintJob capable of handling data from
    		  // any of the supported document flavors.
    		  DocPrintJob printJob = service.createPrintJob();
    		  
    		  // Construct a SimpleDoc with the specified
    		  // print data, doc flavor and doc attribute set.
    		  Doc doc = new SimpleDoc(is, flavor, null);

    		  // set up the attributes	  
    		  PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();    		 
    		  attributes.add(new Copies(copias));
    		  
    		  // Print a document with the specified job attributes.    		 
    		  printJob.print(doc, attributes);
    		  logger.info("Exiting app");
    	         
    	} catch (PrintException e) {
    		logger.error(e, e);
    		throw new Exception("Error imprimiendo documentos: " + e.getMessage());
    	} catch (Exception e) {
    		logger.error(e, e);
    		throw new Exception("Error imprimiendo documentos: " + e.getMessage());
    	} finally {
    		is.close();
    	}
         
    
		
	}


}
