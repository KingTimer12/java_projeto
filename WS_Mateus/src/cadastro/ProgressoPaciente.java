package cadastro;

public enum ProgressoPaciente {
	ESTAVEL, MELHORANDO, RECUPERADO, EM_TRATAMENTO, AGRAVADO, ALTA, FALECIDO;

	public String obterDescricao() {
		switch (this) {
        	case ESTAVEL:
        		return "Estável";
        	case MELHORANDO:
        		return "Melhorando";
        	case RECUPERADO:
        		return "Recuperado";
        	case EM_TRATAMENTO:
        		return "Em Tratamento";
        	case AGRAVADO:
        		return "Agravado";
        	case ALTA:
        		return "Alta";
        	case FALECIDO:
        		return "Falecido";
        	default:
        		return "Desconhecido";
		}
	}

}
