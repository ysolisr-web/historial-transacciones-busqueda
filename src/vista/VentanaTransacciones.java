package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import logica.GestorTransacciones;
import logica.GestorTransacciones.ResultadoBusqueda;
import modelo.Transaccion;

public class VentanaTransacciones extends JFrame {

    private Transaccion[] historial;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaTransacciones;
    private final JTextField txtNumeroBuscar;
    private final JComboBox<String> comboTipo;
    private final JLabel lblEstadoOrden;
    private final JTextArea areaLog;

    public VentanaTransacciones() {
        historial = GestorTransacciones.generarHistorialInicial();

        setTitle("Ejercicio 23 - Historial de Transacciones (Búsqueda Lineal y Binaria)");
        setSize(940, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ==================== PANEL SUPERIOR (CONTROLES) ====================
        JPanel panelControles = new JPanel(new GridLayout(2, 1, 5, 5));
        panelControles.setBorder(BorderFactory.createTitledBorder("Operaciones de Búsqueda y Ordenamiento"));

        // Fila 1: Búsquedas por número
        JPanel filaNumero = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaNumero.add(new JLabel("N.° Transacción:"));
        txtNumeroBuscar = new JTextField(8);
        txtNumeroBuscar.setText("1060");
        filaNumero.add(txtNumeroBuscar);

        JButton btnLineal = new JButton("Búsqueda Lineal");
        JButton btnOrdenar = new JButton("Ordenar por N.° (Manual)");
        btnOrdenar.setBackground(new Color(28, 93, 153));
        btnOrdenar.setForeground(Color.WHITE);
        btnOrdenar.setFocusPainted(false);

        JButton btnBinaria = new JButton("Búsqueda Binaria");
        btnBinaria.setBackground(new Color(33, 115, 70));
        btnBinaria.setForeground(Color.WHITE);
        btnBinaria.setFocusPainted(false);

        lblEstadoOrden = new JLabel("Estado: DESORDENADO");
        lblEstadoOrden.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEstadoOrden.setForeground(new Color(180, 40, 40));

        filaNumero.add(btnLineal);
        filaNumero.add(btnOrdenar);
        filaNumero.add(btnBinaria);
        filaNumero.add(lblEstadoOrden);

        // Fila 2: Filtrado por tipo y reinicio
        JPanel filaTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaTipo.add(new JLabel("Filtrar por Tipo:"));
        String[] tipos = {"Depósito", "Retiro", "Transferencia", "Pago Servicio"};
        comboTipo = new JComboBox<>(tipos);
        filaTipo.add(comboTipo);

        JButton btnFiltrarTipo = new JButton("Buscar por Tipo");
        JButton btnMostrarTodo = new JButton("Mostrar Todo");
        JButton btnReiniciar = new JButton("Restaurar Datos Desordenados");

        filaTipo.add(btnFiltrarTipo);
        filaTipo.add(btnMostrarTodo);
        filaTipo.add(btnReiniciar);

        panelControles.add(filaNumero);
        panelControles.add(filaTipo);
        add(panelControles, BorderLayout.NORTH);

        // ==================== PANEL CENTRAL (TABLA) ====================
        String[] columnas = {"Índice [i]", "N.° Transacción", "Fecha", "Tipo", "Valor ($)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTransacciones = new JTable(modeloTabla);
        tablaTransacciones.setRowHeight(25);
        tablaTransacciones.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaTransacciones);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Historial de Transacciones"));
        add(scrollTabla, BorderLayout.CENTER);

        // ==================== PANEL INFERIOR (RESULTADOS Y ANÁLISIS) ====================
        areaLog = new JTextArea(7, 60);
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("SansSerif", Font.PLAIN, 13));
        areaLog.setText("Historial cargado con 10 transacciones desordenadas. Puedes probar Búsqueda Lineal, filtrar por Tipo, Ordenar manualmente y ejecutar Búsqueda Binaria (incluyendo números inexistentes para ver su punto de inserción).");

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Resultado de la Operación y Complejidad"));
        add(scrollLog, BorderLayout.SOUTH);

        // ==================== EVENTOS ====================
        btnLineal.addActionListener(e -> ejecutarBusquedaLineal());
        btnOrdenar.addActionListener(e -> ejecutarOrdenamiento());
        btnBinaria.addActionListener(e -> ejecutarBusquedaBinaria());
        btnFiltrarTipo.addActionListener(e -> ejecutarFiltroTipo());
        btnMostrarTodo.addActionListener(e -> cargarTabla(historial));
        btnReiniciar.addActionListener(e -> {
            historial = GestorTransacciones.generarHistorialInicial();
            actualizarEstadoOrden();
            cargarTabla(historial);
            areaLog.setText("Datos restaurados al estado inicial desordenado.");
        });

        cargarTabla(historial);
        actualizarEstadoOrden();
    }

