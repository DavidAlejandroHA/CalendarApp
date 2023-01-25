package dad.calendarapp.controller;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import dad.calendarapp.controller.model.Estilo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CalendarItem extends GridPane implements Initializable {

	// model

	private StringProperty lunesTitulo = new SimpleStringProperty();
	private StringProperty martesTitulo = new SimpleStringProperty();
	private StringProperty miercolesTitulo = new SimpleStringProperty();
	private StringProperty juevesTitulo = new SimpleStringProperty();
	private StringProperty viernesTitulo = new SimpleStringProperty();
	private StringProperty sabadoTitulo = new SimpleStringProperty();
	private StringProperty domingoTitulo = new SimpleStringProperty();
	private StringProperty monthTitulo = new SimpleStringProperty();

	private IntegerProperty monthproperty = new SimpleIntegerProperty();
	private IntegerProperty year = new SimpleIntegerProperty();
	private IntegerProperty desplazamiento = new SimpleIntegerProperty(0);

	private ObjectProperty<Month> month = new SimpleObjectProperty<>();
	private ObjectProperty<Locale> idioma = new SimpleObjectProperty<>();
	private ObjectProperty<TextStyle> estilo = new SimpleObjectProperty<>();
	private ObjectProperty<Estilo> apariencia = new SimpleObjectProperty<>();

	// view

	@FXML
	private Label lunesLabel, martesLabel, miercolesLabel, juevesLabel, viernesLabel, sabadoLabel, domingoLabel,
			monthLabel;

	/*
	 * @FXML private Label diaLabel1, diaLabel2, diaLabel3, diaLabel4, diaLabel5,
	 * diaLabel6, diaLabel7, diaLabel8, diaLabel9, diaLabel10, diaLabel11,
	 * diaLabel12, diaLabel13, diaLabel14, diaLabel15, diaLabel16, diaLabel17,
	 * diaLabel18, diaLabel19, diaLabel20, diaLabel21, diaLabel22, diaLabel23,
	 * diaLabel24, diaLabel25, diaLabel26, diaLabel27, diaLabel28, diaLabel29,
	 * diaLabel30, diaLabel31, diaLabel32, diaLabel33, diaLabel34, diaLabel35,
	 * diaLabel36, diaLabel37, diaLabel38, diaLabel39, diaLabel40, diaLabel41,
	 * diaLabel42;
	 */

	@FXML
	private GridPane view;

	// private Label[] listaDiasLabels;

	private List<Label> listaDiasLabels;

	private List<Label> diaSemanaLabelLista;

	public void initialize(URL location, ResourceBundle resources) {

		// set values

		// listaDiasLabels = new Label[] { diaLabel1, diaLabel2 ...42};

		listaDiasLabels = view.getChildren().stream().filter(node -> node instanceof Label).map(node -> (Label) node)
				.filter(label -> "diaLabel".equals(label.getId())).collect(Collectors.toList());

		// view.getStylesheets().add(getClass().getResource("lightstyle.css").toExternalForm());

		monthproperty.set(LocalDate.now().getMonthValue());
		year.set(LocalDate.now().getYear());
		month.set(LocalDate.now().getMonth());
		idioma.set(Locale.getDefault());
		estilo.set(TextStyle.NARROW);
		apariencia.set(Estilo.LIGHT);

		StringProperty[] stringPropsArray = { lunesTitulo, martesTitulo, miercolesTitulo, juevesTitulo, viernesTitulo,
				sabadoTitulo, domingoTitulo };

		diaSemanaLabelLista = view.getChildren().stream().filter(node -> node instanceof Label)
				.map(node -> (Label) node).filter(label -> "diaSemanaLabel".equals(label.getId()))
				.collect(Collectors.toList()); // 7 en total
		// diaSemanaLabelLista.get(0).getStyleClass().get(1)

//		Label[] semanaLabelListaArray = { lunesLabel, martesLabel, miercolesLabel, juevesLabel, viernesLabel,
//				sabadoLabel, domingoLabel };

		// bindings
		lunesLabel.textProperty().bind(lunesTitulo);
		martesLabel.textProperty().bind(martesTitulo);
		miercolesLabel.textProperty().bind(miercolesTitulo);
		juevesLabel.textProperty().bind(juevesTitulo);
		viernesLabel.textProperty().bind(viernesTitulo);
		sabadoLabel.textProperty().bind(sabadoTitulo);
		domingoLabel.textProperty().bind(domingoTitulo);
		monthLabel.textProperty().bind(monthTitulo);

		monthproperty.bind(Bindings.createIntegerBinding(() -> {
			return month.get().getValue();
		}, month, idioma));

		for (int i = 0; i < stringPropsArray.length; i++) {
			final int j = i;
			stringPropsArray[i].bind(Bindings.createStringBinding(() -> {
				int numDiaCorrespondiente = calcularPosicionCalendar(j - getDesplazamiento());
				if (numDiaCorrespondiente == 7) {
					numDiaCorrespondiente = 0;
				} // para gestionar correctamente el último elemento del array
				String diaSemana = DayOfWeek.values()[numDiaCorrespondiente].getDisplayName(estilo.get(), idioma.get());
				diaSemana = (diaSemana.charAt(0) + "").toUpperCase() + diaSemana.substring(1);

				ObservableList<String> listaEstilosDiaSemanaLabel = diaSemanaLabelLista.get(j).getStyleClass();

				if (listaEstilosDiaSemanaLabel.contains("sunday")) {
					listaEstilosDiaSemanaLabel.remove("sunday");
				}

				if (DayOfWeek.values()[numDiaCorrespondiente].getValue() == 7) {
					listaEstilosDiaSemanaLabel.add("sunday");
				}

				return diaSemana;
			}, estilo, idioma, desplazamiento));
		}

		monthTitulo.bind(Bindings.createStringBinding(() -> {
			String mes = this.month.get().getDisplayName(TextStyle.FULL, idioma.get());
			mes = (mes.charAt(0) + "").toUpperCase() + mes.substring(1);
			return mes;
		}, month, idioma));

		for (int i = 0; i < listaDiasLabels.size(); i++) {
			final int j = i + 1;
			listaDiasLabels.get(i).textProperty().bind(Bindings.createStringBinding(() -> {
				int diaPrimeroPosicion = LocalDate.of(year.get(), monthproperty.get(), 1).getDayOfWeek().getValue();
				int diaPrimeroDesplazado = (desplazamiento.get() == 0) ? diaPrimeroPosicion
						: calcularPosicionCalendar(diaPrimeroPosicion + desplazamiento.get());
				if (diaPrimeroDesplazado == 0) {
					diaPrimeroDesplazado = 7;
				}

				ObservableList<String> listaEstilosDiaLabel = listaDiasLabels.get((j - 1)).getStyleClass();

				if (listaEstilosDiaLabel.contains("sunday")) {
					listaEstilosDiaLabel.remove("sunday");
				}

				LocalDate ahora = LocalDate.now();
				if (ahora.getMonthValue() == monthproperty.get() && ahora.getYear() == year.get()
						&& ahora.getDayOfMonth() == (j - diaPrimeroDesplazado + 1)) {
					listaEstilosDiaLabel.add("today");
					// System.out.println(ahora+ " - " + monthproperty.get()+ " - " + year.get() + "
					// - " +(j - diaPrimeroDesplazado + 1) );
				} else {
					listaEstilosDiaLabel.remove("today");
				}

				int diasDelMes = LocalDate.of(year.get(), monthproperty.get(), 1).lengthOfMonth();
				if (j < diaPrimeroDesplazado || (j - diaPrimeroDesplazado + 1) > diasDelMes) {
					return "";
				} else {
					int diaSemana = LocalDate.of(year.get(), monthproperty.get(), (j - diaPrimeroDesplazado + 1))
							.getDayOfWeek().getValue();
					if (diaSemana == 7) {
						listaEstilosDiaLabel.add("sunday");
					}
					return j - diaPrimeroDesplazado + 1 + ""; //
				}
			}, month, year, desplazamiento));
		}
	}

	public int calcularPosicionCalendar(int n) {
		if (n >= 0) { // para n positivos
			if (n <= 7) {
				return n;
			} else {
				return calcularPosicionCalendar(n - 7);
			}
		} else { // para n negativos
			if (n >= 0) {
				return n;
			} else {
				return calcularPosicionCalendar(n + 7);
			}
		}
	}

	public CalendarItem() {
		super();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CalendarItem.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public GridPane getView() {
		return view;
	}

	public final IntegerProperty yearProperty() {
		return this.year;
	}

	public final int getYear() {
		return this.yearProperty().get();
	}

	public final void setYear(final int year) {
		this.yearProperty().set(year);
	}

	public final ObjectProperty<Locale> idiomaProperty() {
		return this.idioma;
	}

	public final Locale getIdioma() {
		return this.idiomaProperty().get();
	}

	public final void setIdioma(final Locale idioma) {
		this.idiomaProperty().set(idioma);
	}

	public final ObjectProperty<Month> monthProperty() {
		return this.month;
	}

	public final Month getMonth() {
		return this.monthProperty().get();
	}

	public final void setMonth(final Month month) {
		this.monthProperty().set(month);
	}

	public final ObjectProperty<TextStyle> textStyleProperty() {
		return this.estilo;
	}

	public final TextStyle getTextStyle() {
		return this.textStyleProperty().get();
	}

	public final void setTextStyle(final TextStyle estiloDiasSemana) {
		this.textStyleProperty().set(estiloDiasSemana);
	}

	public final IntegerProperty desplazamientoProperty() {
		return this.desplazamiento;
	}

	public final int getDesplazamiento() {
		return this.desplazamientoProperty().get();
	}

	public final void setDesplazamiento(final int desplazamiento) {
		this.desplazamientoProperty().set(desplazamiento);
	}

	public final ObjectProperty<Estilo> aparienciaProperty() {
		return this.apariencia;
	}

	public final Estilo getEstilocss() {
		return this.aparienciaProperty().get();
	}

	public final void setEstilocss(final Estilo estilocss) {
		this.aparienciaProperty().set(estilocss);
	}

}
