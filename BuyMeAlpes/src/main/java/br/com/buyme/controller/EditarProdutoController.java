package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.dao.ProdutoIngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoIngrediente;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditarProdutoController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Editar Produto");
	@FXML private Button btnAdicionar,btnFinalizar,btnRetirar;
	@FXML private ComboBox<String> comboIng;
    @FXML private TextField nome,preco,quantidade;
    @FXML private RadioButton grama,quilo;
    @FXML private TableView<ProdutoIngrediente> tabela_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, Double> coluna_quantidade;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_medida;
    private ObservableList<ProdutoIngrediente> data,dataSave;
    private List<Ingrediente> ingredientes = null;
    
    @FXML private TableView<Produto> tabela_produto;
    @FXML private TableColumn<Produto, String> cCodigo;
    @FXML private TableColumn<Produto, String> cNome;
    @FXML private TableColumn<Produto, Double> cValor;
    private ObservableList<Produto> dataProduto;
    private List<Produto> produtos = null;
    
    private ProdutoDAO prodDao = new ProdutoDAO();
    private Produto produto = null;
    private IngredienteDAO ingDao = new IngredienteDAO();
    private ProdutoIngredienteDAO prodIngDao = new ProdutoIngredienteDAO();
    private ProdutoIngrediente prodIng = null;
    private List<Integer> idProdIng = new ArrayList<Integer>();
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	try{
    		produtos = prodDao.getAll();
    		if(produtos != null){
    			//ALIMENTANDO A TABELA DE PRODUTOS
    			cCodigo.setCellValueFactory(
						new PropertyValueFactory<Produto,String>("codigo")
				);
			    cNome.setCellValueFactory(
			    		new PropertyValueFactory<Produto,String>("nome")
			    );
			    cValor.setCellValueFactory(
			    		new PropertyValueFactory<Produto,Double>("valor")
			    );
			    dataProduto = FXCollections.observableArrayList();
			    dataProduto.addAll(produtos);
			    tabela_produto.setItems(dataProduto);
    		}
    		//ALIMENTANDO COMBOBOX DE INGREDIENTES
			ingredientes = ingDao.getAllIngredientes();
			if(ingredientes != null){
				List<String> ingredientesNomes = new ArrayList<String>();
				for(Ingrediente ing: ingredientes){
					ingredientesNomes.add(ing.getDescricao());
				}
				comboIng.getItems().setAll(ingredientesNomes);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void atualizarListProduto(){
    	produtos.clear();
    	produtos = prodDao.getAll();
    	dataProduto.clear();
    	if(produtos != null){
    		dataProduto.setAll(produtos);
    	}
    }
    
    public void limparCampos(){
    	nome.setText("");
    	preco.setText("");
    	comboIng.getSelectionModel().clearSelection();
    	quantidade.setText("");
    	if(data != null){
    		data.clear();
    	}
    	btnAdicionar.setDisable(true);
    	btnRetirar.setDisable(true);
    	btnFinalizar.setDisable(true);
    }
    
    @FXML
    public void editar(){
    	try{
    		produto = tabela_produto.getSelectionModel().getSelectedItem();
    		if(produto != null){
    			nome.setText(produto.getNome());
    			preco.setText(produto.getValor()+"");
    			buscarIngrediente();
    			btnAdicionar.setDisable(false);
    	    	btnRetirar.setDisable(false);
    	    	btnFinalizar.setDisable(false);
    		}else{
    			popup.getError("Selecione um produto para ser editado!");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
    }
    
    @FXML
    public void excluir(){
    	try{
    		produto = tabela_produto.getSelectionModel().getSelectedItem();
    		if(produto != null){
    			try{
    				limparCampos();
    				dataProduto.remove(produto);
    				prodDao.excluirProduto(produto.getId());
    				
    				popup.getInformation("Produto excluido com sucesso!");
    			}catch(Exception e){
    				e.printStackTrace();
    				popup.getError("Erro ao excluir produto, tente novamente!");
    			}
    		}else{
    			popup.getError("Selecione um produto para ser editado!");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
    }
    
    @FXML 
    public void buscarIngrediente(){
		//ALIMENTANDO TABELA DE INGREDIENTES JA CADASTRADOS NO PRODUTO
		coluna_ingrediente.setCellValueFactory(
				new PropertyValueFactory<ProdutoIngrediente,String>("descricao_ingrediente")
		);
	    coluna_quantidade.setCellValueFactory(
	    		new PropertyValueFactory<ProdutoIngrediente,Double>("quantidade")
	    );
	    coluna_medida.setCellValueFactory(
	    		new PropertyValueFactory<ProdutoIngrediente,String>("grama_quilo")
	    );
	    data = FXCollections.observableArrayList();
	    data.addAll(prodIngDao.getList(produto));
	    tabela_ingrediente.setItems(data);
    }
    
    @FXML
    public void salvarProduto(){
		try{
			List<ProdutoIngrediente> listaProdutoIngrediente = new ArrayList<ProdutoIngrediente>();
			dataSave = tabela_ingrediente.getItems();
			String nProd = nome.getText();
			String pProd = preco.getText().replace(',', '.');;
			if(nProd != null && !nProd.isEmpty() && pProd != null && !pProd.isEmpty()){
				if(Utils.isNumberMonetario(pProd)){
					if(dataSave != null && !dataSave.isEmpty()){
						for(ProdutoIngrediente pro: dataSave){
							pro.setProduto(produto);
							for(Ingrediente ing: ingredientes){
								if(ing.getDescricao().equals(pro.getDescricao_ingrediente())){
									pro.setIngrediente(ing);
								}
							}
							listaProdutoIngrediente.add(pro);
						}
					}else{
						listaProdutoIngrediente = null;
					}
					prodIngDao.deleteListProdIng(idProdIng);
					prodDao.editarProduto(produto.getId(),nProd,Double.parseDouble(pProd),listaProdutoIngrediente);
					
					popup.getInformation("Produto foi editado com sucesso!");
			        
			        limparCampos();
			        atualizarListProduto();
			        
				}else{
					popup.getError("O preço do produto deve ser um numeral válido!");
				}
			}else{
				popup.getError("Existem campos obrigatóris em branco!");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
    }
    
    /**
	 * Método que pega o ingrediente selecionado no combo box 
	 * e adiciona no listView do produto
	 */
    @FXML
    public void adicionarIngrediente(){
    	String desc = comboIng.getSelectionModel().selectedItemProperty().getValue();
		String quant = quantidade.getText();
		if(desc != null && !desc.isEmpty()){
			if(Utils.isNumber(quant)){
				ProdutoIngrediente pi = new ProdutoIngrediente();
				pi.setDescricao_ingrediente(desc);
				pi.setQuantidade(Double.parseDouble(quant));
				pi.setGrama_quilo(grama.isSelected()? grama.getText():quilo.getText());
		        data.add(pi);
		        quantidade.setText("");
			}else{
				popup.getError("A quantidade deve ser um numeral inteiro!");
			}
		}else{
			popup.getError("Selecione um ingrediente!");
		}
    }
    
    /**
	 * Método para retirar um ingrediente da tabela listView
	 */
    @FXML
	public void retirarIngrediente(){
    	prodIng = tabela_ingrediente.getSelectionModel().getSelectedItem();
		if(prodIng != null){
			idProdIng.add(prodIng.getId());
			data.remove(prodIng);
		}else{
			popup.getError("Selecione um ingrediente para remover!");
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
