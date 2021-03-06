package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.dao.RelatorioMotivoPerdaLoteDAO;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.RelatorioMotivoPerdaLote;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RelatorioMotivoPerdaLoteController {
	
	@FXML private Parent root;
	
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Relat�rio Motivo Perda por Lote");
	@FXML TextField txtLote,txtMotivoPerda;
	@FXML private TableView<RelatorioMotivoPerdaLote> tabela_motivo_perda;
    @FXML private TableColumn<RelatorioMotivoPerdaLote, String> cLote;
    @FXML private TableColumn<RelatorioMotivoPerdaLote, String> cProduto;
    @FXML private TableColumn<RelatorioMotivoPerdaLote, String> cMotivoPerda;
    @FXML private TableColumn<RelatorioMotivoPerdaLote, Integer> cQuantPerda;
    private ObservableList<RelatorioMotivoPerdaLote> data;
	
	private String lote = null;
	private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
	private RelatorioMotivoPerdaLoteDAO rmpDao = new RelatorioMotivoPerdaLoteDAO();
	
	@FXML
	public void gerarRelatorio(){
		try{
    		lote = txtLote.getText();
			try{
				List<RelatorioMotivoPerdaLote> conteudoTabela = rmpDao.getRelatorio(lote);
				if(conteudoTabela != null && !conteudoTabela.isEmpty()){
					alimentarTabela(conteudoTabela);
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
	
	public void alimentarTabela(List<RelatorioMotivoPerdaLote> conteudoTabela) {
		cLote.setCellValueFactory(
            new PropertyValueFactory<RelatorioMotivoPerdaLote,String>("lote")
        );
        cProduto.setCellValueFactory(
            new PropertyValueFactory<RelatorioMotivoPerdaLote,String>("produto")
        );
        cMotivoPerda.setCellValueFactory(
                new PropertyValueFactory<RelatorioMotivoPerdaLote,String>("motivoPerda")
            );
        cQuantPerda.setCellValueFactory(
                new PropertyValueFactory<RelatorioMotivoPerdaLote,Integer>("quantidade")
            );
 
        data = FXCollections.observableArrayList();
        data.addAll(conteudoTabela);
        tabela_motivo_perda.setItems(data);
	}

	@FXML
	public void baixarRelatorio(){
		String query = rmpDao.getQuery(lote);
		rmpDao.gerarRelatorioPdf(query);
    	popup.getInformation("Relatorio gerado com sucesso!");
	}
	
	@FXML
	protected void initialize(){
		try{
			btnUsuario.setText(usuario.getLogin());
			menuPermissoes();
			List<String> lotes = paDao.getAllLoteAnalisado();
			TextFields.bindAutoCompletion(txtLote, lotes);
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
		
	}
	
	//M�TODOS MENU BAR
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
