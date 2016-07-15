package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.dao.ProdutoDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CadastroProdutoController {
	
	@FXML private ComboBox<String> comboIng;
	@FXML private TableView<ProdutoIngrediente> tabela_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, Double> coluna_quantidade;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_medida;
    @FXML private TextField codigo,nome,preco,quantidade;
    @FXML private RadioButton grama,quilo;
    private ObservableList<ProdutoIngrediente> data;
    private ObservableList<ProdutoIngrediente> dataSave;
    private List<Ingrediente> ingredientes;
    private boolean gerouCod = false;
    
    private IngredienteDAO ingDao = new IngredienteDAO();
    private ProdutoDAO prodDao = new ProdutoDAO();
    
    /**
     * Método para voltar para o menu principal administrador
     * @param event
     * @throws IOException
     */
    @FXML
	public void voltarMenu(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AdminView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
    
    /**
     * Método que gera o código de um produto
     */
    @FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Produto p = null;
			while(true){
				int codProduto = randomGenerator.nextInt(10000);
				p = prodDao.getOne(codProduto+"");
				if(p != null){
					continue;
				}else{
					codigo.setText(codProduto+"");
					gerouCod = true;
					break;
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

    /**
     * Método para salvar um novo produto
     */
	@FXML
	public void salvarProduto(){
		try{
			if(gerouCod){
				String codProd = codigo.getText();
				String nomeProd = nome.getText();
				String precoProd = preco.getText().replace(',', '.');
				if(codProd != null && !codProd.isEmpty() && nomeProd != null && !nomeProd.isEmpty() && 
						precoProd != null && !precoProd.isEmpty()){
					if(Utils.isNumberMonetario(precoProd)){
						List<ProdutoIngrediente> listaProdutoIngrediente = new ArrayList<ProdutoIngrediente>();
						dataSave = tabela_ingrediente.getItems();
						Produto produto = new Produto();
						produto.setCodigo(codProd);
						produto.setNome(nomeProd);
						produto.setValor(Double.parseDouble(precoProd));
						produto.setProduto_ingrediente(listaProdutoIngrediente);
						for(ProdutoIngrediente pro: dataSave){
							pro.setProduto(produto);
							for(Ingrediente ing: ingredientes){
								if(ing.getDescricao().equals(pro.getDescricao_ingrediente())){
									pro.setIngrediente(ing);
								}
							}
							listaProdutoIngrediente.add(pro);
						}
						prodDao.salvar(produto);
						
						Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Produto");
				        dialogoInfo.setContentText("Produto salvo com sucesso!");
				        dialogoInfo.showAndWait();
				        
						limparCampos();
					}else{
						//Se o valor do produto não for um valor válido
						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Produto");
				        dialogoInfo.setContentText("O valor do produto deve ser um valor monetário válido!");
				        dialogoInfo.showAndWait();
					}
				}else{
					//Se existir campos nulos deve gerar erro
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Produto");
			        dialogoInfo.setContentText("Existem campos obrigatórios nulos!");
			        dialogoInfo.showAndWait();
				}
			}else{
				//Se não gerou o código do produto deve apresentar um erro
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Produto");
		        dialogoInfo.setContentText("O código do produto deve ser gerado!");
		        dialogoInfo.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Produto");
	        dialogoInfo.setContentText("Houve um erro ao salvar o produto, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	public void limparCampos(){
		/*Deixando os campos do form em branco*/
		codigo.setText("");
		nome.setText("");
		preco.setText("");
		quantidade.setText("");
		comboIng.getSelectionModel().clearSelection();
		data.clear();
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
		        dialogoInfo.setHeaderText("Salvar Produto");
		        dialogoInfo.setContentText("A quantidade deve ser um numeral inteiro!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Produto");
	        dialogoInfo.setContentText("Selecione um ingrediente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	/**
	 * Método para retirar um ingrediente da tabela listView
	 */
	public void retirarIngrediente(){
		ProdutoIngrediente ing = null;
		ing = tabela_ingrediente.getSelectionModel().getSelectedItem();
		if(ing != null){
			data.remove(ing);
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Produto");
	        dialogoInfo.setContentText("Selecione um ingrediente para remover!");
	        dialogoInfo.showAndWait();
		}
	}
	
	/**
	 * Método inicial, que roda ao iniciar a tela
	 */
	@FXML
	protected void initialize(){
		try{
			ingredientes = ingDao.getAllIngredientes();
			List<String> ingredientesNomes = new ArrayList<String>();
			for(Ingrediente ing: ingredientes){
				ingredientesNomes.add(ing.getDescricao());
			}
			comboIng.getItems().setAll(ingredientesNomes);
			
			// Set up the table data
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
	        tabela_ingrediente.setItems(data);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
