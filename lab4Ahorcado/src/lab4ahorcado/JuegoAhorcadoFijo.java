/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author ferna
 */
public class JuegoAhorcadoFijo extends JuegoAhorcadoBase {

    private String ultimaEntrada;
    private String ultimoMensaje;
    private final int indiceElegido;

    public JuegoAhorcadoFijo(int indicePalabra) {
        super();
        this.indiceElegido = indicePalabra;
    }

    public ArrayList<String> getPalabrasSecretas() {
        return PalabrasFijas.leerPalabras();
    }

    @Override
    public String inicializarPalabraSecreta() {
        ArrayList<String> lista = getPalabrasSecretas();
        String seleccionada = "rango";

        if (indiceElegido >= 0 && indiceElegido < lista.size()) {
            seleccionada = lista.get(indiceElegido);
        }

        palabraSecreta = seleccionada;
        letraUsadas.clear();

        Intentos = 0;
        Max_Intentos = 6;

        cargarFigurasAhorcado();
        palabraActual = actualizarPalabraActual(palabraSecreta);

        return palabraSecreta;
    }

    @Override
    public String actualizarPalabraActual(String palabraSecreta) {
        StringBuilder resultado = new StringBuilder();
        char[] letras = palabraSecreta.toCharArray();

        for (char letra : letras) {
            char letraMin = Character.toLowerCase(letra);

            if (letra == ' ' || letra == '-') {
                resultado.append(letra);
                continue;
            }

            if (letraUsadas.contains(letraMin)) {
                resultado.append(letra);
            } else {
                resultado.append('_');
            }
        }

        return resultado.toString();
    }

    @Override
    public boolean verificarLetra() {
        String entrada = null;

        if (ultimaEntrada != null) {
            entrada = ultimaEntrada.trim();
        }

        ultimaEntrada = null;

        if (entrada == null || entrada.isEmpty()) {
            return false;
        }

        if (entrada.length() != 1) {
            ultimoMensaje = "Ingresa solo una letra.";
            return false;
        }

        char letra = Character.toLowerCase(entrada.charAt(0));

        if (!Character.isLetter(letra)) {
            ultimoMensaje = "Introduce un caracter válido.";
            return false;
        }

        if (letraUsadas.contains(letra)) {
            ultimoMensaje = "Letra no disponible.";
            return false;
        }

        letraUsadas.add(letra);
        boolean acierto = palabraSecreta.toLowerCase().indexOf(letra) >= 0;

        if (acierto) {
            ultimoMensaje = "¡Correcto!";
        } else {
            Intentos++;
            ultimoMensaje = "La letra '" + letra + "' no forma parte de la palabra.";
        }

        palabraActual = actualizarPalabraActual(palabraSecreta);
        return acierto;
    }

    @Override
    public boolean hasGanado() {
        return palabraSecreta != null && palabraSecreta.equals(palabraActual);
    }

    @Override
    public void Jugar() {
        inicializarPalabraSecreta();

        BlockingQueue<String> entradas = new LinkedBlockingQueue<>();
        AhorcadoFijoFrame.bindInputQueue(entradas);

        AhorcadoFijoFrame.updateView(
                figuraAhorcado.get(0),
                formatearConEspacios(palabraActual),
                Intentos, Max_Intentos, letraUsadas,
                "Ingresa solo una letra (A–Z)"
        );

        boolean salir = false;

        while (!hasGanado() && Intentos < Max_Intentos && !salir) {
            try {
                String entrada = entradas.take();

                if ("__SALIR__".equals(entrada)) {
                    salir = true;
                    break;
                }

                ultimaEntrada = entrada;
                verificarLetra();

                int paso = Math.min(Intentos, figuraAhorcado.size() - 1);
                AhorcadoFijoFrame.updateView(
                        figuraAhorcado.get(paso),
                        formatearConEspacios(palabraActual),
                        Intentos, Max_Intentos, letraUsadas,
                        ultimoMensaje
                );

            } catch (InterruptedException e) {

            }
        }

        int paso = Math.min(Intentos, figuraAhorcado.size() - 1);
        String msjFinal;

        if (hasGanado()) {
            msjFinal = "¡Ganaste, felicidades!";
        } else {
            msjFinal = "Mala suerte, perdiste. La palabra era: " + palabraSecreta;
        }

        AhorcadoFijoFrame.updateView(figuraAhorcado.get(paso),
                formatearConEspacios(palabraActual),
                Intentos, Max_Intentos, letraUsadas,
                msjFinal
        );

        AhorcadoFijoFrame.juegoTerminado();
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

    private String formatearConEspacios(String s) {
        return (s == null) ? "" : s.replace("", " ").trim();
    }
}
