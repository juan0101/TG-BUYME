package br.com.buyme.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="produto_vendido")
@Data
public class ProdutoVendido {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="produto", nullable=false)
	private String produto;
	@Column(name="quantidade", nullable=false)
	private int quantidade;
	@Column(name="cliente", nullable=false)
	private String cliente;
	@Temporal(TemporalType.DATE)
	@Column(name="data", nullable=false)
	private Date data;
	@Column(name="valor", nullable=false)
	private double valor;
	@Column(name="produto_pronto", nullable=false)
	private int produtoPronto;
	
}
