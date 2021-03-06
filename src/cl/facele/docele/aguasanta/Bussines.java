package cl.facele.docele.aguasanta;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.print.PrintServiceLookup;

import org.apache.log4j.Logger;

import cl.facele.docele.soap.logica.Soap;

import biz.source_code.base64Coder.Base64Coder;


public class Bussines extends Thread {
	Logger logger = Logger.getLogger(Bussines.class);
	private final String HOME_WORK = "DIRECTORIO.SOURCE";
	private final String PRINTER = "PRINTER.DEVICE.";	//DEFAULT
	private final String COPIAS = "PRINTER.COPIAS.";	//DEFAULT

	public void run() {
		logger.debug("Start...");
		Path pathDirectorio = Paths.get(Resource.getString(HOME_WORK));
		
		logger.debug("Directorio HOME_WORK: " + pathDirectorio);
		if (Files.notExists(pathDirectorio)) {
			logger.error("Directorio [" +
					pathDirectorio.toString() + "] no existe. Finaliza proceso");
			return;
		}
		

		DirectoryStream<Path> directory = null;
		
		Print print = new Print();
		ProcesaTXT transforma = new ProcesaTXT();
		try {
			directory = Files.newDirectoryStream(pathDirectorio);

			Soap soap = new Soap(Soap.SOAP_SFE);
			
			 for (Path filePath : directory) {
				 if (Files.isDirectory(filePath))
					 continue;
				 logger.debug("Nombre archivo: " + filePath.toString());
				 
				 Bean bean = null;
				 String pdf_merito = null;
				 String pdf = null;
				 try {
					 bean = transforma.procesaTXT(filePath);
					 logger.debug("contenidoTXT: " + bean.getContenidoTXT());
					 soap.generaDTE(bean.getRutEmisor(), "999999999", bean.getContenidoTXT());
					 bean.setTipoDocumento(soap.tipoDTE);
					 bean.setFolio(soap.folioDTE);
					 moveOK(filePath);
				 } catch (Exception e) {
					 logger.error("Error no se emitio documento: " + e, e);
					 if (e.getMessage().contains("Failed to access"))
						 continue;
					 moveError(filePath, e.getMessage());
				 }
				 
				 //impresion
				 try {
					 if (bean.getTipoDocumento() < 53) {
						 pdf_merito = soap.getDTE(bean.getRutEmisor(), bean.getTipoDocumento(), bean.getFolio(), "PDF", true);
						 print.savePDF(pdf_merito);
					 }

					 pdf = soap.getDTE(bean.getRutEmisor(), bean.getTipoDocumento(), bean.getFolio(), "PDF", false);
					 print.savePDF(pdf);
					 
					 String impresora = getPrinterDevice(bean.getTipoDocumento());
					 int copiasImpresion = getNumeroCopias(bean.getTipoDocumento());

					 //Valida hay copias por imprimir
					 if (copiasImpresion == 0) {
						 logger.info("No se imprime.");
						 continue;
					 }

					 logger.debug("pdf: " +pdf);
					 if (pdf_merito != null) {
						 print.printToDevice(Base64Coder.decode(pdf), impresora, 1);
						 copiasImpresion--;
					 }
					 for (int i=0; i<copiasImpresion; i++ )
						 print.printToDevice(Base64Coder.decode(pdf), impresora, 1);
					 
					 
				 } catch (Exception e) {
					 logger.error(e, e);
				 }
			 }
		} catch (Exception e1) {
			logger.error(e1, e1);
		} finally {
			try {
				directory.close();
			} catch (IOException e) {
				logger.error(e, e);
			}
		}
		
		
	}

	private int getNumeroCopias(int tipoDocumento) {
		//Numero de copias para X tipo de documento
		if (Resource.getInteger(COPIAS + Integer.toString(tipoDocumento)) != null)
			return Resource.getInteger(COPIAS + Integer.toString(tipoDocumento));

		//Numero de copias por defecto
		if (Resource.getInteger(COPIAS + "DEFAULT") != null)
			return Resource.getInteger(COPIAS + "DEFAULT");
		
		//no copias.
		return 0;
	}

	private String getPrinterDevice(int tipoDocumento) {
		//Impresora para tipo de documento especifica
		String namePriterDevice = Resource.getString(PRINTER + Integer.toString(tipoDocumento));
		if (namePriterDevice != null && !namePriterDevice.isEmpty())
			return namePriterDevice;
		
		//Impresora por defecto para todos los documentos
		namePriterDevice = Resource.getString(PRINTER + "DEFAULT");
		if (namePriterDevice != null && !namePriterDevice.isEmpty())
			return namePriterDevice;
		
		//Impresora por defecto del equipo
		return PrintServiceLookup.lookupDefaultPrintService().getName();
	}

	private void moveOK(Path filePath) {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String periodo = formatter.format(currentDate.getTime());
		Path destinoDir = filePath.getParent().resolve("OK").resolve(periodo);
			try {
				if (Files.notExists(destinoDir))
					Files.createDirectories(destinoDir.toAbsolutePath());

				int n = 0;
				String nombreFile = filePath.getFileName().toString();
				String tmp = null;
				while (true) {
					n++;
					logger.debug(tmp);
					if (n == 1 && Files.notExists(destinoDir.resolve(filePath.getFileName()))) {
						break;
					}
					
					tmp = (nombreFile + ".tmp").split("\\.")[0] + "_" + Integer.toString(n) + ".txt";
					if (Files.notExists(destinoDir.resolve(tmp))) {
						nombreFile = tmp;
						break;
					}

				}
				
				Files.move(filePath, destinoDir.resolve(nombreFile));
			} catch (IOException e) {
				logger.error(e,e);
			}
		
		
		
	}

	private void moveError(Path filePath, String motivo) {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String periodo = formatter.format(currentDate.getTime());
		Path destinoDir = filePath.getParent().resolve("ERROR").resolve(periodo);
			try {
				if (Files.notExists(destinoDir))
					Files.createDirectories(destinoDir.toAbsolutePath());
				
				int n = 0;
				String nombreFile = filePath.getFileName().toString();
				String tmp;
				logger.debug("nombre Archivo: " + nombreFile);
				while (true) {
					n++;
					
					if (n == 1 && Files.notExists(destinoDir.resolve(filePath.getFileName()))) {
						break;
					}
					tmp = (nombreFile + ".tmp").split("\\.")[0] + "_" + Integer.toString(n) + ".txt";
					if (Files.notExists(destinoDir.resolve(tmp))) {
						nombreFile = tmp;
						break;
					}
				}
				
				Files.move(filePath, destinoDir.resolve(nombreFile));
				Files.write(destinoDir.resolve(filePath.getFileName().toString() + ".log"), ("--\tInicio Mensaje:\t" + motivo + "\n--\tFin Mensaje\n--\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			} catch (IOException e) {
				logger.error(e,e);
			}
		
		
		
	}

}
