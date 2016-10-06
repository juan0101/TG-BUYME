package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.MotivoPerdaDAO;
import br.com.buyme.entity.MotivoPerda;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditarMotivoPerdaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Editar Motivo de Perda");
	@FXML private TableView<MotivoPerda> tabela_motivo_perda;
    @FXML private TableColumn<MotivoPerda, String> cCodigo;
    @FXML private TableColumn<MotivoPerda, String> cNome;
    private ObservableList<MotivoPerda> data;
    private List<MotivoPerda> mps = null;
	@FXML private TextField codigo,nome;
	private MotivoPerdaDAO mpDao = new MotivoPerdaDAO();
	@FXML private Button btnAtualizar;
	
	//OBJETO SELECIONADO
	private MotivoPerda mp = null;
	
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		menuPermissoes();
		try{
			mps = mpDao.getAllMotivoPerda();
			if(mps != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<MotivoPerda,String>("codigo")
						);
				cNome.setCellValueFactory(
						new PropertyValueFactory<MotivoPerda,String>("descricao")
						);
				data = FXCollections.observableArrayList();
				data.addAll(mps);
				tabela_motivo_perda.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void atualizarMotivoPerda(){
		String codMotivo = codigo.getText();
		String descMotivo = nome.getText();
		if(codMotivo != null && !codMotivo.isEmpty() && descMotivo != null && !descMotivo.isEmpty()){
			mpDao.editarMotivoPerda(mp.getId(), descMotivo);
			popup.getInformation("Edição feita com sucesso!");
			
			codigo.setText("");
			nome.setText("");
			atualizarLista();
		}else{
			popup.getError("Campos obrigatórios em branco!");
		}
	}
	
	@FXML
	public void editar(){
		mp = tabela_motivo_perda.getSelectionModel().getSelectedItem();
		if(mp != null){
			codigo.setText(mp.getCodigo());
			nome.setText(mp.getDescricao());
			btnAtualizar.setDisable(false);
		}else{
			popup.getError("Selecione um motivo de perda para editar!");
		}
	}
	
	@FXML
	public void excluir(){
		mp = tabela_motivo_perda.getSelectionModel().getSelectedItem();
		if(mp != null){
			mpDao.excluir(mp.getId());
			atualizarLista();
		}else{
			popup.getError("Selecione um motivo para excluir!");
		}
	}
	
	public void atualizarLista(){
		mps.clear();
		data.clear();
		mps = mpDao.getAllMotivoPerda();
		data.addAll(mps);
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
