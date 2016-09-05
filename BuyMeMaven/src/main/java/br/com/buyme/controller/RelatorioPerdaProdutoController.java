package br.com.buyme.controller;

import java.util.List;

import br.com.buyme.dao.RelatorioPerdaProdutoDAO;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RelatorioPerdaProdutoController {
	
	private Popup popup = new Popup("Relatório de Perda de Produto");
	@FXML private TextField txtLote;
	@FXML private TableView<ProdutoAnalisar> tabela_produto_analisar;
    @FXML private TableColumn<ProdutoAnalisar, String> cProduto;
    @FXML private TableColumn<ProdutoAnalisar, String> cLote;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cQuantEsperada;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cQuantProduzida;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cTotalPerda;
    private ObservableList<ProdutoAnalisar> data;
    private List<ProdutoAnalisar> produtosAnalisar = null;
    @FXML private DatePicker dataInicio,dataFim;
    @FXML private Label lblQuantidadeTotal;
    
    //RESPONSÁVEL PELA CONSULTA DO RELATORIO
    private String lote = null;
    private RelatorioPerdaProdutoDAO rppDao = new RelatorioPerdaProdutoDAO();
    //CONTROLA O TOTAL DE PERDA
    private int tPerda = 0;
    
    
    @FXML
    public void gerarRelatorio(){
    	try{
    		lote = txtLote.getText();
			try{
				produtosAnalisar = rppDao.getRelatorio(lote);
				if(produtosAnalisar != null && !produtosAnalisar.isEmpty()){
					tPerda = 0;
					for(ProdutoAnalisar pa: produtosAnalisar){
						tPerda = tPerda + pa.getQuantidadePerda();
					}
					alimentarTabela(produtosAnalisar,tPerda);
				}else{
					popup.getError("Nenhum registro foi encontrado!");
				}
			}catch(Exception e){
				e.printStackTrace();
				popup.getError("Houve um erro, tente novamente!");
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
    }
    
    @FXML
    public void baixarRelatorio(){
    	String query = rppDao.getQuery(lote);
    	rppDao.gerarRelatorioPdf(query, tPerda, lote);
    	popup.getInformation("Relatorio gerado com sucesso!");
    }
    
    public void alimentarTabela(List<ProdutoAnalisar> pas, int tPerda){
    	cProduto.setCellValueFactory(
            new PropertyValueFactory<ProdutoAnalisar,String>("descricao")
        );
        cLote.setCellValueFactory(
            new PropertyValueFactory<ProdutoAnalisar,String>("lote")
        );
        cQuantEsperada.setCellValueFactory(
                new PropertyValueFactory<ProdutoAnalisar,Integer>("quantidade")
            );
        cQuantProduzida.setCellValueFactory(
                new PropertyValueFactory<ProdutoAnalisar,Integer>("quantidadeProduzida")
            );
        cTotalPerda.setCellValueFactory(
                new PropertyValueFactory<ProdutoAnalisar,Integer>("quantidadePerda")
            );
 
        data = FXCollections.observableArrayList();
        data.addAll(pas);
        tabela_produto_analisar.setItems(data);
        
        lblQuantidadeTotal.setText(tPerda+"");
    }
    
    @FXML
	protected void initialize(){
    	
    }

}
