package cadastro;
import java.util.Date;

public class Exames {
	private String tipoExame;
	private String diagnostico;
	private Date dataExame;
	
	public String getTipoExame() {
		return tipoExame;
	}
	public void setTipoExame(String tipoExame) {
		this.tipoExame = tipoExame;
	}
	public String getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	public Date getDataExame() {
		return dataExame;
	}
	
	//Setter
	public void setDataExame(Date dataExame) {
		this.dataExame = dataExame;
	}
	
	
}
