package cadastro;
import java.util.ArrayList;
import java.util.List;

public class Pacientes {
	private String nomeCompleto;
	private String data_nascimento; // perdao representar data com string :(
	private String genero;
	private String responsavel;
	private String contato;
	private String endereco;
	private List<Exames> exames = new ArrayList<>();
	
	public Pacientes (String nomeCompleto, String data_nascimento, String genero, String responsavel, String contato, String endereco) {
		this.nomeCompleto = nomeCompleto;
		this.data_nascimento = data_nascimento;
		this.genero = genero;
		this.responsavel = responsavel;
		this.contato = contato;
		this.endereco = endereco;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getData() {
		return data_nascimento;
	}

	public String getGenero() {
		return genero;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public String getContato() {
		return contato;
	}

	public String getEndereco() {
		return endereco;
	}
	
	//Adicionando exames a lista de exames do paciente!
	public void adicionarExames(Exames exame) {
		exames.add(exame);
	}

}
