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
            String email, senha, opcao;

            while (true) {
                System.out.print("Digite 'registrar' para registrar ou 'login' para fazer login (ou 'sair' para encerrar): ");
                opcao = console.readLine();

                if ("sair".equalsIgnoreCase(opcao)) {
                    break;
                }

                System.out.print("Digite seu email: ");
                email = console.readLine();
                System.out.print("Digite sua senha: ");
                senha = console.readLine();

                out.writeUTF(opcao); // Envia a opção para o servidor
                out.writeUTF(email); // Envia o email para o servidor
                out.writeUTF(senha); // Envia a senha para o servidor
                out.flush();

                boolean sucesso = in.readBoolean(); // Recebe o resultado como booleano
                System.out.println("Operação bem-sucedida? " + sucesso);
            }
        } catch (IOException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }
}
