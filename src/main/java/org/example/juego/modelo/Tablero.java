package org.example.juego.modelo;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.*;


public class Tablero {
    private final Casilla[][] casillas = new Casilla[21][21];
    private final Map<Casilla, List<Casilla>> vecinosMapa = new HashMap<>();
    private static final String[] COLORS = { "yellow", "green", "red", "purple", "orange", "blue", "gray"};
    private static final String[] BORDERCOLORS = { "#CCCC00",  "#006400",  "#8B0000",  "#4B0082", "#FF8C00",  "#00008B", "#585858"};
    private int contadorColors = 0;
    private final Map<Casilla, String> colorAsignado = new HashMap<>();
    private int currentRow = 10, currentCol = 10;
    private Pane fichaVisual;
    private final Map<Casilla, Pane> paneMap = new HashMap<>();
    private final Map<Jugador, Node> fichaPorJugador = new HashMap<>();

    private static Tablero instancia;
    private List<String> categorias = Arrays.asList("Historia", "Ciencia", "Arte", "Deportes", "Geografía", "Entretenimiento"); // Ajusta según tus categorías reales

    public static Tablero getInstancia() {
        if (instancia == null) {
            instancia = new Tablero();
        }
        return instancia;
    }
    public List<String> getCategorias() {
        return categorias;
    }

    public Tablero() {
    }


