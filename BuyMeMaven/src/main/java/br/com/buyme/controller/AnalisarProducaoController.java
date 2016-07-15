package br.com.buyme.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.entity.ProdutoAnalisar;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AnalisarProducaoController {
	
	@FXML private TableView<ProdutoAnalisar> tabela_analise;
	@FXML private TableColumn<ProdutoAnalisar, String> cDescricao;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cQuantidade;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorUni;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorTotal;
    @FXML private TableColumn<ProdutoAnalisar, Date> cDataFabricacao;
    @FXML private TableColumn<ProdutoAnalisar, Date> cDataValidade;
    private ObservableList<ProdutoAnalisar> data;
    private List<ProdutoAnalisar> pas = null;
    @FXML private Label lblProduto,lblProd,lblQuantEsperada,lblQuantValor,lblQuantProduzida;
    @FXML private TextField quantidadeProduzida;
    @FXML private Button btnFinalizar;
    
    private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
    private ProdutoAnalisar pa = null;
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    
    @FXML
    public void analisar(){
    	pa = tabela_analise.getSelectionModel().getSelectedItem();
    	if(pa != null){
    		lblProd.setText(pa.getDescricao());
    		lblProd.setVisible(true);
    		lblQuantValor.setText(pa.getQuantidade()+"");
    		lblQuantValor.setVisible(true);
    		btnFinalizar.setDisable(false);
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Análise de Produção");
	        dialogoInfo.setContentText("Selecione uma produção para analisar!");
	        dialogoInfo.showAndWait();
    	}
    }
    
    @FXML
    public void excluir(){
    	pa = tabela_analise.getSelectionModel().getSelectedItem();
    	if(pa != null){
    		try{
    			data.remove(pa);
    			paDao.excluir(pa.getId());
    			atualizarListProducao();
    		}catch(Exception e){
    			
    		}
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Análise de Produção");
	        dialogoInfo.setContentText("Selecione uma produção para excluir!");
	        dialogoInfo.showAndWait();
    	}
    }
    
    public int gerarCodigo(){
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(10000);
	}
    
    public void limparCampos(){
    	quantidadeProduzida.setText("");
    	lblProd.setText("");
		lblProd.setVisible(false);
		lblQuantValor.setText("");
		lblQuantValor.setVisible(false);
		btnFinalizar.setDisable(true);
    }
    
    @FXML
    public void finalizar(){
    	String qtdProd = quantidadeProduzida.getText();
    	if(qtdProd != null && !qtdProd.isEmpty()){
    		if(Utils.isNumber(qtdProd)){
    			try{
    				int qtdProdReal = Integer.parseInt(qtdProd);
    				paDao.finalizarAnalise(pa.getId(), qtdProdReal);
    				
    				for(int i=0; i<qtdProdReal; i++){
    					ppDao.salvar(gerarCodigo()+"", pa.getDescricao(), pa.getData_fabricacao(), pa.getData_validade(), pa.getValor());					
    				}
    				
    				Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
        	        dialogoInfo.setTitle("BuyMe");
        	        dialogoInfo.setHeaderText("Análise de Produção");
        	        dialogoInfo.setContentText("Produção gerada com sucesso!");
        	        dialogoInfo.showAndWait();
    				
    				atualizarListProducao();
    			}catch (Exception e){
    				e.printStackTrace();
    				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
        	        dialogoInfo.setTitle("BuyMe");
        	        dialogoInfo.setHeaderText("Análise de Produção");
        	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
        	        dialogoInfo.showAndWait();
    			}
    		}else{
    			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    	        dialogoInfo.setTitle("BuyMe");
    	        dialogoInfo.setHeaderText("Análise de Produção");
    	        dialogoInfo.setContentText("Quantidade deve ser um numeral inteiro!");
    	        dialogoInfo.showAndWait();
    		}
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Análise de Produção");
	        dialogoInfo.setContentText("Informe a quantidade que foi produzida do produto!");
	        dialogoInfo.showAndWait();
    	}
    	
    }
    
    public void atualizarListProducao(){
    	pas.clear();
    	data.clear();
    	pas = paDao.getAllAnalisado(false);
    	if(pas != null){
    		data.setAll(pas);
    	}
    }
    
    @FXML
	protected void initialize(){
    	try{
    		pas = paDao.getAllAnalisado(false);
    		if(pas != null){
    			cDescricao.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,String>("descricao")
		        );
    			cQuantidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Integer>("quantidade")
		        );
    			cValorUni.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Double>("valor")
		        );
    			cValorTotal.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Double>("valorTotal")
		        );
    			cDataFabricacao.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Date>("data_fabricacao")
		        );
    			cDataValidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Date>("data_validade")
		        );
		 
		        data = FXCollections.observableArrayList();
		        data.addAll(pas);
		        tabela_analise.setItems(data);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
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
