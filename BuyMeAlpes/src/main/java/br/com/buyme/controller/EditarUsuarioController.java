package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.UsuarioDAO;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class EditarUsuarioController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Editar Usuário");
	@FXML private TextField login,nome,telefone,endereco,numero,cidade,email;
	@FXML private CheckBox admin;
	@FXML private Button btnFinalizar,btnTrocarSenha;
	@FXML private TableView<Usuario> tabela_usuario;
    @FXML private TableColumn<Usuario, String> cLogin;
    @FXML private TableColumn<Usuario, String> cNome;
    @FXML private TableColumn<Usuario, String> cTelefone;
    @FXML private TableColumn<Usuario, String> cEmail;
    @FXML private TableColumn<Usuario, Integer> cAdmin;
    private ObservableList<Usuario> data;
    private List<Usuario> usuarios;

    private UsuarioDAO usuDao = new UsuarioDAO();
    private Usuario usu = null;
    
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		try{
			usuarios = usuDao.getAll();
			cLogin.setCellValueFactory(
	            new PropertyValueFactory<Usuario,String>("login")
	        );
			cNome.setCellValueFactory(
	            new PropertyValueFactory<Usuario,String>("nome")
	        );
			cTelefone.setCellValueFactory(
	            new PropertyValueFactory<Usuario,String>("telefone")
	        );
			cEmail.setCellValueFactory(
		        new PropertyValueFactory<Usuario,String>("email")
		    );
			cAdmin.setCellValueFactory(
		        new PropertyValueFactory<Usuario,Integer>("admin")
		    );
	 
	        data = FXCollections.observableArrayList();
	        data.setAll(usuarios);
	        tabela_usuario.setItems(data);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void carregarTabelaUsuario(){
		usuarios.clear();
		usuarios = usuDao.getAll();
		data.clear();
		data.addAll(usuarios);
	}
	
	public void limparCampos(){
		login.setText("");
		nome.setText("");
		telefone.setText("");
		endereco.setText("");
		numero.setText("");
		cidade.setText("");
		email.setText("");
		admin.setSelected(false);
		btnFinalizar.setDisable(true);
		btnTrocarSenha.setDisable(true);
	}
	
	@FXML
	public void salvarEdicao(){
		int idUsu = usu.getId();
		String loginUsu = login.getText();
		String nomeUsu = nome.getText();
		String telUsu = telefone.getText();
		String endUsu = endereco.getText();
		String numUsu = numero.getText();
		String cidUsu = cidade.getText();
		String emailUsu = email.getText();
		boolean adminUsu = admin.isSelected();
		if(loginUsu != null && !loginUsu.isEmpty() && nomeUsu != null && !nomeUsu.isEmpty() && telUsu != null && !telUsu.isEmpty() &&
				endUsu != null && !endUsu.isEmpty() && numUsu != null && !numUsu.isEmpty() && cidUsu != null && !cidUsu.isEmpty() &&
						emailUsu != null && !emailUsu.isEmpty()){
			if(loginUsu.equals(usu.getLogin()) || verificarLogin(loginUsu)){
				if(Utils.isNumber(numUsu)){
					usuDao.editarUsuario(idUsu, loginUsu, nomeUsu, telUsu, endUsu, Integer.parseInt(numUsu), cidUsu, emailUsu, adminUsu);
					
					popup.getInformation("Usuário editado com sucesso!");
			        
			        carregarTabelaUsuario();
			        limparCampos();
				}else{
					popup.getError("O número do endereço deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("Login já existente!");
			}
		}else{
			popup.getError("Preencha todos os campos!");
		}
	}
	
	@FXML
	public void trocarSenha(){
		try{
			if(usu != null){
				FXMLLoader loader = new FXMLLoader();
	    		loader.setLocation(getClass().getResource("../view/TrocarSenhaUsuarioView.fxml"));
	    		AnchorPane page = (AnchorPane) loader.load();
	    		
	    		Stage dialogStage = new Stage();
	            dialogStage.setTitle("Trocar Senha Usuário");
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);
	    		
	    		TrocarSenhaUsuarioController trocarSenhaUsuarioController = loader.getController();
	    		trocarSenhaUsuarioController.setUsuario(usu);
	    		trocarSenhaUsuarioController.setDialogStage(dialogStage);
	    		
	    		dialogStage.showAndWait();
			}else{
				popup.getError("Selecione um usuario para mudar a senha!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void excluir(){
		try{
			usu = tabela_usuario.getSelectionModel().getSelectedItem();
			if(usu != null){
				try{
					usuDao.removerById(usu.getId());
					retirarUsuario();
					
					popup.getInformation("Usuario excluido com sucesso!");
				}catch(Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro ao tentar excluir, tente novamente!");
				}
			}else{
				popup.getError("Selecione algum usuário para excluir!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
		
	}
	
	public void retirarUsuario(){
		data.remove(usu);
	}
	
	@FXML
	public void editar(){
		try{
			usu = tabela_usuario.getSelectionModel().getSelectedItem();
			if(usu != null){
				login.setText(usu.getLogin());
				nome.setText(usu.getNome());
				telefone.setText(usu.getTelefone());
				endereco.setText(usu.getEndereco());
				numero.setText(usu.getNumero()+"");
				cidade.setText(usu.getCidade());
				email.setText(usu.getEmail());
				if(usu.isAdmin()){
					admin.setSelected(true);
				}else{
					admin.setSelected(false);
				}
				
				btnFinalizar.setDisable(false);
				btnTrocarSenha.setDisable(false);
			}else{
				popup.getError("Selecione algum usuário para editar!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	public boolean verificarLogin(String login){
		try{
			Usuario usu = null;
			usu = usuDao.getOneWithLogin(login);
			if(usu == null){
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
