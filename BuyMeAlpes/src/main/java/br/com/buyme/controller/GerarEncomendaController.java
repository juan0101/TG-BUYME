package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.dao.EncomendaDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.entity.Cliente;
import br.com.buyme.entity.Encomenda;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GerarEncomendaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Salvar Encomenda");
	@FXML private ComboBox<String> comboCliente,comboProd;
	@FXML private Label lblValor;
	@FXML private TextField quantidade;
	@FXML private TableView<Encomenda> tabela_encomenda;
	@FXML private TableColumn<Encomenda, String> cCliente;
    @FXML private TableColumn<Encomenda, String> cProduto;
    @FXML private TableColumn<Encomenda, Integer> cQuantidade;
    @FXML private TableColumn<Encomenda, Double> cValor;
    private ObservableList<Encomenda> data;
    private ObservableList<Encomenda> dataSave;
    
    private ClienteDAO cliDao = new ClienteDAO();
    private List<Cliente> clientes = null;
    private ProdutoDAO prodDao = new ProdutoDAO();
    private List<Produto> produtos = null;
    private EncomendaDAO encDao = new EncomendaDAO();
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	menuPermissoes();
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null){
    			List<String> clientesNomes = new ArrayList<String>();
    			for(Cliente cli: clientes){
    				clientesNomes.add(cli.getNome());
    			}
    			comboCliente.getItems().setAll(clientesNomes);
    		}
    		produtos = prodDao.getAll();
    		if(produtos != null){
    			List<String> produtosNomes = new ArrayList<String>();
    			for(Produto prod: produtos){
    				produtosNomes.add(prod.getNome());
    			}
    			comboProd.getItems().setAll(produtosNomes);
    		}
    		cCliente.setCellValueFactory(
	            new PropertyValueFactory<Encomenda,String>("descCliente")
	        );			
    		cProduto.setCellValueFactory(
	            new PropertyValueFactory<Encomenda,String>("descProduto")
	        );
	        cQuantidade.setCellValueFactory(
	            new PropertyValueFactory<Encomenda,Integer>("quantidade")
	        );
	        cValor.setCellValueFactory(
	            new PropertyValueFactory<Encomenda,Double>("valor")
	        );
	 
	        data = FXCollections.observableArrayList();
	        tabela_encomenda.setItems(data);
	        
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    @FXML
	public void adicionar(){
		String strCliente = comboCliente.getSelectionModel().selectedItemProperty().getValue();
		String strProd = comboProd.getSelectionModel().selectedItemProperty().getValue();
		String quant = quantidade.getText();
		if(strCliente != null && !strCliente.isEmpty() && strProd != null && !strProd.isEmpty() && quant != null && !quant.isEmpty()){
			if(Utils.isNumber(quant)){ 
				int intQuant = Integer.parseInt(quant);
				Encomenda e = new Encomenda();
				e.setCodigo(gerarCodigo()+"");
				for(Produto prod: produtos){
					if(prod.getNome().equals(strProd)){
						e.setProduto(prod);
						e.setDescProduto(prod.getNome());
						e.setValor(intQuant * prod.getValor());
					}
				}
				for(Cliente cli: clientes){
					if(cli.getNome().equals(strCliente)){
						e.setCliente(cli);
						e.setDescCliente(cli.getNome());
					}
				}
				e.setQuantidade(intQuant);
		        data.add(e);
			}else{
				popup.getError("A quantidade deve ser um numeral inteiro!");
			}
		}else{
			popup.getError("Selecione um cliente, um produto e adicione a quantidade!");
		}
	}
	
    @FXML
    public void retirar(){
    	Encomenda e = null;
		e = tabela_encomenda.getSelectionModel().getSelectedItem();
		if(e != null){
			data.remove(e);
		}else{
			popup.getError("Selecione uma encomenda para remover!");
		}
    }
    
    @FXML
    public void finalizar(){
    	dataSave = tabela_encomenda.getItems();
    	if(dataSave != null && !dataSave.isEmpty()){
    		for(Encomenda e: dataSave){
    			try{
    				encDao.salvar(e.getCodigo(), e.getCliente(), e.getProduto(), e.getQuantidade(), 
    						e.getValor(), e.getDescCliente(),e.getDescProduto());
    				
    			}catch(Exception ex){
    				ex.printStackTrace();
    			}
    		}
    		popup.getInformation("As encomendas foram salvas com sucesso!");
	        
	        limparCampos();
    	}else{
    		popup.getError("Tabela de encomendas esta vazia!");
    	}
    }
    
    public void limparCampos(){
    	quantidade.setText("");
		comboCliente.getSelectionModel().clearSelection();
		comboProd.getSelectionModel().clearSelection();
		data.clear();
		lblValor.setText("");
    }
    
    
    public int gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Encomenda e = null;
			while(true){
				int codEncomenda = randomGenerator.nextInt(10000);
				e = encDao.getOne(codEncomenda+"");
				if(e != null){
					continue;
				}else{
					return codEncomenda;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}
    
    public void mostrarValor(){
    	String strProd = comboProd.getSelectionModel().selectedItemProperty().getValue();
    	for(Produto prod: produtos){
			if(prod.getNome().equals(strProd)){
				lblValor.setText(prod.getValor()+"");
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
