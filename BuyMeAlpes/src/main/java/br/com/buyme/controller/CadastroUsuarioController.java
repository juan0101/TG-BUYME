package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CadastroUsuarioController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Salvar Usuário");
	@FXML private TextField nome,telefone,endereco,end_numero,cidade;
	@FXML private TextField email,login,senha,confirma_senha;
	@FXML private CheckBox admin;
	@FXML private Button btnPermissao;
	
	private UsuarioDAO usuDao = new UsuarioDAO();
	private List<PermissoesEnum> permissoes = null;
	
	@FXML
	public void verificaAdmin(){
		if(admin.isSelected()){
			btnPermissao.setDisable(true);
		}else{
			btnPermissao.setDisable(false);
		}
	}
	
	public void permissao(){
		try{
			FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("CadastroParametroView.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();
    		
    		Stage dialogStage = new Stage();
            dialogStage.setTitle("Permissões");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
    		
    		CadastroParametroController cadastroParametroController = loader.getController();
    		cadastroParametroController.setDialogStage(dialogStage);
    		
    		dialogStage.showAndWait();
    		permissoes = cadastroParametroController.buscarPermissoes();
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
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
			String confSenhaUsu = confirma_senha.getText();
			boolean adminUsu = admin.isSelected();
			if(nomeUsu != null && !nomeUsu.isEmpty() && telUsu != null && !telUsu.isEmpty() && endUsu != null && !endUsu.isEmpty() && 
					numUsu != null && !numUsu.isEmpty() && cidUsu != null && !cidUsu.isEmpty() && eUsu != null && !eUsu.isEmpty() &&
					loginUsu != null && !loginUsu.isEmpty() && senhaUsu != null && !senhaUsu.isEmpty() &&
					confSenhaUsu != null && !confSenhaUsu.isEmpty()){
				if(Utils.isNumber(numUsu)){
					if(verificarLogin(loginUsu)){
						if(senhaUsu.length() >= 4){
							if(senhaUsu.equals(confSenhaUsu)){
								Usuario usuario = new Usuario();
								usuario.setLogin(loginUsu);
								usuario.setSenha(Utils.senhaSha256(senhaUsu));
								if(adminUsu){
									usuario.setAdmin(true);
								}else{
									usuario.setAdmin(false);
									permissao();
									if(permissoes != null){
										usuario.setPermissoes(permissoes);
									}
								}
								usuario.setNome(nomeUsu);
								usuario.setTelefone(telUsu);
								usuario.setEndereco(endUsu);
								usuario.setNumero(Integer.parseInt(numUsu));
								usuario.setCidade(cidUsu);
								usuario.setEmail(eUsu);
								
								usuDao.salvar(usuario);	
								
								popup.getInformation("Usuário salvo com sucesso!");
						        
						        limparCampos();
						        
							}else{
								popup.getError("Verifique a senha e sua confirmação!");
							}
						}else{
							popup.getError("A senha deve conter mais que 4 caracteres!");
						}
					}else{
						popup.getError("Login já existente!");
					}
				}else{
					popup.getError("O campo número do endereço deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("Existem campos obrigatórios nulos!");
			}
		}catch (Exception e){
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
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		menuPermissoes();
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
	
}
