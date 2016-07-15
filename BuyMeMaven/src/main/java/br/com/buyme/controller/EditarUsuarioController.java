package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class EditarUsuarioController {
	
	@FXML private TextField login,nome,telefone,endereco,numero,cidade,email;
	@FXML private TextField senhaAntiga,novaSenha;
	@FXML private CheckBox admin;
	@FXML private Button btnFinalizar,btnTrocarSenha,btnSalvarSenha;
	@FXML private Label lblSenhaAntiga, lblNovaSenha;
	@FXML private TableView<Usuario> tabela_usuario;
    @FXML private TableColumn<Usuario, String> cLogin;
    @FXML private TableColumn<Usuario, String> cNome;
    @FXML private TableColumn<Usuario, String> cTelefone;
    @FXML private TableColumn<Usuario, String> cEmail;
    @FXML private TableColumn<Usuario, Integer> cAdmin;
    private ObservableList<Usuario> data;
    private List<Usuario> usuarios;

    private UsuarioDAO usuDao = new UsuarioDAO();
    private Usuario usuario = null;
    
	@FXML
	protected void initialize(){
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
		
		lblNovaSenha.setVisible(false);
		lblSenhaAntiga.setVisible(false);
		senhaAntiga.setVisible(false);
		senhaAntiga.setText("");
		novaSenha.setVisible(false);
		novaSenha.setText("");
		btnSalvarSenha.setVisible(false);
		
	}
	
	@FXML
	public void salvarEdicao(){
		int idUsu = usuario.getId();
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
			if(loginUsu.equals(usuario.getLogin()) || verificarLogin(loginUsu)){
				if(Utils.isNumber(numUsu)){
					usuDao.editarUsuario(idUsu, loginUsu, nomeUsu, telUsu, endUsu, Integer.parseInt(numUsu), cidUsu, emailUsu, adminUsu);
					
					Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Usuário");
			        dialogoInfo.setContentText("Usuário editado com sucesso!");
			        dialogoInfo.showAndWait();
			        
			        carregarTabelaUsuario();
			        limparCampos();
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Usuário");
			        dialogoInfo.setContentText("O número do endereço deve ser um numeral inteiro!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Usuário");
		        dialogoInfo.setContentText("Login já existente!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Usuário");
	        dialogoInfo.setContentText("Preencha todos os campos!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void trocarSenha(){
		try{
			if(usuario != null){
				lblNovaSenha.setVisible(true);
				lblSenhaAntiga.setVisible(true);
				senhaAntiga.setVisible(true);
				novaSenha.setVisible(true);
				btnSalvarSenha.setVisible(true);
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Usuário");
		        dialogoInfo.setContentText("Selecione um usuario para mudar a senha!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Usuário");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void salvarNovaSenha(){
		try{
			String seAn = senhaAntiga.getText();
			String noSe = novaSenha.getText();
			if(seAn != null && !seAn.isEmpty() && noSe != null && !noSe.isEmpty()){
				if(noSe.length()>4){
					String antigaCripto = Utils.senhaSha256(seAn);
					if(antigaCripto.equals(usuario.getSenha())){
						String novaCripto = Utils.senhaSha256(noSe);
						usuDao.trocarSenha(usuario.getId(), novaCripto);
						
						Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Editar Usuário");
				        dialogoInfo.setContentText("Senha modificada com sucesso!");
				        dialogoInfo.showAndWait();
				        
				        limparCampos();
					}else{
						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Editar Usuário");
				        dialogoInfo.setContentText("Senha antiga incorreta!");
				        dialogoInfo.showAndWait();
					}
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Usuário");
			        dialogoInfo.setContentText("A nova senha deve conter mais de 4 caracteres!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Usuário");
		        dialogoInfo.setContentText("Preencha todos os campos!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Usuário");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void excluir(){
		try{
			usuario = tabela_usuario.getSelectionModel().getSelectedItem();
			if(usuario != null){
				try{
					usuDao.removerById(usuario.getId());
					retirarUsuario();
					
					Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Usuário");
			        dialogoInfo.setContentText("Usuario excluido com sucesso!");
			        dialogoInfo.showAndWait();
				}catch(Exception e){
					e.printStackTrace();
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Usuário");
			        dialogoInfo.setContentText("Houve um erro ao tentar excluir, tente novamente!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Usuário");
		        dialogoInfo.setContentText("Selecione algum usuário para excluir!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Usuário");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
		
	}
	
	public void retirarUsuario(){
		data.remove(usuario);
	}
	
	@FXML
	public void editar(){
		try{
			usuario = tabela_usuario.getSelectionModel().getSelectedItem();
			if(usuario != null){
				login.setText(usuario.getLogin());
				nome.setText(usuario.getNome());
				telefone.setText(usuario.getTelefone());
				endereco.setText(usuario.getEndereco());
				numero.setText(usuario.getNumero()+"");
				cidade.setText(usuario.getCidade());
				email.setText(usuario.getEmail());
				if(usuario.isAdmin()){
					admin.setSelected(true);
				}else{
					admin.setSelected(false);
				}
				
				btnFinalizar.setDisable(false);
				btnTrocarSenha.setDisable(false);
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Usuário");
		        dialogoInfo.setContentText("Selecione algum usuário para editar!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Usuário");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
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
