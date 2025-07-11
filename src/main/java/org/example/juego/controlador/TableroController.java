package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.juego.db.ManipuladorPregunta;
import org.example.juego.modelo.*;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.scene.input.KeyCode;


public class TableroController implements Initializable {

    @FXML private GridPane tablero;
    @FXML private VBox vboxJugadores;
    @FXML private AnchorPane Dado;
    @FXML private DadoController dadoController;

    @FXML
    private Button btnRendirse;
    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnSalir;

    private int turno = 0;
    private ListaJugador jugadoresDisponibles;
    private Jugador jugadorTurno;
    private ListaPreguntas preguntasDisponibles;
    private final List<Pane> highlighted = new ArrayList<>();
    private boolean preguntaCorrecta;
    private boolean preguntaCancelada;
    private Tablero modelo;
    private Stage stage;
    private Casilla casillaSeleccionada = null;
    private boolean dadoLanzado = false;


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/DadoView.fxml"));
            Parent dadoRoot = loader.load();
            Dado.getChildren().clear();
            Dado.getChildren().add(dadoRoot);
            renderDado();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Agregar listener para activar el dado con Enter
        agregarListenerEnter();
    }

    private void agregarListenerEnter() {
        // Esperar a que la escena esté lista
        Dado.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        onLanzarDado();
                    }
                });
            }
        });
    }

    @FXML
    public void onLanzarDado(){
        dadoLanzado = true;
        dadoController.clickDado(valor -> {
            onMovimientoJugador(valor);
            dadoLanzado = false;
        });

    }

    public void siguienteTurno() {
        turno++;
        if (turno > jugadoresDisponibles.getUsuarios().size() - 1) {
            turno = 0;
        }
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
    }

    public void setJugadores(ListaJugador jugadoresDisponibles) {
        this.jugadoresDisponibles = jugadoresDisponibles;
        List<Jugador> jugadores = jugadoresDisponibles.getUsuarios();
        if (!jugadores.isEmpty()) {
            jugadorTurno = jugadores.get(turno);
        }
        // Inicializar el tablero con las fichas de los jugadores
        if (modelo != null) {
            modelo.inicializarTablero(tablero, jugadores);
        }
        // Limpiar el VBox antes de a��adir jugadores
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
        // CAMBIO: usar la posición actual del jugador en turno
        List<Casilla> opciones = modelo.getOpcionesMovimiento(valorDado, jugadorTurno);
        for (Casilla posicionActual : opciones) {
            Pane pane = modelo.getPaneDeCasilla(posicionActual);
            // oscurecer (ej. bajar opacidad al 80%)
            pane.setOpacity(0.4);
            // opcional: un efecto de tinte
            // pane.setEffect(new ColorAdjust(0, 0, -0.5, 0));

            // click para mover
            pane.setOnMouseClicked(evt -> {
                casillaSeleccionada = posicionActual;
                try {
                    String colorCasilla = posicionActual.getColor();
                    String categoria = obtenerCategoria(colorCasilla);
                    // Detectar si es el centro
                    if ("CENTRO".equalsIgnoreCase(posicionActual.getTipo())) {
                        modelo.moverFicha(tablero, casillaSeleccionada, jugadorTurno);
                        if (jugadorTurno.getQuesitosRellenos().size() >= 6) {
                            mostrarVentanaVictoria(jugadorTurno.getAlias());
                            deshabilitarTablero();
                        } else {
                            siguienteTurno();
                            setJugadores(jugadoresDisponibles);
                        }
                        casillaSeleccionada = null;
                    } else if (colorCasilla != null && colorCasilla.equalsIgnoreCase("gray")) {
                        // Solo mover la ficha, no abrir modal
                        modelo.moverFicha(tablero, casillaSeleccionada, jugadorTurno);
                        siguienteTurno();
                        setJugadores(jugadoresDisponibles);
                        casillaSeleccionada = null;
                    } else if(!categoria.equalsIgnoreCase("LIBRE")){
                        renderVentanaPreguntas(colorCasilla);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/PreguntasCategoriasView.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        String categoria = obtenerCategoria(colorCasilla);
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
        ctrl.setOnAnswerComplete(correcto -> {
            if (casillaSeleccionada != null) {
                modelo.moverFicha(tablero, casillaSeleccionada, jugadorTurno);
                // Si la respuesta es correcta, asignar la categoría y rellenar el quesito
                if (correcto) {
                    jugadorTurno.asignarCategoria(categoria);
                    jugadorTurno.rellenarQuesitoCategoria(categoria);
                    // Rellenar quesito visual
                    javafx.scene.Node fichaNode = modelo.getFichaVisual(jugadorTurno);
                    if (fichaNode != null && fichaNode instanceof javafx.scene.layout.AnchorPane anchorPane) {
                        Object controller = anchorPane.getProperties().get("fx:controller");
                        if (controller instanceof org.example.juego.controlador.FichaJugadorController fichaController) {
                            fichaController.rellenarQuesito(categoria);
                        }
                    }
                }
            }
            siguienteTurno();
            setJugadores(jugadoresDisponibles);
            casillaSeleccionada = null;
        });
        nuevoStage.show();
    }

    /**
     * Método para obtener la lista de preguntas disponibles por categoria filtrada.
     * @param preguntasCategorias Lista de preguntas por categoría.
     * @return Una pregunta aleatoria de la lista de preguntas filtrada por categoría.
     * */
    public Pregunta preguntaRandom(ListaPreguntas preguntasCategorias){
        return preguntasCategorias.getPreguntas().get(new Random().nextInt(preguntasCategorias.sizeListaPreguntas()));
    }

    /**
     * Método para obtener la categoría correspondiente a un color de categoría segun la casilla actual.
     * @param colorCategoria El color de la categoría (ej. "yellow", "green", etc.)
     * @return La categoría correspondiente como String.
     * */
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

    private void mostrarVentanaVictoria(String aliasGanador) {
        Stage stageVictoria = new Stage();
        VBox root = new VBox();
        root.setSpacing(20);
        root.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #eaffea;");
        Label label = new Label("¡" + aliasGanador + " ha ganado la partida!\n¡Felicidades!");
        label.setStyle("-fx-font-size: 22px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
        Button btnSalir = new Button("Salir");
        Button btnReiniciar = new Button("Reiniciar partida");
        btnSalir.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px;");
        btnReiniciar.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 16px;");
        btnSalir.setOnAction(e -> System.exit(0));
        btnReiniciar.setOnAction(e -> {
            stageVictoria.close();
            reiniciarPartida();
        });
        root.getChildren().addAll(label, btnReiniciar, btnSalir);
        stageVictoria.setScene(new Scene(root));
        stageVictoria.setTitle("¡Victoria!");
        stageVictoria.initModality(Modality.APPLICATION_MODAL);
        stageVictoria.setOnCloseRequest(e -> e.consume()); // Evita cerrar con la X
        stageVictoria.show();
    }

    private void reiniciarPartida() {
        // Reiniciar posiciones y quesitos de los jugadores
        for (Jugador jugador : jugadoresDisponibles.getUsuarios()) {
            jugador.setFilaActual(10); // Centro
            jugador.setColumnaActual(10);
            jugador.getQuesitosRellenos().clear();
            if (jugador.getCategoriasRespondidas() != null) {
                jugador.getCategoriasRespondidas().clear();
            }
        }
        turno = 0;
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
        tablero.setDisable(false);
        setJugadores(jugadoresDisponibles);
    }

    private void deshabilitarTablero() {
        for (Pane p : highlighted) {
            p.setOnMouseClicked(null);
        }
        tablero.setDisable(true);
    }

    @FXML
    private void onRendirse() {
        if (dadoLanzado) return; // No permitir rendirse después de lanzar el dado
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("¿Seguro que deseas rendirte?");
        Label label = new Label("¿Estás seguro que deseas rendirte? Perderás la partida.");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #c0392b; -fx-font-weight: bold; -fx-padding: 10 0 20 0;");
        Button btnSi = new Button("Sí, rendirse");
        Button btnNo = new Button("No");
        btnSi.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-background-radius: 8;");
        btnNo.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-background-radius: 8;");
        HBox hBox = new HBox(20, btnSi, btnNo);
        hBox.setStyle("-fx-alignment: center;");
        VBox vbox = new VBox(20, label, hBox);
        vbox.setStyle("-fx-padding: 30; -fx-background-color: #f9ebea; -fx-alignment: center; -fx-border-color: #e74c3c; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        btnSi.setOnAction(e -> {
            dialog.close();
            ejecutarRendicion();
        });
        btnNo.setOnAction(e -> {
            dialog.close();
            // No hacer nada más, solo cerrar la ventana
        });
        dialog.showAndWait();
    }

    private void ejecutarRendicion() {
        // Marcar como rendido
        jugadorTurno.setRendido(true);
        // Eliminar ficha visual del tablero
        if (modelo != null && tablero != null) {
            javafx.scene.Node ficha = modelo.getFichaVisual(jugadorTurno);
            if (ficha != null) {
                tablero.getChildren().remove(ficha);
            }
        }
        // Quitar de la lista de jugadores activos
        jugadoresDisponibles.eliminarUsuario(jugadorTurno);
        int jugadoresActivos = (int) jugadoresDisponibles.getUsuarios().stream().filter(j -> !j.isRendido()).count();
        if (jugadoresActivos == 0) {
            List<Jugador> ganadores = obtenerGanadoresPorCategorias();
            if (ganadores.size() == 1) {
                mostrarMensaje("Fin de la partida", "El ganador es: " + ganadores.get(0).getAlias());
            } else {
                Jugador ganadorPorTiempo = obtenerGanadorPorTiempo(ganadores);
                mostrarMensaje("Fin de la partida", "Empate en categorías. El ganador por menor tiempo es: " + ganadorPorTiempo.getAlias());
            }
            regresarAlMenu();
        } else if (jugadoresActivos == 1 && jugadoresDisponibles.getUsuarios().size() == 1) {
            Jugador otro = jugadoresDisponibles.getUsuarios().stream().filter(j -> !j.isRendido()).findFirst().get();
            mostrarMensaje("Fin de la partida", "El ganador es: " + otro.getAlias());
            regresarAlMenu();
        } else if (jugadoresActivos == 1 && jugadoresDisponibles.getUsuarios().size() == 2) {
            Jugador otro = jugadoresDisponibles.getUsuarios().stream().filter(j -> !j.isRendido()).findFirst().get();
            if (otro.getCategoriasAcertadas() > jugadorTurno.getCategoriasAcertadas()) {
                mostrarMensaje("Fin de la partida", "El ganador es: " + otro.getAlias());
                regresarAlMenu();
            } else {
                mostrarMensaje("Continúa", "El otro jugador puede seguir jugando en solitario.");
                siguienteTurno();
                setJugadores(jugadoresDisponibles);
            }
        } else {
            mostrarMensaje("Rendición", jugadorTurno.getAlias() + " se ha rendido. Continúan los demás jugadores.");
            // Ajustar turno para que no apunte a un jugador eliminado
            if (turno >= jugadoresDisponibles.getUsuarios().size()) {
                turno = 0;
            }
            jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
            setJugadores(jugadoresDisponibles);
        }
    }

    @FXML
    private void onRegresar() {
        regresarAlMenu();
    }

    @FXML
    private void onSalir() {
        // Cierra completamente la aplicación
        javafx.application.Platform.exit();
        System.exit(0);
    }

    private void regresarAlMenu() {
        // Solo cerrar la ventana del tablero para volver a la selección de jugadores
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titulo);
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setStyle("-fx-padding: 35; -fx-background-color: #f8f9fa; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-width: 3; -fx-border-color: #1a5276; -fx-alignment: center;");
        Label label = new Label(mensaje);
        label.setStyle("-fx-font-size: 17px; -fx-text-fill: #1a5276; -fx-font-family: 'Consolas Bold'; -fx-font-weight: bold; -fx-padding: 10 0 10 0;");
        Button btnContinuar = new Button("Continuar");
        btnContinuar.setStyle("-fx-background-color: #1a5276; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30 8 30; -fx-cursor: hand;");
        btnContinuar.setOnAction(e -> dialog.close());
        vbox.getChildren().addAll(label, btnContinuar);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    private List<Jugador> obtenerGanadoresPorCategorias() {
        int max = jugadoresDisponibles.getUsuarios().stream().mapToInt(Jugador::getCategoriasAcertadas).max().orElse(0);
        List<Jugador> ganadores = new ArrayList<>();
        for (Jugador j : jugadoresDisponibles.getUsuarios()) {
            if (j.getCategoriasAcertadas() == max) {
                ganadores.add(j);
            }
        }
        return ganadores;
    }

    private Jugador obtenerGanadorPorTiempo(List<Jugador> empatados) {
        return empatados.stream().min(Comparator.comparingLong(Jugador::getTiempoTotal)).get();
    }

}
