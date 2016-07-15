package br.com.buyme.controller;

import java.io.IOException;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroUsuarioController {

	@FXML private TextField nome,telefone,endereco,end_numero,cidade;
	@FXML private TextField email,login,senha,confirma_senha;
	@FXML private CheckBox admin;
	
	private UsuarioDAO usuDao = new UsuarioDAO();
	
	
	@FXML
	public void cadastrarUsuario(){
		try{
			String nomeUsu = nome.getText();
			String telUsu = telefone.getText();
			String endUsu = endereco.getText();
			String numUsu = end_numero.getText();
			String cidUsu = cidade.getText();
			String eUsu = email.getText();
			String loginUsu = login.getText();
			String senhaUsu = senha.getText();
			String confSenhaUsu = confirma_senha.getText();
			boolean adminUsu = admin.isSelected();
			if(nomeUsu != null && !nomeUsu.isEmpty() && telUsu != null && !telUsu.isEmpty() && endUsu != null && !endUsu.isEmpty() && 
					numUsu != null && !numUsu.isEmpty() && cidUsu != null && !cidUsu.isEmpty() && eUsu != null && !eUsu.isEmpty() &&
					loginUsu != null && !loginUsu.isEmpty() && senhaUsu != null && !senhaUsu.isEmpty() &&
					confSenhaUsu != null && !confSenhaUsu.isEmpty()){
				if(Utils.isNumber(numUsu)){
					if(verificarLogin(loginUsu)){
						if(senhaUsu.length() > 4){
							if(senhaUsu.equals(confSenhaUsu)){
								Usuario usuario = new Usuario();
								usuario.setLogin(loginUsu);
								usuario.setSenha(Utils.senhaSha256(senhaUsu));
								if(adminUsu){
									usuario.setAdmin(true);
								}else{
									usuario.setAdmin(false);	
								}
								usuario.setNome(nomeUsu);
								usuario.setTelefone(telUsu);
								usuario.setEndereco(endUsu);
								usuario.setNumero(Integer.parseInt(numUsu));
								usuario.setCidade(cidUsu);
								usuario.setEmail(eUsu); 	
							
								usuDao.salvar(usuario);	
								
								Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
						        dialogoInfo.setTitle("BuyMe");
						        dialogoInfo.setHeaderText("Salvar Usuário");
						        dialogoInfo.setContentText("Usuário salvo com sucesso!");
						        dialogoInfo.showAndWait();
						        
						        limparCampos();
						        
							}else{
								Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
						        dialogoInfo.setTitle("BuyMe");
						        dialogoInfo.setHeaderText("Salvar Usuário");
						        dialogoInfo.setContentText("Verifique a senha e sua confirmação!");
						        dialogoInfo.showAndWait();
							}
						}else{
							Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
					        dialogoInfo.setTitle("BuyMe");
					        dialogoInfo.setHeaderText("Salvar Usuário");
					        dialogoInfo.setContentText("A senha deve conter mais que 4 caracteres!");
					        dialogoInfo.showAndWait();
						}
					}else{
						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Usuário");
				        dialogoInfo.setContentText("Login já existente!");
				        dialogoInfo.showAndWait();
					}
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Usuário");
			        dialogoInfo.setContentText("O campo número do endereço deve ser um numeral inteiro!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Usuário");
		        dialogoInfo.setContentText("Existem campos obrigatórios nulos!");
		        dialogoInfo.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Usuário");
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
	
	public void limparCampos(){
		nome.setText("");
		telefone.setText("");
		endereco.setText("");
		end_numero.setText("");
		cidade.setText("");
		email.setText("");
		login.setText("");
		senha.setText("");
		confirma_senha.setText("");
		admin.setSelected(false);
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
