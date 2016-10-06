package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.dao.ProdutoVendidoDAO;
import br.com.buyme.entity.Cliente;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.entity.ProdutoVendido;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;

@Data
public class VendaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Venda");
	@FXML private ComboBox<String> comboCliente, comboProducao;
	@FXML private Button btnAdicionar,btnRetirar,btnFinalizar;
	@FXML private Label lblValor,totalValor,lblNomeProd;
	@FXML private TextField quantidade,nome,codigoProduto;
	@FXML private CheckBox chkClienteCadastrado;
	@FXML private TableView<ProdutoVendido> tabela_pv;
	@FXML private TableColumn<ProdutoVendido, String> cCliente;
    @FXML private TableColumn<ProdutoVendido, String> cProduto;
    @FXML private TableColumn<ProdutoVendido, Integer> cQuantidade;
    @FXML private TableColumn<ProdutoVendido, Double> cValor;
    private ObservableList<ProdutoVendido> data;
    private ObservableList<ProdutoVendido> dataSave;
    
    //LISTA DE CLIENTES
    private ClienteDAO cliDao = new ClienteDAO();
    private List<Cliente> clientes = null;
    //RELACIONADO AO PRODUTO SELECIONADO
    private Produto p = null;
    private ProdutoDAO pDao = new ProdutoDAO();
    //LISTA DE PRODUTOS PRONTOS E LISTA DE DATAS DO COMBO
    private List<ProdutoPronto> listPp = null;
    private List<ProdutoPronto> listPpDeletarBanco = new ArrayList<ProdutoPronto>();
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    private List<String> listDataProduto = new ArrayList<String>();
    //USADO PARA SALVAR O PRODUTO VENDIDO
    private ProdutoVendidoDAO pvDao = new ProdutoVendidoDAO();
    
    @FXML
	protected void initialize(){
    	btnUsuario.setText(usuario.getLogin());
    	menuPermissoes();
    	try{
    		List<String> nomesProduto = pDao.getNomes();
    		TextFields.bindAutoCompletion(codigoProduto, nomesProduto);
    		clientes = cliDao.getAll();
    		if(clientes != null){
    			List<String> clientesNomes = new ArrayList<String>();
    			for(Cliente cli: clientes){
    				clientesNomes.add(cli.getNome());
    			}
    			comboCliente.getItems().setAll(clientesNomes);
    		}
    		
    		cCliente.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,String>("cliente")
	        );			
    		cProduto.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,String>("produto")
	        );
    		cQuantidade.setCellValueFactory(
    	            new PropertyValueFactory<ProdutoVendido,Integer>("quantidade")
    	        );
	        cValor.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,Double>("valor")
	        );
	 
	        data = FXCollections.observableArrayList();
	        tabela_pv.setItems(data);
	        
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * MÉTODO QUE VERIFICA SE CHECKBOX PARA CLIENTE NÃO CADASTRADO ESTA SELECIONADO
     * CASO ESTEJA, O BOX PARA SELECIONAR O CLIENTE FICA DESATIVADO
     * CASO NÃO ESTEJA, O CAMPO PARA INSERIR O NOME DO CLIENTE FICA DESATIVADO
     */
    @FXML
    public void clienteNaoCadastrado(){
    	if(chkClienteCadastrado.isSelected()){
    		comboCliente.setDisable(true);
    		nome.setDisable(false);
    	}else{
    		comboCliente.setDisable(false);
    		nome.setDisable(true);
    	}
    }
    
    /**
     * MÉTODO QUE BUSCA O PRODUTO PELO CÓDIGO INSERIDO
     * ALIMENTA O COMBOBOX COM AS DATAS DAS PRODUÇOES DO PRODUTO
     * HABILITA O COMBOBOX, CAMPO QUANTIDADE E BOTOES PARA INSERÇÃO E EXCLUSÃO DE ITEMS DA TABELA
     */
    @FXML
    public void buscarProduto(){
    	String nomeProd = codigoProduto.getText();
    	if(nomeProd != null && !nomeProd.isEmpty()){
    		p = pDao.getOneByNome(nomeProd);
    		if(p != null){
    			lblValor.setText(String.valueOf(p.getValor()).replace('.', ','));
    			lblNomeProd.setText(p.getNome());
    			
    			listPp = ppDao.getListByCodigo(Integer.parseInt(p.getCodigo()));
    			for(ProdutoPronto pp: listPp){
    				listDataProduto.add(pp.getLote());
    			}
    			comboProducao.getItems().setAll(listDataProduto);
    			comboProducao.getSelectionModel().select(verificaDataAntiga(listPp).getLote());
    			
    			comboProducao.setDisable(false);
    			quantidade.setDisable(false);
    			btnAdicionar.setDisable(false);
    			btnRetirar.setDisable(false);
    			btnFinalizar.setDisable(false);
    		}else{
    			popup.getError("Produto não encontrado!");
    		}
    	}else{
    		popup.getError("Insira o código para buscar o produto!");
    	}
    }
    
    /**
     * MÉTODO PARA LIMPAR OS CAMPOS DA TELA
     */
    public void limparCampos(){
    	data.clear();
    	dataSave.clear();
    	comboCliente.getSelectionModel().clearSelection();
    	nome.setText("");
    	totalValor.setText("0,00");
    	lblValor.setText("0,00");
    	lblNomeProd.setText("");
    	codigoProduto.setText("");
    	comboCliente.setDisable(false);
    	nome.setDisable(true);
    	comboProducao.setDisable(true);
    	comboProducao.getSelectionModel().clearSelection();
    	quantidade.setText("");
		quantidade.setDisable(true);
		chkClienteCadastrado.setSelected(false);
		btnAdicionar.setDisable(true);
		btnRetirar.setDisable(true);
		btnFinalizar.setDisable(true);
    }
    
    
    /**
     * MÉTODO QUE FINALIZA UMA COMPRA
     * SALVA OS PRODUTOS VENDIDOS, E REMOVE DO ESTOQUE
     */
    @FXML
    public void finalizar(){
    	dataSave = tabela_pv.getItems();
    	if(dataSave != null && !dataSave.isEmpty()){
    		try{
    			for(ProdutoVendido pv: dataSave){
    				pvDao.salvar(pv.getProduto(), pv.getQuantidade(), pv.getCliente(), new Date(), pv.getValor(), pv.getProdutoPronto()); //salvando a venda
    				ppDao.removerQuantidadeById(pv.getProdutoPronto(), pv.getQuantidade());
    			}
    			
    			popup.getInformation("Vendas efetuadas com sucesso!");
    	        
    	        limparCampos();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}else{
    		popup.getError("Não há nenhuma venda!");
    	}
    }
    
    
    /**
     * MÉTODO QUE RETIRA UM PRODUTO VENDIDO DA TABELA
     */
    @FXML
    public void retirar(){
    	ProdutoVendido pv = null;
		pv = tabela_pv.getSelectionModel().getSelectedItem();
		if(pv != null){
			for(ProdutoPronto pp: listPp){
				if(pp.getId() == pv.getProdutoPronto().getId()){
					if(pp.getQuantidade() == 0){
						comboProducao.getItems().add(pp.getLote());
					}
					pp.setQuantidade(pp.getQuantidade() + pv.getQuantidade());
					break;
				}
			}
			data.remove(pv);
			valorTotal(pv.getValor(), true);
		}else{
			popup.getError("Selecione uma venda para remover!");
		}
    }
    
    
    /**
     * MÉTODO QUE MONTA UM OBJETO PRODUTO VENDIDO E ADICIONA NA TABELA
     */
    @FXML
	public void adicionar(){
    	try{
    		ProdutoVendido pv = new ProdutoVendido();
    		String nomeCliente = nomeCliente();
    		if(nomeCliente != null && !nomeCliente.isEmpty()){
    			pv.setCliente(nomeCliente);
    			if(p != null){
    				String dataCombo = comboProducao.getSelectionModel().selectedItemProperty().getValue();
    				if(dataCombo != null && !dataCombo.isEmpty()){
    					String strQuantidade = quantidade.getText();
    					if(strQuantidade != null && !strQuantidade.isEmpty() && Utils.isNumber(strQuantidade)){
    						int intQuantidade = Integer.parseInt(strQuantidade);
    						pv.setProduto(p.getNome());
    						for(ProdutoPronto pp: listPp){
    							if((pp.getLote()).equals(dataCombo)){
    								if(pp.getQuantidade() >= intQuantidade){
    									pv.setProdutoPronto(pp);
        								pv.setValor(intQuantidade * p.getValor());
        								pv.setQuantidade(intQuantidade);
        								pp.setQuantidade(pp.getQuantidade()-intQuantidade);
        								if(pp.getQuantidade() == 0){
        									comboProducao.getItems().remove(dataCombo);
        								}
        								data.add(pv);
        								break;
    								}else{
    									popup.getError("A quantidade de venda é maior do que a quantidade em estoque: "+ pp.getQuantidade());
    								}
    							}
    						}
    						valorTotal(pv.getValor(), false);
    					}else{
    						popup.getError("O campo quantidade não pode ser vazio, e deve ser um número inteiro!");
    					}
    				}else{
    					popup.getError("Selecione a produção!");
    				}
    			}else{
    				popup.getError("O produto não foi encontrado!");
    			}
    		}else{
    			popup.getError("Selecione um cliente, ou insira o nome do cliente não cadastrado!");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
	}
    
    /**
     * MÉTODO QUE CAPTURA O NOME DO CLIENTE QUE ESTA FAZENDO A COMPRA
     * @return nomeDoCliente
     */
    public String nomeCliente(){
    	String nomeCliente = "";
    	if(chkClienteCadastrado.isSelected()){
			if(nome.getText() != null && !nome.getText().isEmpty()){
				nomeCliente = nome.getText();
			}else{
				popup.getError("Se o cliente não é cadastrado, é necessário o nome para a venda!");
			}
		}else{
			if(comboCliente.getSelectionModel().selectedItemProperty().getValue() != null && 
				!comboCliente.getSelectionModel().selectedItemProperty().getValue().isEmpty()){
				nomeCliente = comboCliente.getSelectionModel().selectedItemProperty().getValue();
			}else{
				popup.getError("É necessário selecionar um cliente para a venda!");
			}
		}
    	return nomeCliente;
    }
    
    /**
     * MÉTODO QUE MANIPULA O VALOR TOTAL DA COMPRA
     * @param valor
     * @param retirar
     */
    public void valorTotal(double valor, boolean retirar){
    	String v = totalValor.getText().replace(',', '.');
    	double valorTotal = Double.parseDouble(v);
    	if(retirar){
    		valorTotal = valorTotal - valor;
    		totalValor.setText(String.valueOf(valorTotal).replace('.', ','));
    	}else{
    		valorTotal = valorTotal + valor;
    		totalValor.setText(String.valueOf(valorTotal).replace('.', ','));
    	}
    }
    
    public ProdutoPronto verificaDataAntiga(List<ProdutoPronto> pps){
    	Date data = null;
    	ProdutoPronto ppRetorno = null;
    	for(ProdutoPronto pp: pps){
    		if(data == null || pp.getDataValidade().before(data)){
    			data = pp.getDataValidade();
    			ppRetorno = pp;
    		}
    	}
    	return ppRetorno;
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
