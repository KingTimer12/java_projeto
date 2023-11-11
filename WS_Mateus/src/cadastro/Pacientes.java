package cadastro;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pacientes {
	private String nomeCompleto;
	private Date data_nascimento;
	private Date conclusao_tratamento;
	private String genero;
	private String responsavel;
	private String contato;
	private String endereco;
	private String feedback_medico;
	private String plano_medico;
	private List<Exames> exames = new ArrayList<>();
	private ProgressoPaciente progesso;

	public Pacientes (String nomeCompleto, Date data_nascimento, String genero, String responsavel, String contato, String endereco) {
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


	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}


	public Date getData_nascimento() {
		return data_nascimento;
	}


	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}


	public Date getConclusao_tratamento() {
		return conclusao_tratamento;
	}

	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getResponsavel() {
		return responsavel;
	}


	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}


	public String getContato() {
		return contato;
	}


	public void setContato(String contato) {
		this.contato = contato;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getFeedback_medico() {
		return feedback_medico;
	}

	public String getPlano_medico() {
		return plano_medico;
	}

	public List<Exames> getExames() {
		return exames;
	}

	public ProgressoPaciente getProgesso() {
		return progesso;
	}


	//Adicionando exames a lista de exames do paciente!
	public void adicionarExames(Exames exame) {
		exames.add(exame);
	}

}
