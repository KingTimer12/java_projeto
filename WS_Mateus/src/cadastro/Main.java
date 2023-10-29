package cadastro;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		//Instanciando um paciente
		Scanner sc = new Scanner(System.in);
		System.out.print("Informe seu nome completo: ");
		String nomePaciente = sc.nextLine();
		System.out.print("Informe sua data de nascimento: ");
		String data = sc.next();
		System.out.print("Informe o gênero: ");
		String genero = sc.next();
		System.out.print("Informe o seu responsável: ");
		String responsavel = sc.next();
		System.out.print("Informe seu número para contato: ");
		String contato = sc.next();
		System.out.print("Informe o seu endereço: ");
		sc.nextLine();
		String endereco = sc.nextLine();
		Pacientes paciente1 = new Pacientes(nomePaciente, data, genero, responsavel, contato, endereco);
		
		//Instanciando um Médico
		System.out.print("Digite o seu nome doutor: ");
		String nomeDoutor = sc.next();
		Profissionais doutor1 = new Profissionais(nomeDoutor);
		
		//Cadastrando o paciente criado a ficha do doutor
		doutor1.adicionarPacientes(paciente1);
		System.out.println("Paciente adicionado a lista do doutor com sucesso!");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		sc.close();
	}
}
