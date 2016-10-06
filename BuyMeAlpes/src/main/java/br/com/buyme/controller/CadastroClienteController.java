package br.com.buyme.controller;

import java.io.IOException;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;

public class CadastroClienteController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();

	private Popup popup = new Popup("Salvar Cliente");
	@FXML private TextField nome,telefone,endereco,end_numero,cidade,email,login,senha;
	private ClienteDAO cliDao = new ClienteDAO();
	
	@FXML
	public void finalizar(){
		try{
			String nomeUsu = nome.getText();
			String telUsu = telefone.getText();
			String endUsu = endereco.getText();
			String numUsu = end_numero.getText();
			String cidUsu = cidade.getText();
			String eUsu = email.getText();
			String loginUsu = login.getText();
			String senhaUsu = senha.getText();
			if(nomeUsu != null && !nomeUsu.isEmpty() && telUsu != null && !telUsu.isEmpty() && endUsu != null && !endUsu.isEmpty() && 
					numUsu != null && !numUsu.isEmpty() && cidUsu != null && !cidUsu.isEmpty() && eUsu != null && !eUsu.isEmpty()){
				if(Utils.isNumber(numUsu)){
					try{
						cliDao.salvar(nomeUsu, telUsu, endUsu, Integer.parseInt(numUsu), cidUsu, eUsu, loginUsu, senhaUsu);
						popup.getInformation("Cliente salvo com sucesso!");
				        limparCampos();
					}catch(Exception e){
						e.printStackTrace();
						popup.getError("Houve um erro ao salvar o cliente, tente novamente!");
					}
				}else{
					popup.getError("O campo número do endereço deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("Existem campos obrigatórios nulos!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
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
	}
	
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		menuPermissoes();
	}
	
	public void menuPermissoes(){
		if(!usuario.isAdmin()){
			if(!usuario.getPermissoes().contains(PermissoesEnum.CADASTRO)){
				menuCadastrar.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.EDICAO)){
				menuEditar.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.PRODUCAO)){
				menuProducao.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.ENCOMENDA)){
				menuEncomenda.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.VENDA)){
				menuVenda.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.RELATORIO)){
				menuRelatorio.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.HOME)){
				menuHome.setVisible(false);
			}
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
	
	@FXML public void motivoPerdaLote(ActionEvent event) throws IOException{menu.motivoPerdaLote(event, root);}
}
