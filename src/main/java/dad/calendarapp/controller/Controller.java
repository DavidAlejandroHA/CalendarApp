package dad.calendarapp.controller;

import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;

import dad.calendarapp.CalendarApp;
import dad.calendarapp.controller.model.Estilo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class Controller implements Initializable {

	// model
	private IntegerProperty year = new SimpleIntegerProperty(LocalDate.now().getYear());
	private IntegerProperty desplazamiento = new SimpleIntegerProperty(0);

	private ObjectProperty<Locale> idioma = new SimpleObjectProperty<>(new Locale(Locale.getDefault().getLanguage()));
	// en vez de poner Locale.getDefault() es necesario ponerlo así, si no me
	// seleccionará un idioma con una variante aplicada (al ser un objeto diferente
	// no aparecerá seleccionado en la lista)

	private ObjectProperty<TextStyle> estilo = new SimpleObjectProperty<>(TextStyle.SHORT);
	private ObjectProperty<Estilo> apariencia = new SimpleObjectProperty<>(Estilo.LIGHT);

	// view

	@FXML
	private CalendarItem eneroCalendar;

	@FXML
	private CalendarItem febreroCalendar;

	@FXML
	private CalendarItem marzoCalendar;

	@FXML
	private CalendarItem abrilCalendar;

	@FXML
	private CalendarItem mayoCalendar;

	@FXML
	private CalendarItem junioCalendar;

	@FXML
	private CalendarItem julioCalendar;

	@FXML
	private CalendarItem agostoCalendar;

	@FXML
	private CalendarItem septiembreCalendar;

	@FXML
	private CalendarItem octubreCalendar;

	@FXML
	private CalendarItem noviembreCalendar;

	@FXML
	private CalendarItem diciembreCalendar;

	@FXML
	private ComboBox<Locale> idiomaComboBox;

	@FXML
	private ComboBox<TextStyle> estiloComboBox;

	@FXML
	private ComboBox<Estilo> aparienciaComboBox;

	@FXML
	private Label yearLabel;

	@FXML
	private Button yearMasBoton;

	@FXML
	private Button yearMenosBoton;

	@FXML
	private Spinner<Integer> desplazamientoSpinner;

	@FXML
	private GridPane view;

	public GridPane getView() {
		return view;
	}

	private CalendarItem[] calendarios;

	public void initialize(URL location, ResourceBundle resources) {

		// spinner value factory
		desplazamientoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-9999, 9999, 0));

		// set spinner event handler
		EventHandler<KeyEvent> enterKeyEventHandler;
		enterKeyEventHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// handle users "enter key event"
				if (event.getCode() == KeyCode.ENTER) {
					boolean limiteExecido = false;
					try {
						// String numString = desplazamientoSpinner.getEditor().textProperty().get();
						String stringNum = desplazamientoSpinner.getEditor().textProperty().get();
						int num = stringNum == null ? 0 : Integer.parseInt(stringNum);
						if (num < -9999 || num > 9999) {
							limiteExecido = true;
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {

						// show message to user: "only numbers allowed"

						// reset editor to INITAL_VALUE
						desplazamientoSpinner.getEditor().textProperty().set(0 + "");

						Alert alerta = new Alert(AlertType.ERROR);
						alerta.setTitle("Error");
						alerta.setHeaderText((limiteExecido) ? "Solo se pueden insertar números entre -9999 y 9999."
								: "Solo se pueden insertar números enteros en el área.");
						alerta.initOwner(CalendarApp.primaryStage);
						alerta.showAndWait();
					}
				}
			}
		};

		// note: use KeyEvent.KEY_PRESSED, because KeyEvent.KEY_TYPED is to late,
		// spinners SpinnerValueFactory reached new value before key released an
		// SpinnerValueFactory will throw an exception
		desplazamientoSpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);
		// https://stackoverflow.com/questions/25885005/insert-only-numbers-in-spinner-control

		// set values
		calendarios = new CalendarItem[] { eneroCalendar, febreroCalendar, marzoCalendar, abrilCalendar, mayoCalendar,
				junioCalendar, julioCalendar, agostoCalendar, septiembreCalendar, octubreCalendar, noviembreCalendar,
				diciembreCalendar };

		ArrayList<Locale> valores = cargarAllLocales();
		Collections.sort(valores, new Comparator<Locale>() {

			@Override
			public int compare(Locale o1, Locale o2) {
				Collator collator = Collator.getInstance(); // permite comparar texto incluso con tildes
				String nombreLocale1 = o1.getDisplayLanguage();
				String nombreLocale2 = o2.getDisplayLanguage();
				// return nombreLocale1.compareTo(nombreLocale2);
				return collator.compare(nombreLocale1, nombreLocale2);
			}
		});

		idiomaComboBox.getItems().setAll(valores);
		idiomaComboBox.getSelectionModel().select(idioma.get());

		estiloComboBox.getItems().setAll(TextStyle.values());
		estiloComboBox.getSelectionModel().select(estilo.get());

		aparienciaComboBox.getItems().setAll(Estilo.values());
		aparienciaComboBox.getSelectionModel().select(apariencia.get());

		// ---- añadir StringConverter al combobox de idiomas para modificar el texto de
		// ---- cada objeto del combo seleccionado (pasar las iniciales del idioma a un
		// ---- locale y luego obtener el nombre completo del idioma)
		idiomaComboBox.setConverter(new StringConverter<Locale>() {
			@Override
			public String toString(Locale object) {
				String lenguaje = object.getDisplayLanguage();
				return (lenguaje.charAt(0) + "").toUpperCase() + lenguaje.substring(1);
			}

			@Override
			public Locale fromString(String string) {
				return new Locale(string);
			}
		});

		// bindings
		// ---- añadir listener para bindear el valor Integer del spinner a
		// ---- desplazamiento property cada vez que el valor cambie
		desplazamientoSpinner.getValueFactory().valueProperty().addListener(this::valueComboChanged);

		// ---- bindear el year property de los calendarios al year property del
		// controller
		for (int i = 0; i < calendarios.length; i++) {
			calendarios[i].yearProperty().bind(year);
		}
		// ---- bindear el desplazamiento property de los calendarios al desplazamiento
		// ---- property del controller
		for (int i = 0; i < calendarios.length; i++) {
			calendarios[i].desplazamientoProperty().bind(desplazamiento);
		}
		// ---- bindear el year property al yearLabel
		yearLabel.textProperty().bindBidirectional(year, new NumberStringConverter("#"));

		// ---- sobreescribir el idioma del mes y dias de la semana al actualizarse
		for (int i = 0; i < calendarios.length; i++) {
			calendarios[i].idiomaProperty().bind(Bindings.createObjectBinding(() -> {
				return idioma.getValue();
			}, idioma));
		}

		// ---- actualizar el estilo del mes y de los dias de la semana
		for (int i = 0; i < calendarios.length; i++) {
			calendarios[i].textStyleProperty().bind(estilo);

			// ---- y bindear el estilo css de cada calendario al del controller
			calendarios[i].aparienciaProperty().bind(Bindings.createObjectBinding(() -> {
				return apariencia.getValue();
			}, apariencia)); // en realidad no cambiará nada porque el cambio de
							// estilo se gestiona desde el controller
		}

		// ---- bindear el idioma seleccionado a la idioma property
		idioma.bind(Bindings.createObjectBinding(() -> {
			return idiomaComboBox.getSelectionModel().selectedItemProperty().get();
		}, idiomaComboBox.valueProperty()));

		// ---- bindear el estilo css seleccionado a cssproperty

		apariencia.set(Estilo.LIGHT);
		apariencia.bind(Bindings.createObjectBinding(() -> {
			// view.getStylesheets().setAll(getClass().getResource("/css/" +
			// apariencia.get().getEstilo()).toExternalForm());
			return aparienciaComboBox.getSelectionModel().selectedItemProperty().get();
		}, aparienciaComboBox.valueProperty())); ////////

		// ---- bindear el estilo seleccionado al estilo property
		estilo.bind(Bindings.createObjectBinding(() -> {
			return estiloComboBox.getSelectionModel().selectedItemProperty().get();
		}, estiloComboBox.valueProperty()));

		// No se puede añadir esto dentro del binding, tiene que ser mediante un
		// listener
		apariencia.addListener((o, ov, nv) -> {
			view.getStylesheets()
					.setAll(getClass().getResource("/css/" + apariencia.get().getEstilo()).toExternalForm());
		});
	}

	public Controller() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// el binding a el .getValueFactory().valueProperty() del combobox no iba, tuve
	// que añadir un listener que gestionase los bindings
	private void valueComboChanged(ObservableValue<? extends Integer> o, Integer ov, Integer nv) {
		if (ov != null) {
			desplazamiento.asObject().unbindBidirectional(desplazamientoSpinner.getValueFactory().valueProperty());
		}

		if (nv != null) {
			desplazamiento.asObject().bindBidirectional(desplazamientoSpinner.getValueFactory().valueProperty());
		}
		// System.out.println(desplazamientoSpinner.getValueFactory().valueProperty().get());
		// System.out.println(desplazamiento.get());
	}

	public ArrayList<Locale> cargarAllLocales() {
		String[] valores = Locale.getISOLanguages();
		ArrayList<Locale> valoresLocale = new ArrayList<>();
		for (int j = 0; j < valores.length; j++) {
			valoresLocale.add(new Locale(valores[j]));
		}

		/*
		 * Locale[] resultado = new Locale[valoresLocale.size()]; resultado =
		 * valoresLocale.toArray(resultado); // si no encuentra nada usa el puesto por
		 * defecto
		 */

		// return resultado;
		return valoresLocale;
	}

	@FXML
	void onMasYearAction(ActionEvent event) {
		operarAnio(1);
	}

	@FXML
	void onMenosYearAction(ActionEvent event) {
		operarAnio(-1);
	}

	public void operarAnio(int n) {
		year.set(year.get() + n);
	}

}
