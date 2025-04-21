import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    private static Socket socket;
    private static ServerSocket server;

    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private int porta = 1025;
    private List<String> fortunes;

    public Servidor() {
        fortunes = carregarFortunas("fortune-br.txt");
    }

    public void iniciar() {
        System.out.println("Servidor iniciado na porta: " + porta);
        try {
            server = new ServerSocket(porta);
            while (true) {
                try {
                    socket = server.accept();
                    entrada = new DataInputStream(socket.getInputStream());
                    saida = new DataOutputStream(socket.getOutputStream());

                    String mensagem = entrada.readUTF();
                    System.out.println("Mensagem recebida do cliente: " + mensagem);

                    String resposta = parser(mensagem);
                    saida.writeUTF(resposta);
                    System.out.println("Resposta enviada ao cliente: " + resposta);
                } catch (Exception e) {
                    System.err.println("Erro ao processar a conexão do cliente: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Erro ao fechar o socket: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String parser(String mensagem) {
        try {
            if (mensagem.equals("read")) {
                return getFortunaAleatoria(); // Returns a random fortune
            } else if (mensagem.startsWith("write:")) {
                String novaFortuna = mensagem.substring(6); // Extracts the fortune
                salvarFortuna(novaFortuna);
                return "Fortuna salva com sucesso!";
            } else {
                return "Comando inválido!";
            }
        } catch (Exception e) {
            return "Erro no processamento da mensagem!";
        }
    }

    private List<String> carregarFortunas(String arquivo) {
        List<String> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            StringBuilder fortuna = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.equals("%")) {
                    lista.add(fortuna.toString().trim());
                    fortuna.setLength(0);
                } else {
                    fortuna.append(linha).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private String getFortunaAleatoria() {
        Random random = new Random();
        return fortunes.get(random.nextInt(fortunes.size()));
    }

    private void salvarFortuna(String fortuna) {
        fortunes.add(fortuna);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("fortune-br.txt", true))) {
            bw.write("\n" + fortuna + "\n%");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Servidor().iniciar();
    }
}
