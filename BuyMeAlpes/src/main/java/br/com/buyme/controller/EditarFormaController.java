package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditarFormaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();

	private Popup popup = new Popup("Editar Forma");
	@FXML private TableView<Forma> tabela_forma;
    @FXML private TableColumn<Forma, String> cCodigo;
    @FXML private TableColumn<Forma, String> cDescricao;
    @FXML private TableColumn<Forma, String> cProduto;
    @FXML private TableColumn<Forma, Integer> cQuant;
    private ObservableList<Forma> data;
	@FXML private TextField codigo,descricao,quantidade,prod;
	@FXML private ComboBox<String> comboProd;
	@FXML private Button btnFinalizar;
	private List<Forma> formas = null;
	private List<Produto> produtos = null;
	private List<String> produtoNome = new ArrayList<String>();

	private ProdutoDAO prodDao = new ProdutoDAO();
	private FormaDAO formaDao = new FormaDAO();
	private Forma forma = null;
	
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		try{
			formas = formaDao.getAll();
			if(formas != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("codigo")
						);
				cDescricao.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("descricao")
						);
				cProduto.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("nomeProduto")
						);
				cQuant.setCellValueFactory(
						new PropertyValueFactory<Forma,Integer>("quantidade")
						);
				data = FXCollections.observableArrayList();
				data.addAll(formas);
				tabela_forma.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro ao carregar as formas, tente novamente!");
		}
	}
	
	public void atualizarCombo(){
		if(produtoNome != null){
			produtoNome.clear();
		}
		produtos = prodDao.getAll();
		for(Produto prod: produtos){
			produtoNome.add(prod.getNome());
		}
		comboProd.getItems().setAll(produtoNome);
		comboProd.getSelectionModel().select(forma.getNomeProduto());
	}
	
	public void atualizarListForma(){
		formas.clear();
		formas = formaDao.getAll();
		data.clear();
		if(formas != null){
			data.addAll(formas);
		}
	}
	
	@FXML
	public void editar(){
		forma = tabela_forma.getSelectionModel().getSelectedItem();
		if(forma != null){
			codigo.setText(forma.getCodigo());
			descricao.setText(forma.getDescricao());
			quantidade.setText(forma.getQuantidade()+"");
			atualizarCombo();
			btnFinalizar.setDisable(false);
		}else{
			popup.getError("Selecione uma forma para editar!");
		}
	}
	
	@FXML
	public void excluir(){
		forma = tabela_forma.getSelectionModel().getSelectedItem();
		if(forma != null){
			try{
				data.remove(forma);
				formaDao.excluir(forma.getId());
				popup.getInformation("Forma excluida com sucesso!");
			}catch(Exception e){
				e.printStackTrace();
				popup.getError("Houve um erro, tente novamente!");
			}
		}else{
			popup.getError("Selecione uma forma para editar!");
		}
	}
	
	public void limparCampos(){
		codigo.setText("");
		descricao.setText("");
		quantidade.setText("");
		btnFinalizar.setDisable(true);
		comboProd.getSelectionModel().clearSelection();
	}
	
	@FXML
	public void salvarEdicao(){
		try{
			String codForma = codigo.getText();
			String descForma = descricao.getText();
			String prodForma = comboProd.getSelectionModel().selectedItemProperty().getValue();
			String quantForma = quantidade.getText();
			if(codForma != null && !codForma.isEmpty() && descForma != null && !descForma.isEmpty() && prodForma != null && !prodForma.isEmpty() &&
					quantForma != null && !quantForma.isEmpty()){
				if(Utils.isNumber(quantForma)){
					for(Produto pro: produtos){
						if(pro.getNome().equals(prodForma)){
							formaDao.salvarEdicao(codForma, descForma, Integer.parseInt(quantForma), pro.getId(), forma.getId());
							
							popup.getInformation("Forma editada com sucesso!");
					        
					        atualizarListForma();
					        limparCampos();
						}
					}
				}else{
					popup.getError("A quantidade deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("Existem campos obrigatórios não preenchidos!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
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
}
