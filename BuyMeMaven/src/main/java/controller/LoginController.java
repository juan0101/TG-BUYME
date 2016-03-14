package controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import dao.UsuarioDAO;
import entity.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;
import util.Utils;

@Data
public class LoginController{
	@FXML
	private TextField txtLoginUsuario;
	@FXML
	private TextField txtLoginSenha;
	
	@FXML
	public void verificarLogin(ActionEvent event) throws IOException{
		Usuario usuario = new Usuario();
		System.out.println("verificou");
		usuario.setLogin(txtLoginUsuario.getText());
		usuario.setSenha(Utils.senhaSha256(txtLoginSenha.getText()));
		UsuarioDAO usuDao = new UsuarioDAO();
		Usuario usuarioRetorno = usuDao.getOne(usuario);
		if( usuarioRetorno != null ){
			mudarScene(event,usuarioRetorno);
		}else{
			JOptionPane.showMessageDialog(null, "usuario nao encontrado");
		}
	}
	
	public void mudarScene(ActionEvent event,Usuario usuario)throws IOException{
		System.out.println("entrei");
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AdminView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	
}
