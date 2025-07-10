package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.juego.JuegoApplication;
import org.example.juego.db.ManipuladorPregunta;
import org.example.juego.modelo.*;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class TableroController implements Initializable {

    @FXML private GridPane tablero;
    @FXML private VBox vboxJugadores;
    @FXML private AnchorPane Dado;
    @FXML private DadoController dadoController;

    private int turno = 0;
    private ListaJugador jugadoresDisponibles;
    private Jugador jugadorTurno;
    private ListaPreguntas preguntasDisponibles;
    private final List<Pane> highlighted = new ArrayList<>();
    private boolean preguntaCorrecta;
    private boolean preguntaCancelada;
    private Tablero modelo;
    private Stage stage;



    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Tablero Trivia UCAB");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (stage != null) {
            stage.setTitle("");
        }

        try {
            ManipuladorPregunta pregunta = new ManipuladorPregunta();
            preguntasDisponibles = pregunta.getListaPreguntas();
            modelo = new Tablero();
            modelo.inicializarTablero(tablero);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/DadoView.fxml"));
            Parent dadoRoot = loader.load();
            // Elimina cualquier dado anterior
            Dado.getChildren().clear();
            Dado.getChildren().add(dadoRoot);

            // Renderizo el dado
            renderDado();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLanzarDado(){
        dadoController.clickDado(this::onMovimientoJugador);

    }

    public void siguienteTurno() {
        turno++;
        if (turno > jugadoresDisponibles.getUsuarios().size() - 1) {
            turno = 0;
        }
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
    }

    @FXML
    private void onSiguienteTurno() {
        siguienteTurno();
        setJugadores(jugadoresDisponibles);
    }

    public void setJugadores(ListaJugador jugadoresDisponibles) {
        this.jugadoresDisponibles = jugadoresDisponibles;
        List<Jugador> jugadores = jugadoresDisponibles.getUsuarios();
        if (!jugadores.isEmpty()) {
            jugadorTurno = jugadores.get(turno);
        }
        // Limpiar el VBox antes de añadir jugadores
        vboxJugadores.getChildren().clear();

        // Crear un Accordion para los jugadores
        Accordion accordion = new Accordion();
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            String texto = (i + 1) + ". " + jugador.getAlias() + " 🎲 " + jugador.getResultadoDado();
            String infoTexto = "Información del jugador: Categoría respondida";
            Label infoLabel = new Label(infoTexto);
            VBox infoBox = new VBox(infoLabel);
            infoBox.setStyle("-fx-background-color: #f1f1f1; -fx-padding: 8px 16px; -fx-background-radius: 0 0 10px 10px;");
            TitledPane pane = new TitledPane(texto, infoBox);
            // Si es el turno actual, poner en verde el TitledPane completo
            if (jugador == jugadorTurno) {
                pane.setStyle("-fx-background-color: #28a745; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-text-fill: #145a23;");
                // El gráfico será el nombre en verde oscuro y la flecha
                Label turnoLabel = new Label(jugador.getAlias() + " ⬅ TURNO");
                turnoLabel.setStyle("-fx-text-fill: #145a23; -fx-font-weight: bold; -fx-font-size: 16px;");
                pane.setText((i + 1) + ". 🎲 " + jugador.getResultadoDado());
                pane.setGraphic(turnoLabel);
                pane.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            } else {
                pane.setStyle("");
                pane.setText(texto);
                pane.setGraphic(null);
            }
            accordion.getPanes().add(pane);
        }
        vboxJugadores.getChildren().add(accordion);
    }

    private void renderDado(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/juego/DadoView.fxml")
            );
            Parent root = loader.load();
            dadoController = loader.getController();

            Dado.getChildren().setAll(root);
            dadoController.start(1);

            // Habilitar la imagen del dado al iniciar el turno
            ImageView imgDado = (ImageView) root.lookup("#imgDado");
            if (imgDado != null) {
                imgDado.setDisable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMovimientoJugador(int valorDado) {
        // limpiar anteriores…
        for (Pane p : highlighted) {
            // restaura opacidad original
            double original = (double) p.getUserData();
            p.setOpacity(original);
            p.setEffect(null);
            p.setOnMouseClicked(null);
        }
        highlighted.clear();

        // nuevo resaltado:
        List<Casilla> opciones = modelo.getOpcionesMovimiento(valorDado);
        for (Casilla posicionActual : opciones) {
            Pane pane = modelo.getPaneDeCasilla(posicionActual);
            // oscurecer (ej. bajar opacidad al 80%)
            pane.setOpacity(0.4);
            // opcional: un efecto de tinte
            // pane.setEffect(new ColorAdjust(0, 0, -0.5, 0));

            // click para mover
            pane.setOnMouseClicked(evt -> {
                modelo.moverFicha(tablero, posicionActual, jugadorTurno);
                try {
                    String categoria = obtenerCategoria(posicionActual.getColor());
                    if(!categoria.equalsIgnoreCase("LIBRE")){
                        renderVentanaPreguntas(posicionActual.getColor());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                onMovimientoJugador(0);
            });
            highlighted.add(pane);
        }
    }

    private void renderVentanaPreguntas(String colorCasilla) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/PreguntasCategoriasView.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        String categoria = obtenerCategoria(colorCasilla);

        // Obtener el controlador asociado
        PreguntasCategoriasController ctrl = loader.getController();

        ctrl.getPaneColor().setStyle("-fx-background-color: " +  colorCasilla + ";");
        ctrl.getLblCategoria().setText(categoria);
        Pregunta preguntaAleatoriaCategoria = preguntaRandom(preguntasDisponibles.buscarPreguntaCategoria(categoria));
        ctrl.getLblPregunta().setText(preguntaAleatoriaCategoria.getPregunta());
        ctrl.setRespuesta(preguntaAleatoriaCategoria.getRespuesta());

        Stage nuevoStage = new Stage();
        nuevoStage.setScene(new Scene(root));

        nuevoStage.initModality(Modality.APPLICATION_MODAL);

        nuevoStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icono2.png"))));

        ctrl.setStage(nuevoStage);
        nuevoStage.show();
    }

    public Pregunta preguntaRandom(ListaPreguntas preguntasCategorias){
        return preguntasCategorias.getPreguntas().get(new Random().nextInt(preguntasCategorias.sizeListaPreguntas()));
    }

    public String obtenerCategoria(String colorCategoria){
        String categoriaCorrespondiente = "";
        switch (colorCategoria){
            case "yellow":
                categoriaCorrespondiente = "Historia";
                break;
            case "green":
                categoriaCorrespondiente = "Geografía";
                break;
            case "red":
                categoriaCorrespondiente = "Arte y Literatura";
                break;
            case "purple":
                categoriaCorrespondiente = "Entretenimiento";
                break;
            case "orange":
                categoriaCorrespondiente = "Deportes";
                break;
            case "blue":
                categoriaCorrespondiente = "Ciencia";
                break;
            default:
                categoriaCorrespondiente = "LIBRE";
            break;
        }
        return categoriaCorrespondiente;
    }

}
