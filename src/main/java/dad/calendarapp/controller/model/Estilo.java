package dad.calendarapp.controller.model;

public enum Estilo {
	LIGHT("lightstyle.css"),
	DARK("darkstyle.css");
	
	private String estilo;
	
	public String getEstilo() {
		return estilo;
	}

	Estilo(String estilo){
		this.estilo = estilo;
	}
}
