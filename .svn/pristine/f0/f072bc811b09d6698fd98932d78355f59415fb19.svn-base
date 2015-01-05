package cl.facele.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MoveFile {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static synchronized void main(String[] args) throws IOException {

		Path path = Paths.get("C:\\Users\\Usuario\\FACELE\\CLIENTES\\Dartel\\txt\\ND\\hola_fin");
		
		Files.write(path, "hola mundo".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
		
		Path path2 = Paths.get("C:\\Users\\Usuario\\FACELE\\CLIENTES\\Dartel\\txt\\ND\\error");
		
		if (Files.notExists(path))
			System.exit(-1);

		
		int n = 0;
		String nombreFile = path.getFileName().toString();
		while (true) {
			if (n == 0 && Files.notExists(path2.resolve(path.getFileName()))) {
				break;
			}
			nombreFile = (nombreFile + ".tmp").split("|.|")[0] + "_" + Integer.toString(n) + ".txt";
			System.out.println("nombre Archivo: " + nombreFile);
			if (Files.notExists(path2.resolve(nombreFile))) {
				break;
			}

			n++;
		}
		
		Files.move(path, path2.resolve(nombreFile));
//		path = Files.move(path, path.resolveSibling("hola_"));
//		Files.move(path, path2.resolve("hola_fin_2.txt"));

	}

}
