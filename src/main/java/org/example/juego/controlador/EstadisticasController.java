package org.example.juego.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.juego.modelo.Jugador;
import org.example.juego.modelo.ListaJugador;
import org.example.juego.modelo.Tablero;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticasController {
    @FXML
    private TableView<EstadisticaJugador> tablaEstadisticas;
    @FXML
    private TableColumn<EstadisticaJugador, String> colAlias;
    @FXML
    private TableColumn<EstadisticaJugador, Integer> colPartidasJugadas;
    @FXML
    private TableColumn<EstadisticaJugador, Integer> colPartidasGanadas;
    @FXML
    private TableColumn<EstadisticaJugador, Integer> colPartidasPerdidas;
    @FXML
    private TableColumn<EstadisticaJugador, String> colPreguntasPorCategoria;
    @FXML
    private TableColumn<EstadisticaJugador, Double> colTiempoTotal;

    @FXML
    public void initialize() {
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colPartidasJugadas.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));
        colPartidasGanadas.setCellValueFactory(new PropertyValueFactory<>("partidasGanadas"));
        colPartidasPerdidas.setCellValueFactory(new PropertyValueFactory<>("partidasPerdidas"));
        colPreguntasPorCategoria.setCellValueFactory(new PropertyValueFactory<>("preguntasPorCategoria"));
        colTiempoTotal.setCellValueFactory(new PropertyValueFactory<>("tiempoTotal"));
        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        // Aquí deberías obtener la lista de jugadores activos y sus estadísticas
        // Este ejemplo asume que hay una clase ListaJugador con método getUsuarios()
        List<Jugador> jugadores = ListaJugador.getInstancia().getUsuarios();
        List<String> categorias = Tablero.getInstancia().getCategorias();
        ObservableList<EstadisticaJugador> datos = FXCollections.observableArrayList();
        for (Jugador j : jugadores) {
            StringBuilder cat = new StringBuilder();
            for (String c : categorias) {
                int correctas = j.getCorrectasPorCategoria(c);
                cat.append(c).append(": ").append(correctas).append("  ");
            }
            datos.add(new EstadisticaJugador(
                    j.getAlias(),
                    j.getPartidasJugadas(),
                    j.getPartidasGanadas(),
                    j.getPartidasPerdidas(),
                    cat.toString(),
                    j.getTiempoTotalCorrectas()
            ));
        }
        tablaEstadisticas.setItems(datos);
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) tablaEstadisticas.getScene().getWindow();
        stage.close();
    }

    public static class EstadisticaJugador {
        private final String alias;
        private final int partidasJugadas;
        private final int partidasGanadas;
        private final int partidasPerdidas;
        private final String preguntasPorCategoria;
        private final double tiempoTotal;

        public EstadisticaJugador(String alias, int partidasJugadas, int partidasGanadas, int partidasPerdidas, String preguntasPorCategoria, double tiempoTotal) {
            this.alias = alias;
            this.partidasJugadas = partidasJugadas;
            this.partidasGanadas = partidasGanadas;
            this.partidasPerdidas = partidasPerdidas;
            this.preguntasPorCategoria = preguntasPorCategoria;
            this.tiempoTotal = tiempoTotal;
        }
        public String getAlias() { return alias; }
        public int getPartidasJugadas() { return partidasJugadas; }
        public int getPartidasGanadas() { return partidasGanadas; }
        public int getPartidasPerdidas() { return partidasPerdidas; }
        public String getPreguntasPorCategoria() { return preguntasPorCategoria; }
        public double getTiempoTotal() { return tiempoTotal; }
    }
}

