package cl.facele.docele.aguasanta;

import org.apache.log4j.Logger;

public class Service {

	/**
	 * @param args
	 */
	private static Logger logger = Logger.getLogger(Object.class);
	private static Service service;
	private boolean stopped = false;

	public static void main(String[] args) throws Exception {
		String cmd = "";
		if (args.length > 0) {
			cmd = args[0];
		} else {
			logger.debug("No se ha informado parametros");
			System.exit(-1);
		}
 
		if (service == null)
			service = new Service();
		
		if ("start".equals(cmd))
			service.start();
		if ("stop".equals(cmd))
			service.stop();
		
		System.exit(0);
	}

	public void stop() {
		logger.info("...Deteniendo servicio.");
		stopped = true;

		synchronized (this) {
			this.notify();
		}
	}

	public void start() {
		logger.debug("Arranca servicio...");
		long startTimeProcess = 0l;
		// http://commons.apache.org/daemon/procrun.html
		try {
			Thread thread = null;
			while (!stopped) {
				logger.debug("Inicia ciclo...");
				try {
					if (thread == null || !thread.isAlive()) {
						startTimeProcess = System.currentTimeMillis();
						thread = new Bussines();
						thread.start();
					} else if ((System.currentTimeMillis() - startTimeProcess) > 60 * 1000) {
							logger.info("Interrumpiendo Thread por timeOut: " + (System.currentTimeMillis() - startTimeProcess) + "milisegundos");
							thread.interrupt();
						
					} else
							logger.info("Proceso esta ACTIVO, no se activa un segundo proceso. "
								+ thread.getState().toString());

					System.gc();

				} catch (Exception e) {
					logger.error("Existe un ERROR desconocido en aplicativos"
							+ e.getMessage(), e);
				}

				synchronized (this) {
					try {
						this.wait(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
