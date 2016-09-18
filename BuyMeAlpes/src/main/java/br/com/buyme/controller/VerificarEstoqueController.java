package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VerificarEstoqueController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Verificar Estoque");
	@FXML private Label lblTotal,lblQuantidadeTotal;
	@FXML private ComboBox<String> comboProdutos;
	@FXML private Button btnDownload;
	@FXML private TableView<ProdutoPronto> tabela_produto;
    @FXML private TableColumn<ProdutoPronto, Integer> cCodigo;
    @FXML private TableColumn<ProdutoPronto, String> cDescricao;
    @FXML private TableColumn<ProdutoPronto, Integer> cQuantidade;
    private ObservableList<ProdutoPronto> data;
    
    private List<Produto> produtos = null;
    private List<String> nomeProdutos = new ArrayList<String>();
    private List<ProdutoPronto> listaPp = null;
    
    private ProdutoDAO prodDao = new ProdutoDAO();
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    
    //VARIAVEIS PARA O RELATORIO
    private int quantidadeTotal = 0;
    private String nomeProduto = "";
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	menuPermissoes();
    	try{
    		produtos = prodDao.getAll();
    		if(produtos != null){
    			nomeProdutos.add("");
    			for(Produto p: produtos){
    				nomeProdutos.add(p.getNome());
    			}
    			comboProdutos.getItems().setAll(nomeProdutos);
    			
    			cCodigo.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Integer>("codigo")
		        );
		        cDescricao.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,String>("descricao")
		        );
		        cQuantidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Integer>("quantidade")
		        );
		 
		        data = FXCollections.observableArrayList();
		        tabela_produto.setItems(data);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void verificar(){
    	String nomeCombo = comboProdutos.getSelectionModel().selectedItemProperty().getValue() == null? null : 
			comboProdutos.getSelectionModel().selectedItemProperty().getValue().equals("")? null : 
				comboProdutos.getSelectionModel().selectedItemProperty().getValue();
    	try{
    		quantidadeTotal = 0;
    		data.clear();
    		nomeProduto = nomeCombo;
    		listaPp = ppDao.verificaProdutoEstoque(nomeProduto);
    		for(ProdutoPronto pp: listaPp){
    			quantidadeTotal = quantidadeTotal + pp.getQuantidade();
    		}
    		lblQuantidadeTotal.setText(quantidadeTotal+"");
    		data.addAll(listaPp);
    		btnDownload.setDisable(false);
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
    }
    
    @FXML
    public void baixarRelatorio(){
    	try{
    		String query = "select codigo,descricao,SUM(quantidade) as quantidade from produto_pronto where 1=1 ";
    		if(nomeProduto != null){
    			query = query + "and descricao = '"+nomeProduto+"' ";
    		}
    		query = query + "group by descricao";
    		ppDao.gerarRelatorioEstoquePdf(query, quantidadeTotal);
    		popup.getInformation("Relatório criado com sucesso!");
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
