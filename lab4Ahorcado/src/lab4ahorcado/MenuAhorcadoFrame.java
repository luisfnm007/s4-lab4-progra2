/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab4ahorcado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author ferna
 */
public class MenuAhorcadoFrame extends JFrame {

    public MenuAhorcadoFrame(){
        super("Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        setContentPane(panel);

        JLabel titulo = new JLabel("AHORCADO", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 60));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnFijo = new JButton("Jugar Palabra Fija");
        JButton btnAzar = new JButton("Jugar Palabra al Azar");
        JButton btnCambiar = new JButton("Cambiar Palabras Fijas");
        JButton btnSalir = new JButton("Salir");

        Dimension buttonSize = new Dimension(250, 40);
        Font buttonFont = (new Font("Georgia", Font.ITALIC, 20));
        
        for (JButton btn : new JButton[]{btnFijo, btnAzar, btnCambiar, btnSalir}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(buttonSize);
            btn.setFont(buttonFont);
            btn.setFocusPainted(false);
        }

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(40));
        panel.add(btnFijo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnAzar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnCambiar);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnSalir);
        
        btnFijo.addActionListener(e -> {
            this.setVisible(false);
            SwingUtilities.invokeLater(() -> new AhorcadoFijoFrame(this).setVisible(true));
        });

        btnAzar.addActionListener(e -> {
            this.setVisible(false);
            JuegoAzarFrame juegoAzar = new JuegoAzarFrame(this);
            juegoAzar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            juegoAzar.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Lab4Ahorcado.this.setVisible(true);
                }
            });
            juegoAzar.setVisible(true);
            
            
            Component[] components = juegoAzar.getContentPane().getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    JPanel southPanel = (JPanel) component;
                    for(Component subComp : southPanel.getComponents()){
                        if(subComp instanceof JButton && "Nueva partida".equals(((JButton)subComp).getText())){
                            ((JButton)subComp).doClick();
                            return;
                        }
                    }
                }
            }
        });

        btnCambiar.addActionListener(e -> {
            this.setVisible(false);
            SwingUtilities.invokeLater(() -> new CambiaPalabrasFrame(this).setVisible(true));
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
         SwingUtilities.invokeLater(() -> new Lab4Ahorcado().setVisible(true));
    }
    
}
