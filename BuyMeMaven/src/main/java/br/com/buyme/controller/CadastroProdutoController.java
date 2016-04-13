package br.com.buyme.controller;

import java.util.List;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.ProdutoIngrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastroProdutoController {
	
	@FXML private ComboBox<String> comboIng;
	@FXML private TableView<ProdutoIngrediente> tabela_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, Double> coluna_quantidade;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_medida;
    @FXML private TextField codigo;
    @FXML private TextField nome;
    @FXML private TextField preco;
    @FXML private TextField quantidade;
    @FXML private RadioButton grama;
    @FXML private RadioButton quilo;
    private ObservableList<ProdutoIngrediente> data;

	@FXML
	public void salvarProduto(){
		System.out.println(codigo.getText());
		System.out.println(nome.getText());
		System.out.println(preco.getText());
		System.out.println(quantidade.getText());
		if(grama.isSelected()){
			System.out.println(grama.getText());
		}else if(quilo.isSelected()){
			System.out.println(quilo.getText());
		}
		System.out.println(comboIng.getSelectionModel().selectedItemProperty().getValue());
	}
	
	@FXML
	public void adicionarIngrediente(){
		ProdutoIngrediente pi = new ProdutoIngrediente();
		pi.setIngrediente(comboIng.getSelectionModel().selectedItemProperty().getValue());
		pi.setQuantidade(Double.parseDouble(quantidade.getText()));
		pi.setGrama_quilo(grama.isSelected()? grama.getText():quilo.getText());
        data.add(pi);
	}
	
	@FXML
	protected void initialize(){
		try{
			IngredienteDAO ingredienteDao = new IngredienteDAO();
			List<String> ingredientes = ingredienteDao.getNomes();
			System.out.println(ingredientes.size());
			comboIng.getItems().setAll(ingredientes);
			
			// Set up the table data
	        coluna_ingrediente.setCellValueFactory(
	            new PropertyValueFactory<ProdutoIngrediente,String>("ingrediente")
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