    private void cargarTabla(Transaccion[] datos) {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < datos.length; i++) {
            Transaccion t = datos[i];
            modeloTabla.addRow(new Object[]{
                i,
                t.getNumero(),
                t.getFecha(),
                t.getTipo(),
                String.format("$%,.2f", t.getValor())
            });
        }
    }

    private void actualizarEstadoOrden() {
        boolean ordenado = GestorTransacciones.estaOrdenadoPorNumero(historial);
        if (ordenado) {
            lblEstadoOrden.setText("Estado: ORDENADO (Listo para Binaria)");
            lblEstadoOrden.setForeground(new Color(33, 115, 70));
        } else {
            lblEstadoOrden.setText("Estado: DESORDENADO");
            lblEstadoOrden.setForeground(new Color(180, 40, 40));
        }
    }

    private Integer leerNumeroIngresado() {
        try {
            return Integer.parseInt(txtNumeroBuscar.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número entero válido para la transacción (ej. 1060 o 1040).",
                    "Entrada inválida",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void ejecutarBusquedaLineal() {
        Integer numero = leerNumeroIngresado();
        if (numero == null) return;

        cargarTabla(historial);
        ResultadoBusqueda res = GestorTransacciones.busquedaLinealPorNumero(historial, numero);

        if (res.fueEncontrado()) {
            int idx = res.getIndice();
            tablaTransacciones.setRowSelectionInterval(idx, idx);
            areaLog.setText(String.format(
                    "[Búsqueda Lineal - O(n)] Transacción #%d ENCONTRADA en el índice [%d] tras %d comparaciones.\nDetalle: %s",
                    numero, idx, res.getComparaciones(), historial[idx].toString()
            ));
        } else {
            tablaTransacciones.clearSelection();
            areaLog.setText(String.format(
                    "[Búsqueda Lineal - O(n)] La transacción #%d NO EXISTE en el historial (se recorrieron los %d elementos).",
                    numero, res.getComparaciones()
            ));
        }
    }

    private void ejecutarOrdenamiento() {
        GestorTransacciones.ordenarPorNumeroManual(historial);
        actualizarEstadoOrden();
        cargarTabla(historial);
        areaLog.setText("Historial ordenado ascendentemente por número de transacción mediante Insertion Sort manual. Ya se cumple la precondición para Búsqueda Binaria.");
    }

    private void ejecutarBusquedaBinaria() {
        if (!GestorTransacciones.estaOrdenadoPorNumero(historial)) {
            JOptionPane.showMessageDialog(this,
                    "Precondición no cumplida: el historial debe estar ordenado por número antes de ejecutar Búsqueda Binaria.\nPresione primero 'Ordenar por N.° (Manual)'.",
                    "Arreglo desordenado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer numero = leerNumeroIngresado();
        if (numero == null) return;

        cargarTabla(historial);
        ResultadoBusqueda res = GestorTransacciones.busquedaBinariaPorNumero(historial, numero);

        if (res.fueEncontrado()) {
            int idx = res.getIndice();
            tablaTransacciones.setRowSelectionInterval(idx, idx);
            areaLog.setText(String.format(
                    "[Búsqueda Binaria - O(log n)] Transacción #%d ENCONTRADA en el índice [%d] usando solo %d comparaciones.\nDetalle: %s",
                    numero, idx, res.getComparaciones(), historial[idx].toString()
            ));
        } else {
            tablaTransacciones.clearSelection();
            int posInsercion = res.getPuntoInsercion();
            areaLog.setText(String.format(
                    "[Búsqueda Binaria - O(log n)] La transacción #%d NO EXISTE (comparaciones: %d).\n"
                    + "Valor retornado: %d → Punto de inserción sugerido: índice [%d] para mantener el orden ascendente.",
                    numero, res.getComparaciones(), res.getIndice(), posInsercion
            ));

            int opcion = JOptionPane.showConfirmDialog(this,
                    String.format("La transacción #%d no existe.\nSu punto de inserción ordenado es el índice [%d].\n¿Desea insertarla ahora en esa posición?", numero, posInsercion),
                    "Punto de inserción encontrado",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                String tipoSeleccionado = comboTipo.getSelectedItem().toString();
                Transaccion nueva = new Transaccion(numero, "2026-11-13", tipoSeleccionado, 500000.00);
                historial = GestorTransacciones.insertarEnPosicion(historial, nueva, posInsercion);
                cargarTabla(historial);
                tablaTransacciones.setRowSelectionInterval(posInsercion, posInsercion);
                areaLog.setText(String.format(
                        "Transacción #%d insertada correctamente en el índice [%d]. El arreglo conserva el orden ascendente.",
                        numero, posInsercion
                ));
            }
        }
    }

    private void ejecutarFiltroTipo() {
        String tipo = comboTipo.getSelectedItem().toString();
        Transaccion[] filtradas = GestorTransacciones.buscarPorTipo(historial, tipo);
        cargarTabla(filtradas);
        areaLog.setText(String.format(
                "[Búsqueda Secuencial por Tipo - O(n)] Se encontraron %d transacciones de tipo '%s'.",
                filtradas.length, tipo
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaTransacciones().setVisible(true);
        });
    }
}