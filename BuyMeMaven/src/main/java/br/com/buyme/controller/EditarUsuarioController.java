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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	
	private Popup popup = new Popup("Editar Usu�rio");
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
					
					popup.getInformation("Usu�rio editado com sucesso!");
			        
			        carregarTabelaUsuario();
			        limparCampos();
				}else{
					popup.getError("O n�mero do endere�o deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("Login j� existente!");
			}
		}else{
			popup.getError("Preencha todos os campos!");
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
				popup.getError("Selecione um usuario para mudar a senha!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
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
						
						popup.getInformation("Senha modificada com sucesso!");
				        
				        limparCampos();
					}else{
						popup.getError("Senha antiga incorreta!");
					}
				}else{
					popup.getError("A nova senha deve conter mais de 4 caracteres!");
				}
			}else{
				popup.getError("Preencha todos os campos!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
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
					
					popup.getInformation("Usuario excluido com sucesso!");
				}catch(Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro ao tentar excluir, tente novamente!");
				}
			}else{
				popup.getError("Selecione algum usu�rio para excluir!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
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
				popup.getError("Selecione algum usu�rio para editar!");
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