    public void inicializarTablero(GridPane tablero, List<Jugador> jugadores) {
        // Solo crear casillas y colores si el tablero está vacío
        if (paneMap.isEmpty()) {
            int filas = casillas.length;
            int cols  = casillas[0].length;
            int filaCentral = filas / 2;
            int radio = filaCentral;
            for (int fila = 0; fila < filas; fila++) {
                for (int columna = 0; columna < cols; columna++) {
                    String tipo = determinarTipo(fila, columna, filaCentral, radio);
                    Casilla casilla = new Casilla(fila, columna, tipo);
                    Pane celda = new Pane();
                    celda.setOpacity(1.0);
                    celda.setUserData(celda.getOpacity());
                    if (fila == columna) {
                        asignarColores(celda, casilla);
                    } else if (fila == filas - 1 - columna) {
                        asignarColores(celda, casilla);
                    } else if (fila == filaCentral) {
                        casilla.setTipo("VACIO");
                        casilla.setVisible(false);
                        celda.setStyle("-fx-background-color: white;-fx-background-radius:3; -fx-border-width: 2; -fx-border-color: #e1e1e1;");
                    } else if( columna == filaCentral ){
                        asignarColores(celda, casilla);
                    } else if (isCircunferencia(fila, columna, filaCentral, radio)) {
                        asignarColores(celda, casilla);
                    }else{
                        celda.setStyle("-fx-background-color: white; -fx-background-radius:3; -fx-border-width: 2; -fx-border-color: #e1e1e1;");
                    }
                    if (isEsquinaEspecial(fila, columna, filas)) {
                        casilla.setTipo("VACIO");
                        casilla.setVisible(false);
                        celda.setStyle("-fx-background-color: white; -fx-background-radius:3; -fx-border-width: 2; -fx-border-color: #e1e1e1;");
                    }
                    if(fila == 10 && columna == 10){
                        celda.setStyle("-fx-background-color: gray; -fx-background-radius:5; -fx-border-width: 5; -fx-border-radius: 2;");
                    }
                    tablero.add(celda, columna, fila);
                    casillas[fila][columna] = casilla;
                    paneMap.put(casilla, celda);
                }
            }
            // Construir adyacencia solo una vez
            construirAdyacencia();
        }
        // Solo eliminar y volver a agregar fichas visuales, sin tocar casillas ni adyacencia
        for (Node ficha : fichaPorJugador.values()) {
            tablero.getChildren().remove(ficha);
        }
        fichaPorJugador.clear();
        for (Jugador jugador : jugadores) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/FichaJugadorView.fxml"));
                AnchorPane ficha = loader.load();
                ficha.setPrefSize(30, 30);
                ficha.setMaxSize(30, 30);
                ficha.setMinSize(30, 30);
                fichaPorJugador.put(jugador, ficha);
                // Rellenar quesitos ya ganados
                Object controller = loader.getController();
                if (controller instanceof org.example.juego.controlador.FichaJugadorController fichaController) {
                    for (String cat : jugador.getQuesitosRellenos()) {
                        fichaController.rellenarQuesito(cat);
                    }
                }
                tablero.add(ficha, jugador.getColumnaActual(), jugador.getFilaActual());
                GridPane.setHalignment(ficha, HPos.CENTER);
                GridPane.setValignment(ficha, VPos.CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isCircunferencia(int f, int c, int cx, int radio) {
        double dist = Math.hypot(f - cx, c - cx);
        return Math.abs(dist - radio) < 0.5;
    }

    private boolean isEsquinaEspecial(int f, int c, int n) {
        int[][] esquinas = {{0,0},{1,1},{2,2},{n-1,n-1},{n-2,n-2},{n-3,n-3},{n-1,0},{n-2,1},{n-3,2},{2,n-3},{1,n-2},{0,n-1}};
        for (int[] e:esquinas) if (f==e[0] && c==e[1]) return true;
        return false;
    }

    private String determinarTipo(int fila, int columna, int centro, int radio) {
        double dist = Math.hypot(fila - centro, columna - centro);
        boolean esCirculo = Math.abs(dist - radio) < 0.5;
        boolean diagP     = (fila == columna);
        boolean diagS     = (fila + columna == 2 * centro);
        boolean linea     = (fila == centro || columna == centro);

        // Centro estrictamente
        if (fila == centro && columna == centro) {
            return "CENTRO";
        }

        // Intersección: parte de la circunferencia y de alguna línea
        if (esCirculo && (diagP || diagS || linea)) {
            return "INTERSECCION";
        }

        // Círculo puro (pero sin que sea intersección)
        if (esCirculo) {
            return "CIRCULO";
        }

        // Cualquier línea (vertical/horizontal/diagonales) que no sea circunferencia
        if (diagP || diagS || linea) {
            return "LINEA";
        }

        return "VACIO";
    }

    private void construirAdyacencia() {
        int size = 21;
        int centro = size / 2;

        // 3.a) Inicializar lista vacía para cada casilla visible y no VACIO
        vecinosMapa.clear();
        for (int fila = 0; fila < size; fila++) {
            for (int columna = 0; columna < size; columna++) {
                Casilla x = casillas[fila][columna];
                if (x.isVisible() && !"VACIO".equals(x.getTipo())) {
                    vecinosMapa.put(x, new ArrayList<>());
                }
            }
        }

        // 3.b) Construir listaCircular con CIRCULO + INTERSECCION
        List<Casilla> listaCircular = new ArrayList<>();
        for (int fila = 0; fila < size; fila++) {
            for (int columna = 0; columna < size; columna++) {
                Casilla x = casillas[fila][columna];
                if (x.isVisible() &&
                        ("CIRCULO".equals(x.getTipo()) || "INTERSECCION".equals(x.getTipo()))) {
                    listaCircular.add(x);
                }
            }
        }
        listaCircular.sort(Comparator.comparingDouble(
                cas -> Math.atan2(cas.getX() - centro, cas.getY() - centro)
        ));

        for (int i = 0; i < listaCircular.size(); i++) {
            Casilla actual = listaCircular.get(i);
            Casilla sig    = listaCircular.get((i + 1) % listaCircular.size());
            Casilla ant    = listaCircular.get((i - 1 + listaCircular.size()) % listaCircular.size());
            vecinosMapa.get(actual).add(sig);
            vecinosMapa.get(actual).add(ant);
        }

        // 3.c) Conectar CENTRO, LINEA e INTERSECCION a lo largo de rectas y diagonales
        for (int fila = 0; fila < size; fila++) {
            for (int columna = 0; columna < size; columna++) {
                Casilla x = casillas[fila][columna];
                if (!x.isVisible() || "VACIO".equals(x.getTipo())) continue;

                // Centro
                if ("CENTRO".equals(x.getTipo())) {
                    int[][] dirs = {
                            {-1, -1}, {-1,  1},
                            { 1, -1}, { 1,  1},
                            {-1,  0}, { 1,  0}
                    };
                    for (int[] d : dirs) {
                        int fn = fila + d[0], cn = columna + d[1];
                        if (esValidaParaLinea(fn, cn)) {
                            Casilla vecino = casillas[fn][cn];
                            vecinosMapa.get(x).add(vecino);
                            vecinosMapa.get(vecino).add(x);
                        }
                    }
                    continue;
                }

                boolean esLineaCentral   = (columna == centro);
                boolean esDiagPrincipal  = (fila == columna);
                boolean esDiagSecundaria = (fila + columna == size - 1);

                // Línea vertical/ horizontal
                if (esLineaCentral) {
                    for (int df : new int[]{-1, 1}) {
                        int fn = fila + df;
                        if (esValidaParaLinea(fn, columna)) {
                            Casilla vecino = casillas[fn][columna];
                            vecinosMapa.get(x).add(vecino);
                            vecinosMapa.get(vecino).add(x);
                        }
                    }
                }

                // Diagonal principal
                if (esDiagPrincipal) {
                    for (int[] d : new int[][]{{-1, -1}, {1, 1}}) {
                        int fn = fila + d[0], cn = columna + d[1];
                        if (esValidaParaLinea(fn, cn)) {
                            Casilla vecino = casillas[fn][cn];
                            vecinosMapa.get(x).add(vecino);
                            vecinosMapa.get(vecino).add(x);
                        }
                    }
                }

                // Diagonal secundaria
                if (esDiagSecundaria) {
                    for (int[] d : new int[][]{{-1, 1}, {1, -1}}) {
                        int fn = fila + d[0], cn = columna + d[1];
                        if (esValidaParaLinea(fn, cn)) {
                            Casilla vecino = casillas[fn][cn];
                            vecinosMapa.get(x).add(vecino);
                            vecinosMapa.get(vecino).add(x);
                        }
                    }
                }
            }
        }

        // 3.d) Conexiones adicionales: LINEA ↔ INTERSECCION y INTERSECCION ↔ CIRCULO
        int[] df8 = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc8 = {-1,  0,  1,-1, 1,-1, 0, 1};
        for (int fila = 0; fila < size; fila++) {
            for (int columna = 0; columna < size; columna++) {
                Casilla actual = casillas[fila][columna];
                if (!actual.isVisible()) continue;

                String tipoAct = actual.getTipo();
                if (!"LINEA".equals(tipoAct) && !"INTERSECCION".equals(tipoAct)) continue;

                for (int k = 0; k < 8; k++) {
                    int fn = fila + df8[k], cn = columna + dc8[k];
                    if (fn < 0 || fn >= size || cn < 0 || cn >= size) continue;
                    Casilla vecino = casillas[fn][cn];
                    if (!vecino.isVisible()) continue;

                    String tipoVec = vecino.getTipo();
                    if ("LINEA".equals(tipoAct) && "INTERSECCION".equals(tipoVec)
                            || "INTERSECCION".equals(tipoAct) && "CIRCULO".equals(tipoVec)) {
                        vecinosMapa.get(actual).add(vecino);
                        vecinosMapa.get(vecino).add(actual);
                    }
                }
            }
        }
    }

    // Helper para validar línea (igual que tu versión de consola)
    private boolean esValidaParaLinea(int f, int c) {
        int size = 21;
        if (f < 0 || f >= size || c < 0 || c >= size) return false;
        Casilla x = casillas[f][c];
        return x.isVisible() && !"VACIO".equals(x.getTipo());
    }

    public List<Casilla> getOpcionesMovimiento(int pasos) {
        Casilla origen = casillas[currentRow][currentCol];
        if(pasos<=0) return Collections.emptyList();

        Map<Casilla,Integer> dist = new HashMap<>();
        Queue<Casilla> cola = new LinkedList<>();
        dist.put(origen,0);
        cola.add(origen);

        List<Casilla> res = new ArrayList<>();

        while(!cola.isEmpty()){
            Casilla u = cola.poll();
            int d = dist.get(u);
            if(d==pasos){res.add(u); continue;}
            for(Casilla v: vecinosMapa.get(u)){
                if(!dist.containsKey(v)){
                    dist.put(v,d+1);
                    cola.add(v);
                }
            }
        }

        return res;
    }

    // Nuevo método para obtener opciones de movimiento desde la posición de un jugador
    public List<Casilla> getOpcionesMovimiento(int pasos, Jugador jugador) {
        Casilla origen = casillas[jugador.getFilaActual()][jugador.getColumnaActual()];
        if(pasos<=0) return Collections.emptyList();
        Map<Casilla,Integer> dist = new HashMap<>();
        Queue<Casilla> cola = new LinkedList<>();
        dist.put(origen,0);
        cola.add(origen);
        List<Casilla> res = new ArrayList<>();
        while(!cola.isEmpty()){
            Casilla u = cola.poll();
            int d = dist.get(u);
            if(d==pasos){res.add(u); continue;}
            for(Casilla v: vecinosMapa.get(u)){
                if(!dist.containsKey(v)){
                    dist.put(v,d+1);
                    cola.add(v);
                }
            }
        }
        return res;
    }

    public void moverFicha(GridPane tablero, Casilla destino, Jugador jugadorTurno) {
        Node ficha = fichaPorJugador.get(jugadorTurno);
        if (ficha != null) {
            tablero.getChildren().remove(ficha);
            jugadorTurno.setFilaActual(destino.getX());
            jugadorTurno.setColumnaActual(destino.getY());
            tablero.add(ficha, destino.getY(), destino.getX());
            GridPane.setHalignment(ficha, HPos.CENTER);
            GridPane.setValignment(ficha, VPos.CENTER);
        }
    }

    public Pane getPaneDeCasilla(Casilla cas) {
        return paneMap.get(cas);
    }

    private void asignarColores(Pane casilla, Casilla casDinamica) {
        if( contadorColors == 7 ) contadorColors = 0;
        casilla.setStyle("-fx-background-color: " + COLORS[contadorColors] + "; -fx-border-color: " + BORDERCOLORS[contadorColors] + "; -fx-background-radius:5; -fx-border-width: 5; -fx-border-radius: 2");
        casDinamica.setColor(COLORS[contadorColors]);
        contadorColors++;
    }

    public Node getFichaVisual(Jugador jugador) {
        return fichaPorJugador.get(jugador);
    }
}
