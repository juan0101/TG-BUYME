package br.com.buyme.controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class LoginController{
	@FXML
	private TextField txtLoginUsuario;
	@FXML
	private TextField txtLoginSenha;
	
	private Usuario usuario = Usuario.getInstance();
	private UsuarioDAO usuDao = new UsuarioDAO();
	
	@FXML
	public void verificarLogin(ActionEvent event) throws IOException{
		String strLogin = txtLoginUsuario.getText();
		String strSenha = txtLoginSenha.getText();
		if(strLogin != null && !strLogin.isEmpty() && strSenha != null && !strSenha.isEmpty()){
			strSenha = Utils.senhaSha256(strSenha);
			Usuario retornoUsuario = usuDao.getOne(strLogin, strSenha);
			if(retornoUsuario != null ){
				preencherUsuario(retornoUsuario);
				
				Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/HomeView.fxml"));
				Scene newPageScene = new Scene(newPageParent);
				Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				newStage.hide();
				newStage.setScene(newPageScene);
				newStage.show();
			}else{
				JOptionPane.showMessageDialog(null, "usuario nao encontrado");
			}
		}
	}
	
	public void preencherUsuario(Usuario usu){
		usuario.setId(usu.getId());
		usuario.setAdmin(usu.isAdmin());
		usuario.setCidade(usu.getCidade());
		usuario.setEmail(usu.getEmail());
		usuario.setEndereco(usu.getEndereco());
		usuario.setLogin(usu.getLogin());
		usuario.setNome(usu.getNome());
		usuario.setNumero(usu.getNumero());
		usuario.setSenha(usu.getSenha());
		usuario.setTelefone(usu.getTelefone());
		usuario.setPermissoes(usu.getPermissoes());
	}
	
	
}
