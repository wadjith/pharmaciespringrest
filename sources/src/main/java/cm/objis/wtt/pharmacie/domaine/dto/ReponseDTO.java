package cm.objis.wtt.pharmacie.domaine.dto;

public class ReponseDTO {
	
	private int code;
	private String message;
	
	public ReponseDTO() {
		super();
	}
	public ReponseDTO(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
