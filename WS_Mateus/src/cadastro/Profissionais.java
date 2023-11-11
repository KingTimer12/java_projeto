package cadastro;
import java.util.ArrayList;
import java.util.List;

public class Profissionais {
	private String nome;
	private String especialidade_medica;
	//Lista de pacientes relacionados a esse profissional!
	private List<Pacientes> pacientes = new ArrayList<>();
	
	//Método construtor.
	public Profissionais(String nome) {
		this.nome = nome;
	} 

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEspecialidade_medica() {
		return especialidade_medica;
	}

	public void setEspecialidade_medica(String especialidade_medica) {
		this.especialidade_medica = especialidade_medica;
	}

	public List<Pacientes> getPacientes() {
		return pacientes;
	}

	//Adicionando pacientes a lista de pacientes do profissional!
	public void adicionarPacientes(Pacientes paciente) {
		pacientes.add(paciente);
	}
	
}
