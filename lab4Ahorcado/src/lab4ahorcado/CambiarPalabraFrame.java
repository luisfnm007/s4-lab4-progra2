package lab4ahorcado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author marye
 */
public class CambiarPalabraFrame extends JFrame {

    private final AdminPalabraSecretas admin;
    private final DefaultListModel<String> listModel;
    private final JList<String> listaPalabras;
    private final JTextField tfPalabra;
    private final JFrame menuAnterior;

    public CambiarPalabraFrame(JFrame menuAnterior) {
        super("CAMBIAR PALABRA");
        this.menuAnterior = menuAnterior;
        this.admin = new AdminPalabraSecretas();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (menuAnterior != null) {
                    menuAnterior.setVisible(true);
                }
            }
        });

        JPanel PPrinicipal = new JPanel(new BorderLayout(12, 12));
        PPrinicipal.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(PPrinicipal);

        JLabel titulo = new JLabel("CAMBIAR PALABRA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        PPrinicipal.add(titulo, BorderLayout.NORTH);

        JSplitPane lista = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        lista.setResizeWeight(0.55);
        PPrinicipal.add(lista, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        listaPalabras = new JList<>(listModel);
        listaPalabras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPalabras.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(listaPalabras);
        JPanel panelLista = new JPanel(new BorderLayout(8, 8));
        JLabel lblLista = new JLabel("Palabras actuales:");
        lblLista.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panelLista.add(lblLista, BorderLayout.NORTH);
        panelLista.add(scroll, BorderLayout.CENTER);

        lista.setLeftComponent(panelLista);

        JPanel PDerecho = new JPanel(new BorderLayout(8, 8));
        JPanel pnlForm = new JPanel(new BorderLayout(6, 6));
        pnlForm.setBorder(new EmptyBorder(6, 6, 6, 6));
        JLabel lblAgregar = new JLabel("Agregar palabra:");
        lblAgregar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        PDerecho.add(lblAgregar, BorderLayout.NORTH);

        tfPalabra = new JTextField();
        tfPalabra.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnlForm.add(tfPalabra, BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnBorrar = new JButton("Borrar Palabra");
        JButton btnLimpiar = new JButton("Limpiar");

        btnBorrar.setEnabled(false);

        pnlAcciones.add(btnAgregar);
        pnlAcciones.add(btnBorrar);
        pnlAcciones.add(btnLimpiar);

        pnlForm.add(pnlAcciones, BorderLayout.SOUTH);
        PDerecho.add(pnlForm, BorderLayout.CENTER);

        JLabel Validacion = new JLabel();
        Validacion.setFont(new Font("SansSerif", Font.ITALIC, 12));
        PDerecho.add(Validacion, BorderLayout.SOUTH);

        lista.setRightComponent(PDerecho);

        JPanel PBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnGuardar = new JButton("Guardar cambios");
        JButton btnVolver = new JButton("Regresar a menu");
        PBotones.add(btnGuardar);
        PBotones.add(btnVolver);
        PPrinicipal.add(PBotones, BorderLayout.SOUTH);

        cargarListaDesdeAdmin();

        listaPalabras.addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) {
                btnBorrar.setEnabled(listaPalabras.getSelectedIndex() != -1);
                String sel = listaPalabras.getSelectedValue();
                tfPalabra.setText(sel != null ? sel : "");
            }
        });

        btnAgregar.addActionListener(e -> {
            String palabra = tfPalabra.getText().trim();
            if (!esValida(palabra)) {
                mostrarError("La palabra debe contener solo letras (a-z) y no estar vacía.");
                return;
            }
            palabra = palabra.toLowerCase();
            if (modelContainsIgnoreCase(palabra)) {
                mostrarError("La palabra ya existe en la lista.");
                return;
            }
            listModel.addElement(palabra);
            listaPalabras.setSelectedIndex(listModel.getSize() - 1);
            tfPalabra.setText("");
        });
        
        btnBorrar.addActionListener(e -> {
            int indice = listaPalabras.getSelectedIndex();
            if (indice != -1) {
                int resp = JOptionPane.showConfirmDialog(
                        this,
                        "¿Eliminar la palabra seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );
                if (resp == JOptionPane.YES_OPTION) {
                    listModel.remove(indice);
                    if (!listModel.isEmpty()) {
                        listaPalabras.setSelectedIndex(Math.max(0, indice - 1));
                    }
                }
            }
        });

        btnLimpiar.addActionListener(e -> tfPalabra.setText(""));


        btnGuardar.addActionListener(e -> {
            ArrayList<String> nuevaPalabra = new ArrayList<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                String s = listModel.get(i).trim().toLowerCase();
                if (!esValida(s)) {
                    mostrarError("Lista contiene entradas inválidas. Revisa y vuelve a intentar.");
                    return;
                }
                nuevaPalabra.add(s);
            }
            PalabrasFijas.guardarPalabras(nuevaPalabra);
            JOptionPane.showMessageDialog(this, "Palabras guardadas correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (menuAnterior != null) {
                menuAnterior.setVisible(true);
            }
        });

        btnVolver.addActionListener(e -> {
            dispose();
            if (menuAnterior != null) {
                menuAnterior.setVisible(true);
            }
        });

        tfPalabra.addActionListener(e -> btnAgregar.doClick());
    }

    private void cargarListaDesdeAdmin() {
        listModel.clear();
        java.util.List<String> palabras = admin.listaPalabras();
        if (palabras != null) {
            for (String p : palabras) {
                listModel.addElement(p.toLowerCase());
            }
        }
    }

    private boolean esValida(String txt) {
        return txt != null && !txt.isEmpty() && txt.matches("[a-zA-Z]+");
    }

    private boolean modelContainsIgnoreCase(String palabra) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
