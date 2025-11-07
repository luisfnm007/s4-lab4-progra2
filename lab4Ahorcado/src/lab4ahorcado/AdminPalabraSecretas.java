package lab4ahorcado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author marye
 */
public class AdminPalabraSecretas {

    private ArrayList<String> palabrasSecreta;
    private Random random;

    public AdminPalabraSecretas() {
        this.palabrasSecreta = PalabrasFijas.leerPalabras();
        this.random = new Random();
    }

    public boolean agregarPalabra(String nuevaPalabra) {
        if (nuevaPalabra == null || nuevaPalabra.trim().isEmpty()) {
            return false;
        }

        nuevaPalabra = nuevaPalabra.toLowerCase().trim();

        if (palabrasSecreta.contains(nuevaPalabra)) {
            return false; 
        }

        palabrasSecreta.add(nuevaPalabra);
        PalabrasFijas.guardarPalabras(palabrasSecreta);
        return true;
    }


    public String obtenerPalabra() {
        if (palabrasSecreta.isEmpty()) {
            return null;
        }
        int index = random.nextInt(palabrasSecreta.size());
        return palabrasSecreta.get(index);
    }


    public List<String> listaPalabras() {
        return Collections.unmodifiableList(palabrasSecreta);
    }


    public boolean existePalabra(String palabra) {
        if (palabra == null) {
            return false;
        }
        return palabrasSecreta.contains(palabra.toLowerCase().trim());
    }
}


