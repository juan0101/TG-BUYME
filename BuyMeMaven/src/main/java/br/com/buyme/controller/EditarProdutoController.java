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
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EditarProdutoController {
	
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
    public void editarProduto(){
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
    			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Produto");
		        dialogoInfo.setContentText("Selecione um produto para ser editado!");
		        dialogoInfo.showAndWait();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Produto");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
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
    				
    				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
    		        dialogoInfo.setTitle("BuyMe");
    		        dialogoInfo.setHeaderText("Editar Produto");
    		        dialogoInfo.setContentText("Produto excluido com sucesso!");
    		        dialogoInfo.showAndWait();
    			}catch(Exception e){
    				e.printStackTrace();
    				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    		        dialogoInfo.setTitle("BuyMe");
    		        dialogoInfo.setHeaderText("Editar Produto");
    		        dialogoInfo.setContentText("Erro ao excluir produto, tente novamente!");
    		        dialogoInfo.showAndWait();
    			}
    		}else{
    			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Produto");
		        dialogoInfo.setContentText("Selecione um produto para ser editado!");
		        dialogoInfo.showAndWait();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Produto");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
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
					
					Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Produto");
			        dialogoInfo.setContentText("Produto foi editado com sucesso!");
			        dialogoInfo.showAndWait();
			        
			        limparCampos();
			        atualizarListProduto();
			        
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Produto");
			        dialogoInfo.setContentText("O preço do produto deve ser um numeral válido!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Produto");
		        dialogoInfo.setContentText("Existem campos obrigatóris em branco!");
		        dialogoInfo.showAndWait();
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
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Produto");
		        dialogoInfo.setContentText("A quantidade deve ser um numeral inteiro!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Produto");
	        dialogoInfo.setContentText("Selecione um ingrediente!");
	        dialogoInfo.showAndWait();
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
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Produto");
	        dialogoInfo.setContentText("Selecione um ingrediente para remover!");
	        dialogoInfo.showAndWait();
		}
	}
    
    @FXML
	public void voltarMenu(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AdminView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

}
