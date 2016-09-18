package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.buyme.dao.EncomendaDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.dao.ProdutoVendidoDAO;
import br.com.buyme.entity.Encomenda;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class AtenderEncomendaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Atender Encomenda");
	@FXML private Button btnFinalizar;
	@FXML private TextField cliente,produto,quantidade;
	//ALIMENTA A TABELA PRINCIPAL DE ENCOMENDAS
	@FXML private TableView<Encomenda> tabela_encomenda;
    @FXML private TableColumn<Encomenda, String> cCodigo;
	@FXML private TableColumn<Encomenda, String> cCliente;
    @FXML private TableColumn<Encomenda, String> cProduto;
    @FXML private TableColumn<Encomenda, Integer> cQuantidade;
    @FXML private TableColumn<Encomenda, Double> cValor;
    private ObservableList<Encomenda> data;
    private List<Encomenda> encomendas = null;
    //ALIMENTA A TABELA DE PRODUTOS EM ESTOQUE
  	@FXML private TableView<ProdutoPronto> tabela_produto_estoque;
    @FXML private TableColumn<ProdutoPronto, String> cCodigoEstoque;
  	@FXML private TableColumn<ProdutoPronto, String> cProdutoEstoque;
    @FXML private TableColumn<ProdutoPronto, Date> cDFabricacaoEstoque;
    @FXML private TableColumn<ProdutoPronto, Date> cDValidadeEstoque;
    @FXML private TableColumn<ProdutoPronto, Integer> cQuantidadeEstoque;
    private ObservableList<ProdutoPronto> dataEstoque;
    private List<ProdutoPronto> produtosEstoque = new ArrayList<ProdutoPronto>();
    //ALIMENTA A TABELA DE PRODUTOS ENCOMENDA
  	@FXML private TableView<ProdutoPronto> tabela_produto_encomenda;
    @FXML private TableColumn<ProdutoPronto, String> cCodigoEncomenda;
  	@FXML private TableColumn<ProdutoPronto, String> cProdutoEncomenda;
    @FXML private TableColumn<ProdutoPronto, Date> cDFabricacaoEncomenda;
    @FXML private TableColumn<ProdutoPronto, Date> cDValidadeEncomenda;
    @FXML private TableColumn<ProdutoPronto, Integer> cQuantidadeEncomenda;
    private ObservableList<ProdutoPronto> dataEncomenda;
    
    private EncomendaDAO encDao = new EncomendaDAO();
    private Encomenda encomenda = null;
    private ProdutoPronto prodEncomenda = null;
    
    //PRODUTO DA ENCOMENDA
    private Produto p = null;
    //VARIAVEL QUE VERIFICA SE A ENCOMENDA JA FOI ATENDIDA
    private boolean finalizado = false;
    //QUANTIDADE TOTAL DE ENCOMENDA
    private int quantidadeTotalEnc = 0;
    //SALVA OS PRODUTOS VENDIDOS
    private ProdutoVendidoDAO pvDao = new ProdutoVendidoDAO();
    //RESPONSAVEL POR CONTROLAR O ESTOQUE
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	menuPermissoes();
    	try{
    		encomendas = encDao.getAll();
    		if(encomendas != null){
    			cCodigo.setCellValueFactory(
		            new PropertyValueFactory<Encomenda,String>("codigo")
		        );
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
		        data.addAll(encomendas);
		        tabela_encomenda.setItems(data);
		        
		      //ALIMENTANDO TABELA DE ESTOQUE
    			cCodigoEstoque.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,String>("codigo")
		        );
    			cProdutoEstoque.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,String>("descricao")
		        );
    			cDFabricacaoEstoque.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Date>("dataFabricacao")
		        );
    			cDValidadeEstoque.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Date>("dataValidade")
		        );
    			cQuantidadeEstoque.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Integer>("quantidade")
		        );
		 
    			dataEstoque = FXCollections.observableArrayList();
    			
		        tabela_produto_estoque.setItems(dataEstoque);
		        
		      //ALIMENTANDO TABELA DE ENCOMENDA
    			cCodigoEncomenda.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,String>("codigo")
		        );
    			cProdutoEncomenda.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,String>("descricao")
		        );
    			cDFabricacaoEncomenda.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Date>("dataFabricacao")
		        );
    			cDValidadeEncomenda.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Date>("dataValidade")
		        );
    			cQuantidadeEncomenda.setCellValueFactory(
		            new PropertyValueFactory<ProdutoPronto,Integer>("quantidade")
		        );
		 
    			dataEncomenda = FXCollections.observableArrayList();
    			tabela_produto_encomenda.setItems(dataEncomenda);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * MÉTODOS PARA LIMPAR ATUALIZAR A TABELA DE ESTOQUE
     */
    public void atualizarListEstoque(){
    	dataEstoque.clear();
		dataEstoque.addAll(produtosEstoque);
    }
    
    /**
     * MÉTODO QUE REMOVE O PRODUTO PRONTO DA TABELA DE ATENDIMENTO
     * E RETORNA PARA ESTOQUE
     */
    @FXML
    public void retirarEncomenda(){
    	prodEncomenda = tabela_produto_encomenda.getSelectionModel().getSelectedItem();
    	if(prodEncomenda != null){
    		for(ProdutoPronto pp: produtosEstoque){
    			if(pp.getId() == prodEncomenda.getId()){
    				pp.setQuantidade(pp.getQuantidade() + prodEncomenda.getQuantidade());
    			}
    		}
    		quantidadeTotalEnc = quantidadeTotalEnc + prodEncomenda.getQuantidade();
    		dataEncomenda.remove(prodEncomenda);
    		atualizarListEstoque();
    		finalizado = false;
    	}else{
    		popup.getError("Seleciona um produto para retirar da encomenda!");
    	}
    }
    
    /**
     * MÉTODO QUE ADICIONA O PRODUTO PRONTO NA TABELA DE ATENDIMENTO
     */
    public void adicionaAtendimento(ProdutoPronto pp, int quantidade){
    	ProdutoPronto auxPp = new ProdutoPronto(pp);
    	auxPp.setQuantidade(quantidade);
    	dataEncomenda.add(auxPp);
    }
    
    /**
     * MÉTODO ACIONADO AO SELECIONAR UMA PRODUÇÃO PARA ATENDER A ENCOMENDA
     */
    @FXML
    public void adicionarEncomenda(){
    	if(!finalizado){
    		boolean atende = true;
    		prodEncomenda = tabela_produto_estoque.getSelectionModel().getSelectedItem();
    		Integer quantAtender = telaQuantidade(); //ABRE A TELA PARA INSERIR A QUANTIDADE
    		if(quantAtender != null && quantAtender != 0){
    			if(quantAtender <= quantidadeTotalEnc){
    				for(ProdutoPronto pp: produtosEstoque){
    					if(pp.getId() == prodEncomenda.getId()){
    						if((pp.getQuantidade() - quantAtender) >= 0){
    							pp.setQuantidade(pp.getQuantidade() - quantAtender);
    							atende = true;
    						}else{
    							atende = false;
    						}
    						break;
    					}
    				}
    				if(atende){
    					atualizarListEstoque();
        				
        				adicionaAtendimento(prodEncomenda, quantAtender);
        				
        				quantidadeTotalEnc = quantidadeTotalEnc - quantAtender;
        				if(quantidadeTotalEnc == 0){
        					finalizado = true;
        				}
    				}else{
    					popup.getError("A quantidade digitada é maior que a quantidade em estoque!");
    				}
    				
    			}else{
    				popup.getError("A quantidade digitada é maior que a encomenda!");
    			}
    		}
    	}else{
    		popup.getError("A encomenda já foi atendida!");
    	}
    }
    
    /**
     * MÉTODO ACIONADO PELO BUTTON ATENDER
     * BUSCA OS PRODUTOS PRONTOS EM ESTOQUE, 
     * E ALIMENTA OS TEXTFIELD COM OS DADOS DA ENCOMENDA
     */
    @FXML
    public void atender(){
    	limparDadosEncomenda();
    	encomenda = tabela_encomenda.getSelectionModel().getSelectedItem();
    	if(encomenda != null){
    		btnFinalizar.setDisable(false);
    		p = encomenda.getProduto();
    		if(p != null){
    			produtosEstoque = ppDao.getListByCodigo(Integer.parseInt(p.getCodigo()));
        		if(produtosEstoque != null && !produtosEstoque.isEmpty()){
        			cliente.setText(encomenda.getDescCliente());
        			produto.setText(encomenda.getDescProduto());
        			quantidade.setText(encomenda.getQuantidade()+"");
        			quantidadeTotalEnc = encomenda.getQuantidade();

        			atualizarListEstoque();
        			
        			finalizado = false;
        		}else{
        			popup.getError("Não foi encontrado nenhum produto desejado na encomenda!");
        		}
    		}else{
    			popup.getError("O produto pedido na encomenda não foi encontrado!");
    		}
    	}else{
    		popup.getError("Selecione uma encomenda para atender!");
    	}
    }
    
    /**
     * MÉTODO QUE EXCLUI UMA ENCOMENDA SEM ATENDE-LA
     */
    @FXML
    public void excluir(){
    	encomenda = tabela_encomenda.getSelectionModel().getSelectedItem();
    	if(encomenda != null){
    		try{
    			encomendas.remove(encomenda);
    			encDao.removerById(encomenda.getId());
    			
    			popup.getInformation("Encomenda removida com sucesso!");
    	        
    	        atualizarListEncomenda();
    	        
    		}catch(Exception e){
    			e.printStackTrace();
    			popup.getError("Houve um erro, tente novamente!");
    		}
    	}else{
    		popup.getError("Selecione uma encomenda para atender!");
    	}
    }
    
    @FXML
    public void finalizar(){
    	List<ProdutoPronto> produtosVendidos = tabela_produto_encomenda.getItems();
    	if(produtosVendidos != null && !produtosVendidos.isEmpty()){
    		try{
    			for(ProdutoPronto pp: produtosVendidos){
        			pvDao.salvar(produto.getText(), pp.getQuantidade(), cliente.getText(), new Date(), encomenda.getValor(), pp );
        			ppDao.removerQuantidadeById(pp, pp.getQuantidade());
        		}
    			
    			if(quantidadeTotalEnc == 0){
    				encDao.desativarById(encomenda.getId());
    			}else{
    				encDao.diminuirQuantidadeById(encomenda.getId(), quantidadeTotalEnc);
    			}
    			
    			popup.getInformation("Encomenda atendida!");
    	        
    	        limparCampos();
    	        atualizarListEncomenda();
    		}catch(Exception e){
    			e.printStackTrace();
    			popup.getError("Houve um erro, tente novamente!");
    		}
    		
    	}else{
    		popup.getError("Adicione produtos para atender ao pedido!");
    	}
    }
    
    
    /**
     * MÉTODO PARA LIMPAR AS TABELAS DE ESTOQUE E ENCOMENDA
     */
    public void limparCampos(){
    	produtosEstoque.clear();
    	dataEstoque.clear();
    	dataEncomenda.clear();
    	cliente.setText("");
    	produto.setText("");
    	quantidade.setText("");
    	btnFinalizar.setDisable(true);
    }
    
    /**
     * MÉTODO PARA LIMPAR OS CAMPOS E A TABELA DE ENCOMENDA
     */
    public void limparDadosEncomenda(){
    	cliente.setText("");
    	produto.setText("");
    	quantidade.setText("");
    	dataEncomenda.clear();
    }
    
    public void atualizarListEncomenda(){
    	encomendas.clear();
    	data.clear();
    	try{
    		encomendas = encDao.getAll();
    		if(encomendas != null && !encomendas.isEmpty()){
    			data.setAll(encomendas);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * MÉTODO QUE ABRE A TELA PARA INSERIR A QUANTIDADE DE PRODUTOS PARA ATENDER A ENCOMENDA
     * @return Quantidade de produtos para atender a encomenda
     */
    public Integer telaQuantidade(){
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("BuyMe");
    	dialog.setHeaderText("Atender encomenda");
    	dialog.setContentText("Digite a quantidade que deseja atender com essa produção: ");

    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		if(Utils.isNumber(result.get())){
    			return Integer.parseInt(result.get());
    		}else{
    			popup.getError("A quantidade deve ser um número!");
    	        
    	        return 0;
    		}
    	}else{
    		return 0;
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
