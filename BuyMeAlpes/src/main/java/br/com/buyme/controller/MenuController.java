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
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Usuário");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarIngrediente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Ingrediente");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarUsuario(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Usuário");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarIngrediente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Ingrediente");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Produto");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Produto");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Forma");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Forma");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void produzirForma(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("ProduzirFormaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Produzir Forma");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void analisarProducao(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("AnalisarProducaoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Analisar Produção");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarCliente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Cliente");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void gerarEncomenda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("GerarEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Gerar Encomenda");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void atenderEncomenda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("AtenderEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Atender Encomenda");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void venda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("VendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Venda");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarCliente(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarClienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Cliente");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void relatorioPerdaProduto(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("RelatorioPerdaProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Relatório Perda Produto");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void cadastrarMotivoPerda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("CadastroMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Cadastrar Motivo Perda");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void editarMotivoPerda(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("EditarMotivoPerdaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Editar Motivo Perda");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void verificarEstoque(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("VerificarEstoqueView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Relatório Verificar Estoque");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void logout(ActionEvent event, Parent parent, Usuario usuario) throws IOException{
		this.root = parent;
		usuario = null;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Login");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void home(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("HomeView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Home");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	public void motivoPerdaLote(ActionEvent event, Parent parent) throws IOException{
		this.root = parent;
		Parent newPageParent = FXMLLoader.load(getClass().getResource("RelatorioMotivoPerdaLoteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.setTitle("Relatório Motivo Perda / Lote");
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

}
