package br.com.buyme.popup;


import javafx.scene.control.Alert;
import lombok.Data;

@Data
public class Popup {
	
	private String titulo;
	
	public Popup(String titulo){
		this.titulo = titulo;
	}
	
	public void getError(String mensagem){
		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		dialogoInfo.setTitle("Alpes Chocolates");
		dialogoInfo.setHeaderText(titulo);
		dialogoInfo.setContentText(mensagem);
		dialogoInfo.showAndWait();
	}
	
	public void getInformation(String mensagem){
		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
		dialogoInfo.setTitle("Alpes Chocolates");
		dialogoInfo.setHeaderText(titulo);
		dialogoInfo.setContentText(mensagem);
		dialogoInfo.showAndWait();
	}
	
	public void getWarning(String mensagem){
		Alert dialogoInfo = new Alert(Alert.AlertType.WARNING);
		dialogoInfo.setTitle("Alpes Chocolates");
		dialogoInfo.setHeaderText(titulo);
		dialogoInfo.setContentText(mensagem);
		dialogoInfo.showAndWait();
	}
	

}
