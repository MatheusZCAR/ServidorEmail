import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Servidor {
    private static Map<String, String> usuarios = new HashMap<>(); // Armazena email e senha

    public static void main(String[] args) {
        int porta = 3000; // Porta do servidor

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Criando uma nova thread para lidar com o cliente
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ) {
                String opcao, email, senha;
                while (true) {
                    opcao = in.readUTF(); // Recebe a opção do cliente
                    email = in.readUTF(); // Recebe o email do cliente
                    senha = in.readUTF(); // Recebe a senha do cliente

                    boolean sucesso = false;
                    if ("registrar".equalsIgnoreCase(opcao)) {
                        sucesso = registrarUsuario(email, senha);
                    } else if ("login".equalsIgnoreCase(opcao)) {
                        sucesso = loginUsuario(email, senha);
                    }

                    out.writeBoolean(sucesso); // Envia o resultado como booleano
                    out.flush();
                }
            } catch (EOFException e) {
                System.out.println("Cliente desconectado.");
            } catch (IOException e) {
                System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
                }
            }
        }

        private boolean registrarUsuario(String email, String senha) {
            if (isValidEmail(email) && !usuarios.containsKey(email)) {
                usuarios.put(email, senha);
                return true;
            }
            return false;
        }

        private boolean loginUsuario(String email, String senha) {
            return usuarios.containsKey(email) && usuarios.get(email).equals(senha);
        }

        private boolean isValidEmail(String email) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            Pattern pattern = Pattern.compile(emailRegex);
            return pattern.matcher(email).matches();
        }
    }
}
