/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ferna
 */
public class PalabrasFijas {

    private static final List<String> BASE = List.of(
        "hola", "celular", "baloncesto", "tablero", "ahorcado",
        "silla", "mesa", "internet", "arriba", "abajo"
    );

    private static List<String> palabras = new ArrayList<>(BASE);

    public static ArrayList<String> leerPalabras() {
        return new ArrayList<>(palabras);
    }

    public static void guardarPalabras(List<String> nuevas) {
        if (nuevas != null) {
            palabras = new ArrayList<>(nuevas);
        }
    }
}
