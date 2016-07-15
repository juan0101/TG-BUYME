package br.com.buyme.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdministradorController {
	
	@FXML
	private Parent root;
	
	@FXML
	public void cadastrarUsuario(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarIngrediente(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarUsuario() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarIngrediente() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarProduto(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarProduto() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarForma(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarForma() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void produzirForma() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/ProduzirFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void analisarProducao() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AnalisarProducaoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarCliente() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void gerarEncomenda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/GerarEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

}
