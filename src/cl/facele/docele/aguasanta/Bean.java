package cl.facele.docele.aguasanta;

public class Bean {
	String rutEmisor;
	int tipoDocumento;
	long folio;
	String contenidoTXT;
	String xml;
	String pdf;
	int estadoOperacion;
	String descripcionOperacion;
	
	public String getRutEmisor() {
		return rutEmisor;
	}
	public void setRutEmisor(String rutEmisor) {
		this.rutEmisor = rutEmisor;
	}
	public int getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public long getFolio() {
		return folio;
	}
	public void setFolio(long folio) {
		this.folio = folio;
	}
	public String getContenidoTXT() {
		return contenidoTXT;
	}
	public void setContenidoTXT(String contenidoTXT) {
		this.contenidoTXT = contenidoTXT;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public int getEstadoOperacion() {
		return estadoOperacion;
	}
	public void setEstadoOperacion(int estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}
	public String getDescripcionOperacion() {
		return descripcionOperacion;
	}
	public void setDescripcionOperacion(String descripcionOperacion) {
		this.descripcionOperacion = descripcionOperacion;
	}

}
