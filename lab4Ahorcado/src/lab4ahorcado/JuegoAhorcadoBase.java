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
    
    public static ArrayList<Character> letraUsadas= new ArrayList<>();
    public static ArrayList<String> figuraAhorcado= new ArrayList<>();
    
    public JuegoAhorcadoBase(){
        this.Intentos=0;
        this.Max_Intentos=6;
    }
    
    public String actualizarPalabraActual(String palabraSecreta){
        return palabraActual;
    }
    
    public boolean verificarLetra(){
        return true;
    }
    
    public boolean hasGanado(){
        return true; 
    }
}
