package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;

public class CadastroFormaController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("Salvar Forma");
	@FXML private TextField txtCodigo,txtDescricao,txtQuantidade;
	@FXML private ComboBox<String> comboProd;
	private List<Produto> produtos;
	private boolean gerouCod = false;
	
	private ProdutoDAO prodDao = new ProdutoDAO();
	private FormaDAO formaDao = new FormaDAO();
	
	/**
	 * Método que roda quando inicializa a tela
	 * 
	 */
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		try{
			produtos = prodDao.getAll();
			List<String> produtoNome = new ArrayList<String>();
			for(Produto prod: produtos){
				produtoNome.add(prod.getNome());
			}
			comboProd.getItems().setAll(produtoNome);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
     * Método que gera o código de um produto
     */
    @FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Forma f = null;
			while(true){
				int codProduto = randomGenerator.nextInt(10000);
				f = formaDao.getOneByCodigo(codProduto+"");
				if(f != null){
					continue;
				}else{
					txtCodigo.setText(codProduto+"");
					gerouCod = true;
					break;
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para cadastro de uma nova forma
	 * 
	 */
	@FXML
	public void finalizar(){
		try{
			if(gerouCod){
				String codForma = txtCodigo.getText();
				String descForma = txtDescricao.getText();
				String quantProd = txtQuantidade.getText();
				String nomeProd = comboProd.getSelectionModel().selectedItemProperty().getValue();
				if(codForma != null && !codForma.isEmpty() && descForma != null && !descForma.isEmpty() &&
						quantProd != null && !quantProd.isEmpty() && nomeProd != null && !nomeProd.isEmpty()){
					if(Utils.isNumber(quantProd)){
						Forma forma = new Forma();
						List<Forma> listaForma = new ArrayList<Forma>();
						forma.setCodigo(codForma);
						forma.setDescricao(descForma);
						forma.setQuantidade(Integer.parseInt(quantProd));
						for(Produto pro: produtos){
							if(pro.getNome().equals(nomeProd)){
								forma.setProduto(pro);
								forma.setNomeProduto(pro.getNome());
								listaForma.add(forma);
								pro.setForma(listaForma); //Setando a lista de forma no produto
								prodDao.updateSalvarForma(pro); //Update no produto com a lista de forma
								
								popup.getInformation("Forma salva com sucesso!");
								
								limparCampos();
							}
						}
					}else{
						popup.getError("O campo quantidade deve ser um número válido!");
					}
				}else{
					popup.getError("Existem campos obrigatórios nulos!");
				}
			}else{
				popup.getError("O código da forma deve ser gerado!");
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	public void limparCampos(){
		/*Deixando os campos do form em branco*/
		txtCodigo.setText("");
		txtDescricao.setText("");
		txtQuantidade.setText("");
		comboProd.getSelectionModel().clearSelection();
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
