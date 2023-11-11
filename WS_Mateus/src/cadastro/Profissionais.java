package cadastro;
import java.util.ArrayList;
import java.util.List;

public class Profissionais {
	private String nome;
	private String feedback;
	private String plano;
	//Lista de pacientes relacionados a esse profissional!
	private List<Pacientes> pacientes = new ArrayList<>();
	
	//Método construtor.
	public Profissionais(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	//Adicionando pacientes a lista de pacientes do profissional!
	public void adicionarPacientes(Pacientes paciente) {
		pacientes.add(paciente);
	}
	
}
