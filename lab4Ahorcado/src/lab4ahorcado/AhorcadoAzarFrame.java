/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;

/**
 *
 * @author ferna
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AhorcadoAzarFrame extends JFrame {

    private static JTextArea txtFigura;
    private static JLabel lblTituloPalabra, lblIntentos, lblLetras, lblMensaje;
    private static JTextArea taPalabra;
    private static JTextField txtEntrada;
    private static JButton btnEnviar, btnRendirse, btnVolver;

    private static BlockingQueue<String> inputQueue;
    private static JFrame menuAnterior;

    public AhorcadoAzarFrame(JFrame menu) {
        super("Ahorcado Aleatorio");
        menuAnterior = menu;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (inputQueue != null) {
                    inputQueue.offer("Salir");
                }
                if (menuAnterior != null) {
                    menuAnterior.setVisible(true);
                }
            }
        });

        vista();

        new Thread(() -> {
            JuegoAhorcadoAzar juego = new JuegoAhorcadoAzar();
            juego.Jugar();
        }, "hilo-juego-azar").start();
    }

    private void vista() {
        setResizable(false);
        setSize(450, 820);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(16, 16));
        panelPrincipal.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(panelPrincipal);

        JLabel lblTitulo = new JLabel("Ahorcado Aleatorio", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        txtFigura = new JTextArea(24, 44);
        txtFigura.setEditable(false);
        txtFigura.setFont(new Font("Monospaced", Font.PLAIN, 22));
        txtFigura.setLineWrap(false);
        txtFigura.setBackground(Color.WHITE);
        txtFigura.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        JScrollPane spMunieco = new JScrollPane(txtFigura);
        spMunieco.setBorder(BorderFactory.createEmptyBorder());
        spMunieco.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentro.add(spMunieco);
        panelCentro.add(Box.createVerticalStrut(10));

        lblTituloPalabra = new JLabel("Palabra:");
        lblTituloPalabra.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentro.add(lblTituloPalabra);

        taPalabra = new JTextArea(2, 40);
        taPalabra.setPreferredSize(new Dimension(taPalabra.getPreferredSize().width, 100));
        taPalabra.setFont(new Font("Monospaced", Font.BOLD, 30));
        taPalabra.setEditable(false);
        taPalabra.setLineWrap(false);
        taPalabra.setBackground(new Color(248, 248, 248));
        taPalabra.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scPalabra = new JScrollPane(taPalabra,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentro.add(scPalabra);
        panelCentro.add(Box.createVerticalStrut(10));

        lblIntentos = new JLabel("Intentos: 0 / 6");
        lblIntentos.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIntentos.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLetras = new JLabel("Letras usadas: —");
        lblLetras.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLetras.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblMensaje.setForeground(new Color(50, 100, 185));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(lblIntentos);
        panelCentro.add(Box.createVerticalStrut(4));
        panelCentro.add(lblLetras);
        panelCentro.add(Box.createVerticalStrut(4));
        panelCentro.add(lblMensaje);
        panelCentro.add(Box.createVerticalStrut(8));

        JPanel panelJuego = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel lblLetra = new JLabel("Letra: ");
        txtEntrada = new JTextField(20);
        txtEntrada.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ((AbstractDocument) txtEntrada.getDocument()).setDocumentFilter(new SingleLetterFilter(lblMensaje));
        btnEnviar = new JButton("Intentar");
        btnRendirse = new JButton("Rendirse");
        panelJuego.add(lblLetra);
        panelJuego.add(txtEntrada);
        panelJuego.add(btnEnviar);
        panelJuego.add(btnRendirse);
        panelCentro.add(panelJuego);

        // Botón volver abajo del todo
        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        btnVolver = new JButton("Volver al Menú");
        panelAbajo.add(btnVolver);
        panelPrincipal.add(panelAbajo, BorderLayout.SOUTH);

        // --- Acciones idénticas a antes ---
        btnEnviar.addActionListener(e -> {
            if (inputQueue == null) {
                return;
            }
            String t = txtEntrada.getText().trim();
            if (t.length() != 1 || !Character.isLetter(t.charAt(0))) {
                lblMensaje.setText("Ingresa solo una letra.");
                txtEntrada.requestFocusInWindow();
                txtEntrada.selectAll();
                return;
            }
            inputQueue.offer(t);
            txtEntrada.setText("");
            txtEntrada.requestFocusInWindow();
        });
        txtEntrada.addActionListener(e -> btnEnviar.doClick());

        btnRendirse.addActionListener(e -> {
            if (inputQueue != null) {
                inputQueue.offer("Salir");
            }
        });

        btnVolver.addActionListener(e -> {
            if (inputQueue != null) {
                inputQueue.offer("Salir");
            }
            dispose();
            if (menuAnterior != null) {
                menuAnterior.setVisible(true);
            }
        });
    }

    public static void bindInputQueue(BlockingQueue<String> q) {
        inputQueue = q;
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
            taPalabra.setCaretPosition(0);
            lblIntentos.setText("Intentos: " + intentos + " / " + limite);
            lblLetras.setText("Letras usadas: " + (usadas.isEmpty() ? "—" : usadas.toString()));
            lblMensaje.setText(mensaje == null ? " " : mensaje);
        });
    }

    public static void juegoTerminado(String Mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtEntrada.setEnabled(false);
            btnEnviar.setEnabled(false);
            btnRendirse.setEnabled(false);
        });
    }

    private static class SingleLetterFilter extends DocumentFilter {

        private final JLabel lblEstado;
        private static final Pattern PATRON_PRIMERA_LETRA = Pattern.compile("\\p{L}");

        SingleLetterFilter(JLabel feedbackLabel) {
            this.lblEstado = feedbackLabel;
        }

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) {
                return;
            }
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String one = tomarPrimeraLetra(string);
            if (one == null) {
                mostrarAlerta("Ingresa solo una letra.");
                return;
            }
            fb.replace(0, current.length(), one, attr);
            limpiarAlerta();
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            if (text == null) {
                text = "";
            }
            String one = tomarPrimeraLetra(text);
            if (one == null) {
                if (text.isEmpty()) {
                    fb.replace(offset, length, text, attrs);
                    return;
                }
                mostrarAlerta("Ingresa solo una letra.");
                return;
            }
            fb.replace(0, current.length(), one, attrs);
            limpiarAlerta();
        }

        @Override
        public void remove(FilterBypass paso, int posicion, int cantidad) throws BadLocationException {
            paso.remove(posicion, cantidad);
        }

        private String tomarPrimeraLetra(String texto) {
            if (texto == null || texto.isEmpty()) {
                return null;
            }
            Matcher buscador = PATRON_PRIMERA_LETRA.matcher(texto);
            return buscador.find() ? buscador.group() : null;
        }

        private void mostrarAlerta(String mensaje) {
            JOptionPane.showMessageDialog(null, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            if (lblEstado != null) {
                lblEstado.setText(mensaje);
            }
        }

        private void limpiarAlerta() {
            if (lblEstado != null) {
                lblEstado.setText(" ");
            }
        }
    }
}
