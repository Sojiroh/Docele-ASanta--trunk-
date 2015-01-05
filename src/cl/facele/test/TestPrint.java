package cl.facele.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cl.facele.docele.aguasanta.Print;

public class TestPrint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Path path = Paths.get("C:\\Users\\Usuario\\FACELE\\tmp\\PDF_1361524986468.pdf");
//		String impresora = "HP LaserJet M3035 mfp PCL6";
		String impresora = "PDFCreator";
		
		Print p = new Print();
		try {
			p.printToDevice(Files.readAllBytes(path), impresora, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
