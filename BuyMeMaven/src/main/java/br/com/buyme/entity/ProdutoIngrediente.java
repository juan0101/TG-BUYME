package br.com.buyme.entity;

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
public class ProdutoIngrediente {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@ManyToOne
	private Produto produto;
	@Column(name="ingrediente")
	private String ingrediente;
	@Column(name="grama_quilo")
	private String grama_quilo;
	@Column(name="quantidade")
	private double quantidade;

}
