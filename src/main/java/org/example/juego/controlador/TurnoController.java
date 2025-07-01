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
import org.example.juego.modelo.ListaJugador;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador encargado de gestionar la ronda de lanzamientos de dado y el orden de los jugadores.
 * Permite manejar empates, mostrar el jugador en turno y pasar el ranking final al tablero.
 */
public class TurnoController {
    /** Contenedor del dado en la interfaz. */
    @FXML
    private AnchorPane ctdDado;
    /** Muestra el alias del jugador en turno. */
    @FXML
    private Label lblJugadorAlias;
    /** Imagen de avatar del jugador en turno. */
    @FXML
    private ImageView imgAvatar;
    /** Botón para lanzar el dado. */
    @FXML
    private Button btnLanzarDado;
    /** Etiqueta para mostrar información y mensajes. */
    @FXML
    private Label lblInfo;
    /** Lista de jugadores que participan en el tablero. */
    private List<Jugador> jugadoresTablero;
    /** Índice del jugador en turno dentro de la ronda. */
    private static int indiceJugador = 0;
    /** Controlador del dado para animar y obtener el valor. */
    private DadoController dadoController;
    /** Jugadores que participan en la ronda actual. */
    private List<Jugador> rondaActual;
    /** Lista final ordenada de jugadores según el resultado del dado. */
    private final List<Jugador> ordenFinal = new ArrayList<>();
    /** Todos los jugadores originales del tablero. */
    private List<Jugador> todosLosJugadores = new ArrayList<>();

    /**
     * Inicia una nueva ronda de lanzamientos con la lista de jugadores indicada.
     * @param jugadores Lista de jugadores que participan en la ronda.
     */
    public void iniciarRonda(List<Jugador> jugadores) {
        this.jugadoresTablero = jugadores;
        this.rondaActual = new ArrayList<>(jugadores);
        indiceJugador = 0;
        btnLanzarDado.setDisable(false);
        renderJugador();  // prepara UI para el primer de esta ronda
    }

    /**
     * Lanza el dado para el jugador en turno y gestiona el avance de la ronda.
     * Si todos han lanzado, maneja el final de la ronda (empates o ranking).
     */
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
            if (indiceJugador < rondaActual.size()) {
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
            }
        }));
    }

    /**
     * Maneja el final de la ronda: resuelve empates o genera el ranking final.
     */
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

            LinkedList<Jugador> rankingFinal = new LinkedList<>(ordenFinal);

            for (Jugador j : todosLosJugadores) {
                if (!rankingFinal.contains(j)) {
                    rankingFinal.add(j);
                }
            }
            tableroConJugadoresOrdenados(rankingFinal, ctdDado);
        }
    }

    /**
     * Cierra la ventana actual y abre el tablero con los jugadores ordenados por el resultado del dado.
     * @param jugadoresOrdenados Lista enlazada de jugadores ordenados.
     * @param nodoCualquieraDelModalActual Nodo de la ventana actual para cerrarla.
     */
    public void tableroConJugadoresOrdenados(LinkedList<Jugador> jugadoresOrdenados, Node nodoCualquieraDelModalActual) {
        try {
            // Cerramos el modal actual usando el nodo que pertenece a esa ventana
            Stage ventanaActual = (Stage) nodoCualquieraDelModalActual.getScene().getWindow();
            ventanaActual.close();

            // Cargamos la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/TableroView.fxml"));
            Parent root = loader.load();

            // Pasamos la lista al nuevo controlador
            TableroController ctrl = loader.getController();
            ListaJugador listaJugador = new ListaJugador(jugadoresOrdenados);
            ctrl.setJugadores(listaJugador);
            // Abrimos el nuevo tablero en un modal nuevo
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(new Scene(root));
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.setMaximized(true);
            nuevoStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra en la interfaz el jugador que debe lanzar el dado, su avatar y alias.
     * Si es el primer jugador de la ronda, prepara el dado.
     */
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

    /**
     * Carga y muestra la vista del dado para el turno actual.
     */
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
    /**
     * Establece la lista de jugadores del tablero y comienza la ronda de lanzamientos.
     * @param jugadoresTablero Lista de jugadores que participarán en la ronda.
     */
    public void setJugadoresTablero(List<Jugador> jugadoresTablero) {
        this.jugadoresTablero = jugadoresTablero;
        this.todosLosJugadores = new ArrayList<>(jugadoresTablero);
        iniciarRonda(new ArrayList<>(todosLosJugadores)); // Copia defensiva
    }
}
