package br.com.buyme.controller;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Data;

@Data
public class EditarUsuarioController {
	
	@FXML private TextField txtNome;
	@FXML private TextField txtTelefone;
	@FXML private TextField txtEndereco;
	@FXML private TextField txtNumero;
	@FXML private TextField txtCidade;
	@FXML private TextField txtEmail;
	@FXML private TextField txtLogin;
	@FXML private TextField txtSenha;
	@FXML private TextField txtConfirmaSenh;
	@FXML private CheckBox checkAdmin;
		  private int idUsuarioEdicao;
	
	 @FXML
	 protected void initialize(){
		 UsuarioDAO usuDao = new UsuarioDAO();
		 Usuario usu = usuDao.getOneWithLogin("teste");
		 setIdUsuarioEdicao(usu.getId());
		 txtNome.setText(usu.getNome());
		 txtTelefone.setText(usu.getTelefone());
		 txtEndereco.setText(usu.getEndereco());
		 txtNumero.setText(""+usu.getNumero());
		 txtCidade.setText(usu.getCidade());
		 txtEmail.setText(usu.getEmail());
		 checkAdmin.setSelected(usu.getAdmin()==1?true:false);
	 }
	 
	 @FXML
	 public void salvarEdicao(){
//		 Usuario usu = new Usuario();
//		 usu.setNome(txtNome.getText());
//		 usu.setTelefone(txtTelefone.getText());
//		 usu.setEndereco(txtEndereco.getText());
//		 usu.setNumero(Integer.parseInt(txtNumero.getText()));
//		 usu.setCidade(txtCidade.getText());
//		 usu.setEmail(txtEmail.getText());
//		 usu.setAdmin(checkAdmin.isSelected()? 1:0);
//		 UsuarioDAO usuDao = new UsuarioDAO();
//		 try{
//			 
//		 }catch (Exception e){
//			 
//		 }
	 }
	 
}
