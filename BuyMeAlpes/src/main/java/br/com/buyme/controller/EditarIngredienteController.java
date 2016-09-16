package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
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
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;

@Data
public class EditarIngredienteController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();

	private Popup popup = new Popup("Editar Ingrediente");
	@FXML private TableView<Ingrediente> tabela_ingrediente;
    @FXML private TableColumn<Ingrediente, String> cCodigo;
    @FXML private TableColumn<Ingrediente, String> cNome;
    private ObservableList<Ingrediente> data;
    private List<Ingrediente> ingredientes = null;
	@FXML private TextField txtCodigo,txtDescricao;
	@FXML private Button btnVoltar,btnAtualizar;
	
	private IngredienteDAO ingDao = new IngredienteDAO();
	private Ingrediente ingrediente = null;
	
	@FXML
	protected void initialize(){
		btnUsuario.setText(usuario.getLogin());
		try{
			ingredientes = ingDao.getAllIngredientes();
			if(ingredientes != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<Ingrediente,String>("codigo")
						);
				cNome.setCellValueFactory(
						new PropertyValueFactory<Ingrediente,String>("descricao")
						);
				data = FXCollections.observableArrayList();
				data.addAll(ingredientes);
				tabela_ingrediente.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro ao carregar os ingredientes, tente novamente!");
		}
	}
	
	@FXML
	public void editar(){
		try{
			ingrediente = tabela_ingrediente.getSelectionModel().getSelectedItem();
			if(ingrediente != null){
				txtCodigo.setText(ingrediente.getCodigo());
				txtDescricao.setText(ingrediente.getDescricao());
				btnAtualizar.setDisable(false);
			}else{
				popup.getError("Selecione um ingrediente para editar!");
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void excluir(){
		try{
			ingrediente = tabela_ingrediente.getSelectionModel().getSelectedItem();
			if(ingrediente != null){
				try{
					ingDao.excluir(ingrediente.getId());
					data.remove(ingrediente);
					
					popup.getInformation("Ingrediente excluido com sucesso!");
			        
					atualizarListIng();
					
				}catch (Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro ao excluir, tente novamente!");
				}
			}else{
				popup.getError("Selecione um ingrediente para editar!");
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	public void atualizarListIng(){
		ingredientes.clear();
		ingredientes = ingDao.getAllIngredientes();
		data.clear();
		if(ingredientes != null){
			data.setAll(ingredientes);
		}
	}
	
 	@FXML
	public void atualizarIngrediente(){
		try{
			String codIng = txtCodigo.getText();
			String descIng = txtDescricao.getText();
			if(codIng != null && !codIng.isEmpty() && descIng != null && !descIng.isEmpty()){
				try{
					ingDao.editarIngrediente(ingrediente.getId(), descIng);
					
					popup.getInformation("Ingrediente editado com sucesso!");
			        
			        atualizarListIng();
			        
			        txtCodigo.setText("");
			        txtDescricao.setText("");
			        btnAtualizar.setDisable(true);
				}catch(Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro ao salvar! Tente novamente.");
				}
			}else{
				popup.getError("Existem campos obrigatórios em branco.");
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro durante o processo! Tente novamente.");
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
	
}
