package br.com.buyme.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdministradorController implements Initializable{
	
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
	
	@FXML
	public void atenderEncomenda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AtenderEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void venda() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../view/VendaView.fxml"));
		loader.load();
		
		Parent newPageParent = loader.getRoot();
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		
		VendaController vendaController = loader.getController();
		vendaController.setTeste(true);
		
		newStage.show();
	}
	
	@FXML
	public void editarCliente() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void relatorioPerdaProduto() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/RelatorioPerdaProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarMotivoPerda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarMotivoPerda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void verificarEstoque() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VerificarEstoqueView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}


}
