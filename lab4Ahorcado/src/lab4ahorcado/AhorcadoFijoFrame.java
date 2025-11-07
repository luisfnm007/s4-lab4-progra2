/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Nathan
 */
public class AhorcadoFijoFrame extends JFrame {

    private static JTextArea txtFigura;
    private static JLabel lblTituloPalabra, lblIntentos, lblLetras, lblMensaje;
    private static JTextArea taPalabra;
    private static JTextField txtEntrada;
    private static JButton btnEnviar, btnRendirse, btnVolver;
    private static JPanel panelJuego;

    private static BlockingQueue<String> entrada;
    private static JFrame menuAnterior;

    public AhorcadoFijoFrame(JFrame menu) {
        super("Ahorcado Palabra Fija");
        menuAnterior = menu;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (entrada != null) {
                    entrada.offer("SALIR");
                }
                menuAnterior.setVisible(true);
            }
        });

        ArrayList<String> palabras = PalabrasFijas.leerPalabras();
        JComboBox<String> comboPalabras = new JComboBox<>(palabras.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(null, comboPalabras, "Elige una palabra para jugar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int indiceSeleccionado = comboPalabras.getSelectedIndex();
            construirGUI();
            new Thread(() -> {
                var juego = new JuegoAhorcadoFijo(indiceSeleccionado);
                juego.Jugar();
            }, "hilo-juego-fijo").start();
        } else {
            dispose();
            menuAnterior.setVisible(true);
        }
    }

    private void construirGUI() {
        setSize(800, 700);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        var root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        txtFigura = new JTextArea(15, 50);
        txtFigura.setEditable(false);
        txtFigura.setFont(new Font("Monospaced", Font.PLAIN, 20));
        txtFigura.setBackground(Color.WHITE);
        txtFigura.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        txtFigura.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtFigura.setText("Figura Ahorcado");
        root.add(txtFigura);
        root.add(Box.createVerticalStrut(20));

        taPalabra = new JTextArea(2, 20);
        taPalabra.setFont(new Font("Monospaced", Font.BOLD, 28));
        taPalabra.setEditable(false);
        taPalabra.setBackground(Color.WHITE);
        taPalabra.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        taPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);
        taPalabra.setText("Palabra a Buscar");
        root.add(taPalabra);
        root.add(Box.createVerticalStrut(20));

        var panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(panelInferior);

        lblIntentos = new JLabel("Intentos: 0 / 6");
        lblIntentos.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIntentos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInferior.add(lblIntentos);

        lblLetras = new JLabel("Letras usadas: —");
        lblLetras.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblLetras.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInferior.add(lblLetras);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblMensaje.setForeground(new Color(50, 100, 185));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInferior.add(lblMensaje);

        panelJuego = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        var lbl = new JLabel("Letra:");
        txtEntrada = new JTextField(5);
        txtEntrada.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ((AbstractDocument) txtEntrada.getDocument()).setDocumentFilter(new SingleLetterFilter(lblMensaje));

        btnEnviar = new JButton("Probar");
        btnRendirse = new JButton("Rendirse");
        panelJuego.add(lbl);
        panelJuego.add(txtEntrada);
        panelJuego.add(btnEnviar);
        panelJuego.add(btnRendirse);
        panelInferior.add(panelJuego);

        var abajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        btnVolver = new JButton("Volver al Menu");
        abajo.add(btnVolver);
        root.add(abajo);

        btnEnviar.addActionListener(e -> {
            if (entrada == null) return;
            var t = txtEntrada.getText().trim();
            if (t.length() != 1 || !Character.isLetter(t.charAt(0))) {
                lblMensaje.setText("Ingresa solo UNA letra (A–Z).");
                txtEntrada.requestFocusInWindow();
                txtEntrada.selectAll();
                return;
            }
            entrada.offer(t);
            txtEntrada.setText("");
            txtEntrada.requestFocusInWindow();
        });
        txtEntrada.addActionListener(e -> btnEnviar.doClick());

        btnRendirse.addActionListener(e -> {
            if (entrada != null) entrada.offer("SALIR");
        });

        btnVolver.addActionListener(e -> {
            if (entrada != null) entrada.offer("SALIR");
            dispose();
            menuAnterior.setVisible(true);
        });
    }

    public static void bindInputQueue(BlockingQueue<String> q) {
        entrada = q;
        SwingUtilities.invokeLater(() -> {
            txtEntrada.setText("");
            txtEntrada.requestFocusInWindow();
        });
    }

    public static void actualizarInfo(String figura, String palabraBonita, int intentos, int limite,
            List<Character> usadas, String mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtFigura.setText(figura);
            taPalabra.setText(palabraBonita);
            lblIntentos.setText("Intentos: " + intentos + " / " + limite);
            lblLetras.setText("Letras usadas: " + (usadas.isEmpty() ? "—" : usadas.toString()));
            lblMensaje.setText(mensaje == null ? " " : mensaje);
        });
    }

    public static void juegoTerminado() {
        SwingUtilities.invokeLater(() -> {
            txtEntrada.setEnabled(false);
            btnEnviar.setEnabled(false);
            btnRendirse.setEnabled(false);
        });
    }

    private static class SingleLetterFilter extends DocumentFilter {
        private final JLabel feedbackLabel;
        SingleLetterFilter(JLabel feedbackLabel) { this.feedbackLabel = feedbackLabel; }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            String one = seleccionarPrimeraLetra(string);
            if (one == null) { mostrarAlerta("Ingresa solo UNA letra (A–Z)."); return; }
            fb.replace(0, fb.getDocument().getLength(), one, attr);
            LimpiarMensaje();
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) text = "";
            String one = seleccionarPrimeraLetra(text);
            if (one == null) {
                if (text.isEmpty()) { fb.replace(offset, length, text, attrs); return; }
                mostrarAlerta("Ingresa solo UNA letra (A–Z).");
                return;
            }
            fb.replace(0, fb.getDocument().getLength(), one, attrs);
            LimpiarMensaje();
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            fb.remove(offset, length);
        }

        private String seleccionarPrimeraLetra(String s) {
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (Character.isLetter(ch)) return String.valueOf(ch);
            }
            return null;
        }

        private void mostrarAlerta(String mensaje) {
            JOptionPane.showMessageDialog(null, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            if (feedbackLabel != null) feedbackLabel.setText(mensaje);
        }

        private void LimpiarMensaje() {
            if (feedbackLabel != null) feedbackLabel.setText(" ");
        }
    }
}