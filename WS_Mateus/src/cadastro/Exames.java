package cadastro;
import java.util.Date;

public class Exames {
	private String tipoExame;
	private String resultado;
	private Date dataExame;
	
	public String getTipoExame() {
		return tipoExame;
	}
	public void setTipoExame(String tipoExame) {
		this.tipoExame = tipoExame;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public Date getDataExame() {
		return dataExame;
	}
	
	//Setter
	public void setDataExame(Date dataExame) {
		this.dataExame = dataExame;
	}
	
	
}
