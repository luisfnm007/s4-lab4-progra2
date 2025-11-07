/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab4ahorcado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Nathan
 */
public abstract class JuegoAhorcadoBase implements JuegoAhorcado{
    public String palabraSecreta;
    public String palabraActual;
    public int Intentos;
    public int Max_Intentos;
    
    public static ArrayList<Character> letraSeleccionadas= new ArrayList<>();
    public static ArrayList<String> letraSeleccionadas= new ArrayList<>();
}
