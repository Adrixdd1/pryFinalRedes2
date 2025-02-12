package connection;

import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

class DatosCliente {
    ObjectOutputStream out;
    InputStreamReader in;

    DatosCliente(Socket cliente) {
        try {
            out = new ObjectOutputStream(cliente.getOutputStream());
            in = new InputStreamReader(cliente.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
