package br.com.buyme.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="produto")
@Data
public class Produto {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@Column(name="nome", nullable=false)
	private String nome;
	@Column(name="valor", nullable=false)
	private double valor;
	@Column(name="produto_ingrediente")
	@OneToMany(mappedBy="produto",fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<ProdutoIngrediente> produto_ingrediente;
	@Column(name="forma")
	@OneToMany(mappedBy="produto",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Forma> forma;
	@Column(name="encomenda")
	@OneToMany(mappedBy="produto",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Encomenda> encomenda;

}
