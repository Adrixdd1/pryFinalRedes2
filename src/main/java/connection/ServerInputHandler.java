package connection;
import java.util.Scanner;

class ServerInputHandler{} /*extends Thread {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("start")) {
                System.out.println("El juego ha comenzado. Deteniendo el broadcast...");
                Server.stopBroadcast();
                Server.startGame();
            }
            Server.broadcast("Servidor: " + message, null);
        }
    }
}
*/