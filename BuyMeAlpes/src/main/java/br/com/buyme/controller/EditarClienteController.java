package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.entity.Cliente;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditarClienteController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	//POPUP RESPONSAVEL PELAS MENSAGENS DE ERROS,INFORMATION E WARNING
	private Popup popup = new Popup("Editar Cliente");
	//ALIMENTA A TABELA PRINCIPAL DE CLIENTES
	@FXML private TableView<Cliente> tabela_cliente;
    @FXML private TableColumn<Cliente, String> cNome;
	@FXML private TableColumn<Cliente, String> cTelefone;
    @FXML private TableColumn<Cliente, String> cCidade;
    @FXML private TableColumn<Cliente, String> cEndereco;
    @FXML private TableColumn<Cliente, Integer> cNumero;
    @FXML private TableColumn<Cliente, String> cEmail;
    private ObservableList<Cliente> data;
    private List<Cliente> clientes = null;
    
    @FXML private TextField nome,telefone,cidade,endereco,numero,email,login;
    @FXML private Button btnFinalizar,btnTrocarSenha;
    
    //RESPONSAVEL PELO MANUSEIO DO BANCO DE DADOS DE CLIENTES
    private ClienteDAO cliDao = new ClienteDAO();
    //CLIENTE SENDO EDITADO
    private Cliente cliente = null;
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null && !clientes.isEmpty()){
    			//ALIMENTANDO TABELA DE CLIENTES
    			cNome.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("nome")
		        );
    			cTelefone.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("telefone")
		        );
    			cCidade.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("cidade")
		        );
    			cEndereco.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("endereco")
		        );
    			cNumero.setCellValueFactory(
		            new PropertyValueFactory<Cliente,Integer>("numero")
		        );
    			cEmail.setCellValueFactory(
    		            new PropertyValueFactory<Cliente,String>("email")
    		    );
		 
    			data = FXCollections.observableArrayList();
    			data.addAll(clientes);
    			tabela_cliente.setItems(data);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void editar(){
    	cliente = tabela_cliente.getSelectionModel().getSelectedItem();
    	if(cliente != null){
    		alimentarCamposCliente();
    	}else{
    		popup.getError("Selecione um cliente para editar");
    	}
    }
    
    @FXML
    public void excluir(){
    	cliente = tabela_cliente.getSelectionModel().getSelectedItem();
    	if(cliente != null){
    		data.remove(cliente);
    		cliDao.removerById(cliente.getId());
    		popup.getInformation("Cliente excluído com sucesso!");
    	}else{
    		popup.getError("Selecione um cliente para excluir!");
    	}
    }
    
    @FXML
    public void salvarEdicao(){
    	String cliNome = nome.getText();
    	String cliTel = telefone.getText();
    	String cliCidade = cidade.getText();
    	String cliEndereco = endereco.getText();
    	String cliNumero = numero.getText();
    	String cliEmail = email.getText();
    	String cliLogin = login.getText();
    	if(cliLogin.equals(cliente.getLogin()) || verificarLogin(cliLogin)){
    		if(cliNome != null && !cliNome.isEmpty() && cliTel != null && !cliTel.isEmpty() && cliCidade != null && !cliCidade.isEmpty() &&
        			cliEndereco != null && !cliEndereco.isEmpty() && cliNumero != null && !cliNumero.isEmpty() && 
        			cliEmail != null && !cliEmail.isEmpty()){
        		if(Utils.isNumber(cliNumero)){
        			try{
    					cliDao.salvarEdicao(cliente.getId(), cliNome, cliTel, cliEndereco, Integer.parseInt(cliNumero), cliCidade, cliEmail, cliLogin);
    					popup.getInformation("Edição feita com sucesso!");
    					atualizarListEncomenda();
    					limparCampos();
        			}catch(Exception e){
        				e.printStackTrace();
        			}
        		}else{
        			popup.getError("O campo número deve ser um numeral!");
        		}
        	}else{
        		popup.getError("Existe campos obrigatórios em branco!");
        	}
    	}else{
    		popup.getError("login já existente!");
    	}
    }
    
    @FXML
    public void trocarSenha(){
    	try{
			if(cliente != null){
				FXMLLoader loader = new FXMLLoader();
	    		loader.setLocation(getClass().getResource("../view/TrocarSenhaClienteView.fxml"));
	    		AnchorPane page = (AnchorPane) loader.load();
	    		
	    		Stage dialogStage = new Stage();
	            dialogStage.setTitle("Trocar Senha Cliente");
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);
	    		
	    		TrocarSenhaClienteController trocarSenhaClienteController = loader.getController();
	    		trocarSenhaClienteController.setCliente(cliente);
	    		trocarSenhaClienteController.setDialogStage(dialogStage);
	    		
	    		dialogStage.showAndWait();
			}else{
				popup.getError("Selecione um usuario para mudar a senha!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
    }
    
    public void atualizarListEncomenda(){
    	clientes.clear();
    	data.clear();
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null && !clientes.isEmpty()){
    			data.setAll(clientes);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void alimentarCamposCliente(){
    	nome.setText(cliente.getNome());
    	telefone.setText(cliente.getTelefone());
    	cidade.setText(cliente.getCidade());
    	endereco.setText(cliente.getEndereco());
    	numero.setText(cliente.getNumero()+"");
    	email.setText(cliente.getEmail());
    	login.setText(cliente.getLogin());
    	btnFinalizar.setDisable(false);
    	btnTrocarSenha.setDisable(false);
    }
    
    public void limparCampos(){
    	nome.setText("");
    	telefone.setText("");
    	cidade.setText("");
    	endereco.setText("");
    	numero.setText("");
    	email.setText("");
    	login.setText("");
    	btnFinalizar.setDisable(true);
    	btnTrocarSenha.setDisable(true);
    }
    
    public boolean verificarLogin(String login){
		try{
			Cliente cli = null;
			cli = cliDao.getOneWithLogin(login);
			if(cli == null){
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
    
    //MÉTODOS MENU BAR
  	@FXML public void home(ActionEvent event) throws IOException{menu.home(event,root);}
  	
  	@FXML public void cadastrarUsuario(ActionEvent event) throws IOException{menu.cadastrarUsuario(event,root);}
  	
  	@FXML public void cadastrarIngrediente(ActionEvent event) throws IOException{menu.cadastrarIngrediente(event,root);}
  	
  	@FXML public void editarUsuario(ActionEvent event) throws IOException{menu.editarUsuario(event,root);}
  	
  	@FXML public void editarIngrediente(ActionEvent event) throws IOException{menu.editarIngrediente(event,root);}
  	
  	@FXML public void cadastrarProduto(ActionEvent event) throws IOException{menu.cadastrarProduto(event, root);}
  	
  	@FXML public void editarProduto(ActionEvent event) throws IOException{menu.editarProduto(event, root);}
  	
  	@FXML public void cadastrarForma(ActionEvent event) throws IOException{menu.cadastrarForma(event, root);}
  	
  	@FXML public void editarForma(ActionEvent event) throws IOException{menu.editarForma(event, root);}
  	
  	@FXML public void produzirForma(ActionEvent event) throws IOException{menu.produzirForma(event, root);}
  	
  	@FXML public void analisarProducao(ActionEvent event) throws IOException{menu.analisarProducao(event, root);}
  	
  	@FXML public void cadastrarCliente(ActionEvent event) throws IOException{menu.cadastrarCliente(event, root);}
  	
  	@FXML public void gerarEncomenda(ActionEvent event) throws IOException{menu.gerarEncomenda(event, root);}
  	
  	@FXML public void atenderEncomenda(ActionEvent event) throws IOException{menu.atenderEncomenda(event, root);}
  	
  	@FXML public void venda(ActionEvent event) throws IOException{menu.venda(event, root);}
  	
  	@FXML public void editarCliente(ActionEvent event) throws IOException{menu.editarCliente(event, root);}
  	
  	@FXML public void relatorioPerdaProduto(ActionEvent event) throws IOException{menu.relatorioPerdaProduto(event, root);}
  	
  	@FXML public void cadastrarMotivoPerda(ActionEvent event) throws IOException{menu.cadastrarMotivoPerda(event, root);}
  	
  	@FXML public void editarMotivoPerda(ActionEvent event) throws IOException{menu.editarMotivoPerda(event, root);}
  	
  	@FXML public void verificarEstoque(ActionEvent event) throws IOException{menu.verificarEstoque(event, root);}
  	
  	@FXML public void logout(ActionEvent event) throws IOException{menu.logout(event, root, usuario);}
  	
}
