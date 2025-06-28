package org.example.juego.controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.juego.helpers.HelperLogin;
import org.example.juego.modelo.Jugador;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TurnoController {
    @FXML
    private AnchorPane ctdDado;
    @FXML
    private Label lblJugadorAlias;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private Button btnLanzarDado;
    @FXML
    private Label lblInfo;
    private List<Jugador> jugadoresTablero;
    private static int indiceJugador = 0;
    private DadoController dadoController;
    private List<Jugador> rondaActual;
    private final List<Jugador> ordenFinal = new ArrayList<>();
    private List<Jugador> todosLosJugadores = new ArrayList<>();


    public void iniciarRonda(List<Jugador> jugadores) {
        this.jugadoresTablero = jugadores;
        this.rondaActual = new ArrayList<>(jugadores);
        indiceJugador = 0;
        btnLanzarDado.setDisable(false);
        renderJugador();  // prepara UI para el primer de esta ronda
    }

    @FXML
    public void lanzarDado() {

        // Si aún no hemos inicializado la ronda, la iniciamos con todos los jugadores
        if (rondaActual == null) {
            iniciarRonda(jugadoresTablero);
        }

        // Ahora ya podemos usar rondaActual sin NPE
        if (indiceJugador >= rondaActual.size()) {
            btnLanzarDado.setDisable(true);
            manejarFinDeRonda();
            return;
        }

        // igual que antes, lanzas el dado y en el callback:
        dadoController.clickDado(valor -> Platform.runLater(() -> {
            Jugador j = rondaActual.get(indiceJugador);
            j.setResultadoDado(valor);
            System.out.println(j.getAlias() + " sacó: " + valor);
            HelperLogin.mostrarStado(lblInfo, j.getAlias() + " saco: " + j.getResultadoDado(), true, true, "alert-success");
            indiceJugador++;
            if (indiceJugador < rondaActual.size()) {
                renderJugador();
            } else {
                btnLanzarDado.setDisable(true);
                manejarFinDeRonda();
            }
        }));
    }

    private void manejarFinDeRonda() {
        // Agrupar jugadores por puntuación
        Map<Integer, List<Jugador>> agrupados = rondaActual.stream()
                .collect(Collectors.groupingBy(Jugador::getResultadoDado));

        // Buscar el valor mayor con más de un jugador (empate)
        Optional<Integer> tieValueOpt = agrupados.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .max(Comparator.naturalOrder());

        if (tieValueOpt.isPresent()) {
            int tieValue = tieValueOpt.get();

            rondaActual.stream()
                    .filter(j -> j.getResultadoDado() > tieValue)
                    .sorted(Comparator.comparingInt(Jugador::getResultadoDado).reversed()) // Orden correcto
                    .forEach(j -> {
                        if (!ordenFinal.contains(j)) {
                            ordenFinal.add(j);
                        }
                    });

            // Lista de empatados que deben volver a tirar
            List<Jugador> empatados = agrupados.get(tieValue);
            HelperLogin.mostrarStado(lblInfo, "¡Hay Empate toca desempatar!", true, true, "alert-warning");

            iniciarRonda(new ArrayList<>(empatados));
        } else {
            // 1. Ordenamos los que quedan en esta ronda final (no hubo empate)
            List<Jugador> restantesOrdenados = rondaActual.stream()
                    .sorted(Comparator.comparingInt(Jugador::getResultadoDado).reversed())
                    .toList();

            // 2. Agregamos estos jugadores al ordenFinal (respetando el orden de resolución)
            restantesOrdenados.forEach(j -> {
                if (!ordenFinal.contains(j)) {
                    ordenFinal.add(j);
                }
            });

            List<Jugador> rankingFinal = new ArrayList<>(ordenFinal);

            for (Jugador j : todosLosJugadores) {
                if (!rankingFinal.contains(j)) {
                    rankingFinal.add(j);
                }
            }

            tableroConJugadoresOrdenados(rankingFinal, ctdDado);
        }
    }

    public void tableroConJugadoresOrdenados(List<Jugador> jugadoresOrdenados, Node nodoCualquieraDelModalActual) {
        try {
            // Cerramos el modal actual usando el nodo que pertenece a esa ventana
            Stage ventanaActual = (Stage) nodoCualquieraDelModalActual.getScene().getWindow();
            ventanaActual.close();

            // Cargamos la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/tableroView.fxml"));
            Parent root = loader.load();

            // Pasamos la lista al nuevo controlador
            TableroController ctrl = loader.getController();
            ctrl.setJugadoresOrdenados(jugadoresOrdenados);

            // Abrimos el nuevo tablero en un modal nuevo
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(new Scene(root));
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderJugador() {
        if (rondaActual == null || rondaActual.isEmpty()) {
            return;
        }

        Jugador jugadorEnTurno = rondaActual.get(indiceJugador);

        String[] imgs = {"j1.jpg", "j2.jpg", "j3.jpg", "j4.jpg", "j5.jpg", "j6.jpg"};
        int avatarIndex = indiceJugador % imgs.length;
        Image avatar = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/" + imgs[avatarIndex])
                )
        );

        // Clip circular
        Circle clip = new Circle(48, 48, 38);
        imgAvatar.setClip(clip);
        imgAvatar.setImage(avatar);

        // 3) Actualizamos el alias
        lblJugadorAlias.setText(jugadorEnTurno.getAlias());

        // 4) Preparamos el dado para este jugador
        if(indiceJugador == 0) {
            renderDado();
        }
    }

    private void renderDado(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/juego/DadoView.fxml")
            );
            Parent root = loader.load();
            dadoController = loader.getController();

            ctdDado.getChildren().setAll(root);
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

    /*
    *   GETTERS AND SETTERS
    * */
    public void setJugadoresTablero(List<Jugador> jugadoresTablero) {
        this.jugadoresTablero = jugadoresTablero;
        this.todosLosJugadores = new ArrayList<>(jugadoresTablero);
        iniciarRonda(new ArrayList<>(todosLosJugadores)); // Copia defensiva
    }
}
