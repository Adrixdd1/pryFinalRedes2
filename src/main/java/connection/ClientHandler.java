package connection;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;

class ClientHandler {/*extends Thread {
    private Socket clientSocket;
    private Set<PrintWriter> clients;

    public ClientHandler(Socket socket, Set<PrintWriter> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Añade el canal de comunicación del cliente al conjunto
            clients.add(out);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);
                // Opcional: podrías reenviar el mensaje a otros clientes o procesarlo según la lógica del juego.
            }//Server.broadcast(message, out);
            }
        }catch (SocketException se){
            System.out.println("el cliente se marchó");
        } catch (IOException e ) {
            e.printStackTrace();

        } finally {
            // Elimina el cliente del conjunto al desconectarse
            // Nota: Asegúrate de eliminar el canal del cliente; aquí se asume que out ya no se usará.
        }
    }*/
}
