package br.com.buyme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="produto_ingrediente")
public class ProdutoIngrediente{
	
	public ProdutoIngrediente(ProdutoIngrediente pi){
		this.id = pi.getId();
		this.produto = pi.getProduto();
		this.ingrediente = pi.getIngrediente();
		this.descricao_ingrediente = pi.getDescricao_ingrediente();
		this.grama_quilo = pi.getGrama_quilo();
		this.quantidade = pi.getQuantidade();
	}
	
	public ProdutoIngrediente() {}
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@JoinColumn(name="id_produto")
	@ManyToOne
	private Produto produto;
	@JoinColumn(name="id_ingrediente")
	@ManyToOne
	private Ingrediente ingrediente;
	@Column(name="descricao_ingrediente")
	private String descricao_ingrediente;
	@Column(name="grama_quilo")
	private String grama_quilo;
	@Column(name="quantidade")
	private double quantidade;

}
