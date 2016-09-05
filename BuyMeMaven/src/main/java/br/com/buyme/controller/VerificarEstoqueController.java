package br.com.buyme.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VerificarEstoqueController {
	
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
    

}
