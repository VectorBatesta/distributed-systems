/**
 * Lab 1: Leitura de Base de Dados N�o-Distribuida
 * 
 * Integrantes do grupo:
 * - Vitor Luis de Queiroz Batista - 2104679
 * - 
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * 
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Principal_v0 {

	public final static Path path = Paths			
			.get("fortune-br.txt");
	private int NUM_FORTUNES = 0;

	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				}// fim while

				System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				int lineCount = 0;

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					System.out.println(fortune.toString());

					System.out.println(lineCount);
				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}


		//#######################################################################################
		//#######################################################################################
		//#######################################################################################
		//#######################################################################################

		public void read(HashMap<Integer, String> hm) throws FileNotFoundException {
			SecureRandom random = new SecureRandom();
			int randomIndex = random.nextInt(hm.size()) + 1; // Seleciona uma fortuna aleatória
			String fortune = hm.get(randomIndex);
			if (fortune != null) {
				System.out.println("Fortuna aleatória:");
				System.out.println(fortune);
			} else {
				System.out.println("Nenhuma fortuna encontrada.");
			}
		}

		public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Digite a nova fortuna:");
			String newFortune = scanner.nextLine();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
				writer.write("%\n"); // Adiciona o separador
				writer.write(newFortune + "\n"); // Adiciona a nova fortuna
				System.out.println("Nova fortuna adicionada com sucesso!");
			} catch (IOException e) {
				System.out.println("SHOW: Exceção ao escrever no arquivo.");
			}
		}
		
		//#######################################################################################
		//#######################################################################################
		//#######################################################################################
		//#######################################################################################
	}


	public void iniciar() {
		FileReader fr = new FileReader();
		HashMap<Integer, String> hm = new HashMap<>();
		Scanner scanner = new Scanner(System.in);

		try {
			NUM_FORTUNES = fr.countFortunes();
			fr.parser(hm);

			System.out.println("Escolha uma opção:");
			System.out.println("1 - Ler uma fortuna aleatória");
			System.out.println("2 - Escrever uma nova fortuna");
			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			if (choice == 1) {
				fr.read(hm);
			} else if (choice == 2) {
				fr.write(hm);
			} else {
				System.out.println("Opção inválida. Encerrando o programa.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}

}
