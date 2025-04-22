/**
 * Laboratorio 2 de Sistemas Distribuidos
 * 
 * Integrantes do grupo:
 * - Vitor Luis de Queiroz Batista - 2104679
 * - Guilherme de Almeida do Carmo - 2207184
 * 
 */

import java.io.*;
import java.net.Socket;

public class Cliente {

    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private int porta = 1025;

    public void iniciar() {
        System.out.println("Cliente iniciado na porta: " + porta);

        try {
            socket = new Socket("127.0.0.1", porta);
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Ler uma fortuna (read)");
                System.out.println("2. Escrever uma fortuna (write)");
                System.out.println("3. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = Integer.parseInt(br.readLine());

                if (opcao == 1) {
                    read();
                } else if (opcao == 2) {
                    System.out.print("Digite a fortuna para enviar: ");
                    String fortuna = br.readLine();
                    write(fortuna);
                } else if (opcao == 3) {
                    System.out.println("Encerrando cliente...");
                    break;
                } else {
                    System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
                }
            }
        }
    }

    private void read() throws IOException {
        saida.writeUTF("read"); // Sends the "read" command
        System.out.println("Enviando comando ao servidor: read");
        String response = entrada.readUTF(); // Waits for the server's response
        System.out.println("Resposta recebida do servidor: " + response);
        System.out.println("Fortuna recebida: " + response);

        // Close the program after the first read
        System.out.println("Encerrando cliente após a leitura.");
        System.exit(0); // Terminates the program
    }

    private void write(String fortuna) throws IOException {
        saida.writeUTF("write:" + fortuna); // Sends the "write" command with the fortune
        System.out.println("Enviando fortuna ao servidor: " + fortuna);
        String response = entrada.readUTF(); // Waits for the server's response
        System.out.println("Resposta do servidor: " + response);

        // Close the program after the write operation
        System.out.println("Encerrando cliente após escrever a fortuna.");
        System.exit(0); // Terminates the program
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.iniciar();
    }
}
