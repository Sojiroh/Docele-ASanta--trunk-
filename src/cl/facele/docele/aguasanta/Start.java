package cl.facele.docele.aguasanta;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class Start {
	static Logger logger = Logger.getLogger(Start.class);
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
            BasicConfigurator.configure();
		logger.debug("Start...");
		Bussines busines = new Bussines();
		busines.start();
		System.exit(0);
	}
}
