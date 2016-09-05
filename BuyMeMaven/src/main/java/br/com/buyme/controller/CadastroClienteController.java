package br.com.buyme.controller;

import java.io.IOException;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroClienteController {

	@FXML private TextField nome,telefone,endereco,end_numero,cidade,email,login,senha;
	private ClienteDAO cliDao = new ClienteDAO();
	
	@FXML
	public void cadastrarCliente(){
		try{
			String nomeUsu = nome.getText();
			String telUsu = telefone.getText();
			String endUsu = endereco.getText();
			String numUsu = end_numero.getText();
			String cidUsu = cidade.getText();
			String eUsu = email.getText();
			String loginUsu = login.getText();
			String senhaUsu = senha.getText();
			if(nomeUsu != null && !nomeUsu.isEmpty() && telUsu != null && !telUsu.isEmpty() && endUsu != null && !endUsu.isEmpty() && 
					numUsu != null && !numUsu.isEmpty() && cidUsu != null && !cidUsu.isEmpty() && eUsu != null && !eUsu.isEmpty()){
				if(Utils.isNumber(numUsu)){
					try{
						cliDao.salvar(nomeUsu, telUsu, endUsu, Integer.parseInt(numUsu), cidUsu, eUsu, loginUsu, senhaUsu);
						Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Cliente");
				        dialogoInfo.setContentText("Cliente salvo com sucesso!");
				        dialogoInfo.showAndWait();
				        limparCampos();
					}catch(Exception e){
						e.printStackTrace();
						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Cliente");
				        dialogoInfo.setContentText("Houve um erro ao salvar o cliente, tente novamente!");
				        dialogoInfo.showAndWait();
					}
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Cliente");
			        dialogoInfo.setContentText("O campo número do endereço deve ser um numeral inteiro!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Cliente");
		        dialogoInfo.setContentText("Existem campos obrigatórios nulos!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Cliente");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	
	public void limparCampos(){
		nome.setText("");
		telefone.setText("");
		endereco.setText("");
		end_numero.setText("");
		cidade.setText("");
		email.setText("");
		login.setText("");
		senha.setText("");
	}
	
	@FXML
	public void voltarMenu(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AdminView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
}
