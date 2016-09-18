package br.com.buyme.controller;

import java.io.IOException;

import br.com.buyme.entity.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
	
	private Parent root;
	private static MenuController menuController;
	
	public MenuController(){}
	
	public static synchronized MenuController getInstance(){
		if(menuController == null){
			menuController = new MenuController();
		}
		return menuController;
	}
	
	public void cadastrarUsuario(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarIngrediente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarUsuario(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarIngrediente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void produzirForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/ProduzirFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void analisarProducao(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AnalisarProducaoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarCliente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void gerarEncomenda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/GerarEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void atenderEncomenda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AtenderEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void venda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarCliente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void relatorioPerdaProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/RelatorioPerdaProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarMotivoPerda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarMotivoPerda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void verificarEstoque(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VerificarEstoqueView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void logout(ActionEvent event, Parent parent, Usuario usuario) throws IOException{
		this.root = parent;
		usuario = null;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void home(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/HomeView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void motivoPerdaLote(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/RelatorioMotivoPerdaLoteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

}
