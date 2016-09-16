package br.com.buyme.controller;

import java.io.IOException;
import java.util.Random;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;

public class CadastroIngredienteController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Salvar Ingrediente");
	@FXML private TextField txtCodigo,txtDescricao;
	private IngredienteDAO ingDao = new IngredienteDAO();
	private boolean gerouCod = false;
	
	 /**
     * Método que gera o código de um produto
     */
    @FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Ingrediente ing = null;
			while(true){
				int codProduto = randomGenerator.nextInt(10000);
				ing = ingDao.getOne(codProduto+"");
				if(ing != null){
					continue;
				}else{
					txtCodigo.setText(codProduto+"");
					gerouCod = true;
					break;
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Método que faz o cadastro do ingrediente
     */
	@FXML
	public void finalizar(){
		if(gerouCod){
			try{
				String codIng = txtCodigo.getText();
				String descIng = txtDescricao.getText();
				if(codIng != null && !codIng.isEmpty() && descIng != null && !descIng.isEmpty()){
					Ingrediente ing = new Ingrediente();
					ing.setCodigo(codIng);
					ing.setDescricao(descIng);
					ingDao.salvar(ing);
					
					popup.getInformation("Ingrediente salvo com sucesso!");
			        
					limparCampos();
				}else{
					popup.getError("Existem campos obrigatórios nulos!");
				}
			}catch (Exception e){
				e.printStackTrace();
				popup.getError("Houve um erro ao salvar o ingrediente, tente novamente!");
			}
		}else{
			popup.getError("O código do ingrediente deve ser gerado!");
		}
	}
	
	/**
	 * Método para limpar os campos
	 */
	public void limparCampos(){
		txtCodigo.setText("");
		txtDescricao.setText("");
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
