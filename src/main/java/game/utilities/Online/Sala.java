package game.utilities.Online;

import java.net.InetAddress;

public class Sala {
    private String nombre;
    private InetAddress direccionIP;

    public Sala(String nombre, InetAddress direccionIP) {
        this.nombre = nombre;
        this.direccionIP = direccionIP;
    }

    public String getNombre() {
        return nombre;
    }

    public InetAddress getDireccionIP() {
        return direccionIP;
    }

    @Override
    public String toString() {
        return "Sala: " + nombre + " (" + direccionIP.getHostAddress() + ")";
    }
}
