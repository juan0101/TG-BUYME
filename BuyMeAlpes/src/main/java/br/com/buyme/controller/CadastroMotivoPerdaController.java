package br.com.buyme.controller;

import java.io.IOException;
import java.util.Random;

import br.com.buyme.dao.MotivoPerdaDAO;
import br.com.buyme.entity.MotivoPerda;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;

public class CadastroMotivoPerdaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	@FXML private TextField codigo,nome;
	private Popup popup = new Popup("Cadastro de Motivo de Perda");
	private MotivoPerdaDAO mpDao = new MotivoPerdaDAO();
	
	@FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			MotivoPerda mp = null;
			while(true){
				int codMotivo = randomGenerator.nextInt(10000);
				mp = mpDao.getOneByCod(codMotivo+"");
				if(mp != null){
					continue;
				}else{
					codigo.setText(codMotivo+"");
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void finalizar(){
		String codMotivo = codigo.getText();
		String descMotivo = nome.getText();
		if(codMotivo != null && !codMotivo.isEmpty() && descMotivo != null && !descMotivo.isEmpty()){
			mpDao.salvar(codMotivo, descMotivo);
			popup.getInformation("Motivo salvo com sucesso!");
			
			codigo.setText("");
			nome.setText("");
		}else{
			popup.getError("Campos obrigatórios em branco!");
		}
	}
	
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
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
