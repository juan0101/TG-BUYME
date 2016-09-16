package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.RelatorioPerdaProdutoDAO;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RelatorioPerdaProdutoController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
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
    	btnUsuario.setText(usuario.getLogin());
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
