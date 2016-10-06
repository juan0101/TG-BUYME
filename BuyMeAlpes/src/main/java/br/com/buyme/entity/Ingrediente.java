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
@Table(name="ingrediente")
@Data
public class Ingrediente {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Column(name="produto_ingrediente", nullable=false)
	@OneToMany(mappedBy="ingrediente",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ProdutoIngrediente> produtos;
	
}
