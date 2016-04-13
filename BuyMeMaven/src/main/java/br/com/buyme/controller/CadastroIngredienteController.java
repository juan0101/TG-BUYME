package br.com.buyme.controller;

import java.io.IOException;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroIngredienteController {
	
	@FXML private TextField txtCodigo;
	@FXML private TextField txtDescricao;
	
	@FXML
	public void cadastrarIngrediente(){
		Ingrediente ing = new Ingrediente();
		ing.setCodigo(txtCodigo.getText());
		ing.setDescricao(txtDescricao.getText());
		try{
			IngredienteDAO ingDao = new IngredienteDAO();
			ingDao.salvar(ing);
		}catch (Exception e){
			System.out.println("Erro ao salvar ingrediente: " + e);
		}
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
