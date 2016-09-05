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
import br.com.buyme.entity.Produto;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GerarEncomendaController {
	
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
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Encomenda");
		        dialogoInfo.setContentText("A quantidade deve ser um numeral inteiro!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Encomenda");
	        dialogoInfo.setContentText("Selecione um cliente, um produto e adicione a quantidade!");
	        dialogoInfo.showAndWait();
		}
	}
	
    @FXML
    public void retirar(){
    	Encomenda e = null;
		e = tabela_encomenda.getSelectionModel().getSelectedItem();
		if(e != null){
			data.remove(e);
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Encomenda");
	        dialogoInfo.setContentText("Selecione uma encomenda para remover!");
	        dialogoInfo.showAndWait();
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
    		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Encomenda");
	        dialogoInfo.setContentText("As encomendas foram salvas com sucesso!");
	        dialogoInfo.showAndWait();
	        
	        limparCampos();
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Encomenda");
	        dialogoInfo.setContentText("Tabela de encomendas esta vazia!");
	        dialogoInfo.showAndWait();
    	}
    }
    
    public void limparCampos(){
    	quantidade.setText("");
		comboCliente.getSelectionModel().clearSelection();
		comboProd.getSelectionModel().clearSelection();
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
