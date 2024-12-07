import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost"; // Endereço do servidor
        int porta = 3000;          // Porta do servidor

        try (Socket socket = new Socket(host, porta);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado ao servidor " + host + ":" + porta);
            String email;

            while (true) {
                System.out.print("Digite um email para validar (ou 'sair' para encerrar): ");
                email = console.readLine();

                if ("sair".equalsIgnoreCase(email)) {
                    break;
                }

                out.writeUTF(email); // Envia o email para o servidor
                out.flush();

                boolean isValid = in.readBoolean(); // Recebe o resultado como booleano
                System.out.println("Email válido? " + isValid);
            }
        } catch (IOException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }
}
