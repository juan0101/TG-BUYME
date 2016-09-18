package br.com.buyme.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.entity.ProdutoIngrediente;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProduzirFormaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();

	private Popup popup = new Popup("Produção de forma");
	@FXML private Button btnProduzir;
	@FXML private TextField quantidade,lote;
	@FXML private ComboBox<String> comboForma;
	@FXML private TableView<ProdutoIngrediente> tabela_ingrediente;
	@FXML private TableColumn<ProdutoIngrediente, String> coluna_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, Double> coluna_quantidade;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_medida;
    @FXML private DatePicker dataValidade;
    private ObservableList<ProdutoIngrediente> data;
    private Integer quantidadeForma = null;
    
    private List<Forma> formas = null;
    private FormaDAO formaDao = new FormaDAO();
    private Forma forma = null;
    private Produto produto = null;
    private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
    
    @FXML
    public void gerarLote(){
    	Random randomGenerator = new Random();
    	ProdutoAnalisar pa = null;
    	try{
			while(true){
				int codLote = randomGenerator.nextInt(10000);
				pa = paDao.getByLote("L"+codLote);
				if(pa != null){
					continue;
				}else{
					lote.setText("L"+codLote);
					break;
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Erro ao gerar Lote! Tente novamente.");
    	}
    }
    
	@FXML
	public void gerarDados(){
		String cForma = comboForma.getSelectionModel().selectedItemProperty().getValue();
		String quantForma = quantidade.getText();
		if(cForma != null && !cForma.isEmpty()){
			if(quantForma != null && !quantForma.isEmpty()){
				if(Utils.isNumber(quantForma)){
					quantidadeForma = Integer.parseInt(quantForma);
					for(Forma f: formas){
						if(f.getDescricao().equals(cForma)){
							forma = f;
							break;
						}
					}
					if(forma != null){
						produto = forma.getProduto();
						if(produto != null){
							List<ProdutoIngrediente> listaPi = produto.getProduto_ingrediente();
							List<ProdutoIngrediente> listaPiTotal = new ArrayList<ProdutoIngrediente>();
							for(ProdutoIngrediente pi: listaPi){
								ProdutoIngrediente auxPi = new ProdutoIngrediente(pi);
								auxPi.setQuantidade((pi.getQuantidade() * forma.getQuantidade()) * quantidadeForma);
								if(auxPi.getQuantidade() >= 1000){
									auxPi.setQuantidade(auxPi.getQuantidade()/1000);
									auxPi.setGrama_quilo("Quilo");
								}
								listaPiTotal.add(auxPi);
							}
							data.setAll(listaPiTotal);
						}else{
							popup.getError("Não há produto para essa forma!");
						}
					}else{
						popup.getError("Forma não encontrada!");
					}
				}else{
					popup.getError("A quantidade deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("O campo quantidade não pode ficar em branco!");
			}
		}else{
			popup.getError("Seleciona uma forma para gerar os dados!");
		}
	}
	
	public void limparCampos(){
		comboForma.getSelectionModel().clearSelection();
		quantidade.setText("");
		data.clear();
	}
	
	/**
	 * Método que faz a produção dos produtos para análise
	 */
	
	@FXML
	public void produzir(){
		LocalDate stringData = dataValidade.getValue();
		String codLote = lote.getText();
		if(stringData != null){
			if(codLote != null && !codLote.isEmpty()){
				try{
					Date data_validade = Utils.getLocalDateToDate(stringData);
					Date data_fabricacao = new Date();
					int numeroDeProdutos = forma.getQuantidade() * quantidadeForma;
					
					paDao.salvar(numeroDeProdutos, forma.getProduto().getNome(), data_fabricacao, data_validade, forma.getProduto().getValor(), false, Integer.parseInt(produto.getCodigo()),codLote);
					
					popup.getInformation("A produção foi enviada para análise!");
			        
			        limparCampos();
			        
				}catch(Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro, tente novamente!");
				}
			}else{
				popup.getError("Gere um código de lote para esta produção!");
			}
		}else{
			popup.getError("Selecione a data de validade da produção!");
		}
	}
	
	/**
	 * Método inicial, que roda ao iniciar a tela
	 */
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		menuPermissoes();
		try{
			formas = formaDao.getAll();
			if(formas != null){
				List<String> formasNomes = new ArrayList<String>();
				for(Forma f: formas){
					formasNomes.add(f.getDescricao());
				}
				comboForma.getItems().setAll(formasNomes);
			}
			
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
			popup.getError("Ocorreu um erro durante o processo, tente novamente!");
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
