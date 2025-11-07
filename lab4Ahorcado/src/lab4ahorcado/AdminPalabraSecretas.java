package lab4ahorcado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author marye
 */
public class AdminPalabraSecretas {

    ArrayList<String> PalabrasDisponibles;

    List<String> PalabrasFijas = Arrays.asList(
            "Avion", "Celeste", "Camerino", "Hipopotamo", "Lechuga",
            "Moneda", "Leopardo", "Honduras", "Programacion", "Laboratorio"
    );

    public AdminPalabraSecretas() {
        PalabrasDisponibles = new ArrayList(PalabrasFijas);
    }

    public void agregarPalabras(String nuevaPalabra) {
        if (nuevaPalabra == null) {
            return;
        }
        
        Boolean validacionPalabra = validacionPalabras(nuevaPalabra);
        if(!validacionPalabra){
            return;
        }
        
        PalabrasDisponibles.add(nuevaPalabra);
    }

    public String obtenerPalabras() {
        Random rand = new Random();

        int indice = rand.nextInt(PalabrasDisponibles.size());
        String PalabraAleatoria = null;

        int i = 0;
        for (String palabra : PalabrasDisponibles) {
            if (i == indice) {
                PalabraAleatoria = palabra;
                break;
            }
            i++;
        }

        return PalabraAleatoria;
    }

    public boolean validacionPalabras(String palabra) {
        for (String pd : PalabrasDisponibles) {
            if (pd.equalsIgnoreCase(palabra)) {
                return false;
            }
        }
        return true;
    }
}
