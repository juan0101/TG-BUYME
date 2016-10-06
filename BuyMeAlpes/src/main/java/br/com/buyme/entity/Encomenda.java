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
@Table(name="encomenda")
@Data
public class Encomenda {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@JoinColumn(name="id_produto")
	@ManyToOne
	private Produto produto;
	@JoinColumn(name="id_cliente")
	@ManyToOne
	private Cliente cliente;
	@Column(name="quantidade", nullable=false)
	private int quantidade;
	@Column(name="valor", nullable=false)
	private double valor;
	@Column(name="descProduto", nullable=false)
	private String descProduto;
	@Column(name="descCliente", nullable=false)
	private String descCliente;
	@Column(name="desativado",columnDefinition = "BIT", nullable=false)
	private boolean desativado;
	
}
