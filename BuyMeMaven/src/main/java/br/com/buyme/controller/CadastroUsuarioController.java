package br.com.buyme.controller;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import br.com.buyme.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class CadastroUsuarioController {

	@FXML private TextField nome;
	@FXML private TextField telefone;
	@FXML private TextField endereco;
	@FXML private TextField end_numero;
	@FXML private TextField cidade;
	@FXML private TextField email;
	@FXML private TextField login;
	@FXML private TextField senha;
	@FXML private TextField confirma_senha;
	@FXML private CheckBox admin;
	
	
	@FXML
	public void cadastrarUsuario(){
		
		Usuario usuario = new Usuario();
		usuario.setLogin(login.getText());
		usuario.setSenha(Utils.senhaSha256(senha.getText()));
		if(admin.isSelected()){
			usuario.setAdmin(1);
		}else{
			usuario.setAdmin(0);	
		}
		
		usuario.setNome(nome.getText());
		usuario.setTelefone(telefone.getText());
		usuario.setEndereco(endereco.getText());
		usuario.setNumero(Integer.parseInt(end_numero.getText()));
		usuario.setCidade(cidade.getText());
		usuario.setEmail(email.getText());
		
		try{
			UsuarioDAO usuDao = new UsuarioDAO();
			usuDao.salvar(usuario);			
		}catch (Exception e){
			System.out.println("Erro ao salvar usuario: "+ e);
		}
		
	}
	
	@FXML
	public void voltarMenu(){
		System.out.println("voltarMenu");
	}
}
