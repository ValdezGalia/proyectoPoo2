package org.example.juego.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

public class EstadisticaController {
    @FXML
    private TableView<RankingJugador> tablaRanking;
    @FXML
    private TableColumn<RankingJugador, String> colAlias;
    @FXML
    private TableColumn<RankingJugador, Integer> colPartidasJugadas;
    @FXML
    private TableColumn<RankingJugador, Integer> colPartidasGanadas;
    @FXML
    private TableColumn<RankingJugador, Integer> colPartidasPerdidas;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasGeografia;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasHistoria;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasCiencias;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasArte;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasDeportes;
    @FXML
    private TableColumn<RankingJugador, Integer> colCorrectasEntretenimiento;
    @FXML
    private TableColumn<RankingJugador, Double> colTiempoTotal;

    @FXML
    public void initialize() {
        tablaRanking.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colPartidasJugadas.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));
        colPartidasGanadas.setCellValueFactory(new PropertyValueFactory<>("partidasGanadas"));
        colPartidasPerdidas.setCellValueFactory(new PropertyValueFactory<>("partidasPerdidas"));
        colCorrectasGeografia.setCellValueFactory(new PropertyValueFactory<>("correctasGeografia"));
        colCorrectasHistoria.setCellValueFactory(new PropertyValueFactory<>("correctasHistoria"));
        colCorrectasCiencias.setCellValueFactory(new PropertyValueFactory<>("correctasCienciasYNaturaleza"));
        colCorrectasArte.setCellValueFactory(new PropertyValueFactory<>("correctasArteYLiteratura"));
        colCorrectasDeportes.setCellValueFactory(new PropertyValueFactory<>("correctasDeportesYPasatiempos"));
        colCorrectasEntretenimiento.setCellValueFactory(new PropertyValueFactory<>("correctasEntretenimiento"));
        colTiempoTotal.setCellValueFactory(new PropertyValueFactory<>("tiempoTotal"));

        // Datos de ejemplo, reemplazar por datos reales
        ObservableList<RankingJugador> ranking = FXCollections.observableArrayList(
            new RankingJugador("Ana", 10, 5, 5, 3, 4, 5, 2, 6, 7, 123.5),
            new RankingJugador("Luis", 8, 3, 5, 2, 2, 3, 1, 2, 3, 98.2),
            new RankingJugador("Marta", 6, 2, 4, 1, 1, 2, 1, 1, 2, 75.0)
        );
        tablaRanking.setItems(ranking);
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    // Clase interna para la tabla de ranking
    public static class RankingJugador {
        private final String alias;
        private final Integer partidasJugadas;
        private final Integer partidasGanadas;
        private final Integer partidasPerdidas;
        private final Integer correctasGeografia;
        private final Integer correctasHistoria;
        private final Integer correctasCienciasYNaturaleza;
        private final Integer correctasArteYLiteratura;
        private final Integer correctasDeportesYPasatiempos;
        private final Integer correctasEntretenimiento;
        private final Double tiempoTotal;

        public RankingJugador(String alias, Integer partidasJugadas, Integer partidasGanadas, Integer partidasPerdidas,
                              Integer correctasGeografia, Integer correctasHistoria, Integer correctasCienciasYNaturaleza,
                              Integer correctasArteYLiteratura, Integer correctasDeportesYPasatiempos, Integer correctasEntretenimiento, Double tiempoTotal) {
            this.alias = alias;
            this.partidasJugadas = partidasJugadas;
            this.partidasGanadas = partidasGanadas;
            this.partidasPerdidas = partidasPerdidas;
            this.correctasGeografia = correctasGeografia;
            this.correctasHistoria = correctasHistoria;
            this.correctasCienciasYNaturaleza = correctasCienciasYNaturaleza;
            this.correctasArteYLiteratura = correctasArteYLiteratura;
            this.correctasDeportesYPasatiempos = correctasDeportesYPasatiempos;
            this.correctasEntretenimiento = correctasEntretenimiento;
            this.tiempoTotal = tiempoTotal;
        }
        public String getAlias() { return alias; }
        public Integer getPartidasJugadas() { return partidasJugadas; }
        public Integer getPartidasGanadas() { return partidasGanadas; }
        public Integer getPartidasPerdidas() { return partidasPerdidas; }
        public Integer getCorrectasGeografia() { return correctasGeografia; }
        public Integer getCorrectasHistoria() { return correctasHistoria; }
        public Integer getCorrectasCienciasYNaturaleza() { return correctasCienciasYNaturaleza; }
        public Integer getCorrectasArteYLiteratura() { return correctasArteYLiteratura; }
        public Integer getCorrectasDeportesYPasatiempos() { return correctasDeportesYPasatiempos; }
        public Integer getCorrectasEntretenimiento() { return correctasEntretenimiento; }
        public Double getTiempoTotal() { return tiempoTotal; }
    }
}
