/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author marye
 */
public class JuegoAhorcadoAzar extends JuegoAhorcadoBase {

    private String ultimaEntrada = null;
    private String ultimoMensaje = " ";

    public JuegoAhorcadoAzar() {
        super();
    }

    public ArrayList<String> getPalabrasSecretas() {
        return PalabrasFijas.leerPalabras();
    }

    public String inicializarPalabraSecreta() {
        ArrayList<String> posibles = getPalabrasSecretas();
        if (posibles.isEmpty()) {
            this.palabraSecreta = "error";
        } else {
            Collections.shuffle(posibles);
            this.palabraSecreta = posibles.get(0);
        }
        letraUsadas.clear();
        this.Intentos = 0;
        this.Max_Intentos = 6;
        cargarFigurasAhorcado();
        this.palabraActual = actualizarPalabraActual(this.palabraSecreta);
        return this.palabraSecreta;
    }

    public String actualizarPalabraActual(String palabraSecreta) {
        if (palabraSecreta == null) {
            return null;
        }
        StringBuilder resultado = new StringBuilder();
        for (char letra : palabraSecreta.toCharArray()) {
            if (letra == ' ' || letra == '-') {
                resultado.append(letra);
                continue;
            }
            char lower = Character.toLowerCase(letra);
            boolean usada = false;
            for (Character lc : letraUsadas) {
                if (lc != null && Character.toLowerCase(lc) == lower) {
                    usada = true;
                    break;
                }
            }
            if (usada) {
                resultado.append(letra);
            } else {
                resultado.append('_');
            }
        }
        this.palabraActual = resultado.toString();
        return this.palabraActual;
    }
    
    public boolean verificarLetra() {
        if (ultimaEntrada == null || ultimaEntrada.trim().isEmpty()) {
            ultimoMensaje = "Entrada vacía";
            return false;
        }
        
        String entrada = ultimaEntrada.trim();
        ultimaEntrada = null;

        if (entrada.length() > 1) {
            if (entrada.equalsIgnoreCase(palabraSecreta)) {
                palabraActual = palabraSecreta;
                ultimoMensaje = "¡Adivinaste la palabra!";
                return true;
            } else {
                this.Intentos++;
                ultimoMensaje = "No es la palabra";
                return false;
            }
        }

        char c = Character.toLowerCase(entrada.charAt(0));
        if (!Character.isLetter(c)) {
            ultimoMensaje = "Introduce una letra válida";
            return false;
        }

        for (Character lu : letraUsadas) {
            if (lu != null && Character.toLowerCase(lu) == c) {
                ultimoMensaje = "Ya usaste esa letra";
                return false;
            }
        }

        letraUsadas.add(c);
        boolean acierto = palabraSecreta.toLowerCase().indexOf(c) >= 0;
        
        if (acierto) {
            ultimoMensaje = "¡Acierto!";
        } else {
            this.Intentos++;
            ultimoMensaje = "La letra '" + c + "' no está";
        }
        
        actualizarPalabraActual(palabraSecreta);
        return acierto;
    }
    
    public boolean hasGanado() {
        return palabraSecreta != null && palabraSecreta.equals(palabraActual);
    }
    
     private void cargarFigurasAhorcado() {
        if (!figuraAhorcado.isEmpty()) {
            return;
        }

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |               ",
            "  |               ",
            "  |               ",
            "  |               ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |               ",
            "  |               ",
            "  |               ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |      |        ",
            "  |      |        ",
            "  |               ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |     /|        ",
            "  |      |        ",
            "  |               ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |     /|\\       ",
            "  |      |        ",
            "  |               ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |     /|\\       ",
            "  |      |        ",
            "  |     /         ",
            "  |               ",
            "=========="
        }));

        figuraAhorcado.add(String.join("\n", new String[]{
            "  +------+        ",
            "  |      |        ",
            "  |      O        ",
            "  |     /|\\       ",
            "  |      |        ",
            "  |     / \\       ",
            "  |               ",
            "=========="
        }));
    }

    @Override
    public void Jugar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
