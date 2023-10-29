package cadastro;

public class Pacientes {
	private String nomeCompleto;
	private String data_nascimento; // perdao representar data com string :(
	private String genero;
	private String responsavel;
	private String contato;
	private String endereco;
	
	public Pacientes
	(String nomeCompleto, String data, String genero, String responsavel, String contato, String endereco) {
		this.nomeCompleto = nomeCompleto;
		this.data_nascimento = data;
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

	
	
}


