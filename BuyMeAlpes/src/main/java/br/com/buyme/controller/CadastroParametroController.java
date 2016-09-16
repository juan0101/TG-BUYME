package br.com.buyme.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class CadastroParametroController {
	
	private Stage dialogStage;
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML private CheckBox cadastro,editarCadastro,producao,encomenda,venda,relatorio;
	
	public void buscarPermissoes(){
		if(cadastro.isSelected()){
		}
		if(editarCadastro.isSelected()){
		}
		if(producao.isSelected()){
		}
		if(encomenda.isSelected()){
		}
		if(venda.isSelected()){
		}
		if(relatorio.isSelected()){
		}
		
	}
	
	@FXML
	public void finalizar(){
		dialogStage.close();
	}

}
