package br.com.buyme.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="produto_ingrediente")
public class ProdutoIngrediente implements Serializable{
	
	private static final long serialVersionUID = -2912431442618431922L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private Ingrediente ingrediente;
	@Column(name="descricao_ingrediente")
	private String descricao_ingrediente;
	@Column(name="grama_quilo")
	private String grama_quilo;
	@Column(name="quantidade")
	private double quantidade;

}
