package br.com.buyme.controller;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.entity.Cliente;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TrocarSenhaClienteController {
	
	private Stage dialogStage;
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	private Cliente cliente;
	public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
	
	private Popup popup = new Popup("Trocar senha cliente");
	@FXML private TextField novaSenha,senhaAntiga;
	private ClienteDAO cliDao = new ClienteDAO();
	
	public void salvarNovaSenha(){
		String seAn = senhaAntiga.getText();
		String noSe = novaSenha.getText();
		if(noSe != null && !noSe.isEmpty()){
			String antigaCripto = Utils.senhaSha256(seAn);
			if(cliente.getSenha() == null || antigaCripto.equals(cliente.getSenha())){
				 String noSenha = Utils.senhaSha256(noSe);
				 try{
					 cliDao.salvarLoginSenha(cliente.getId(), cliente.getLogin(), noSenha);
					 popup.getInformation("Login/Senha alterado com sucesso");
					 
					 dialogStage.close();
				 }catch(Exception e){
					 e.printStackTrace();
					 popup.getError("Houve um erro, tente novamente!");
				 }
			}else{
				popup.getError("Senha antiga incorreta!");
			}
		}else{
			popup.getError("Campos obrigatórios em branco!");
		}
	}
	
	public void cancelar(){
		dialogStage.close();
	}
	
}
