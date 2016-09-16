package br.com.buyme.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="produto_pronto")
@Data
public class ProdutoPronto {
	
	public ProdutoPronto(ProdutoPronto pp){
		this.id = pp.getId();
		this.codigo = pp.getCodigo();
		this.descricao = pp.getDescricao();
		this.dataFabricacao = pp.getDataFabricacao();
		this.dataValidade = pp.getDataValidade();
		this.quantidade = pp.getQuantidade();
		this.lote = pp.getLote();
	}
	
	public ProdutoPronto() {}

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private int codigo;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_fabricacao", nullable=false)
	private Date dataFabricacao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_validade", nullable=false)
	private Date dataValidade;
	@Column(name="quantidade")
	private int quantidade;
	@Column(name="lote")
	private String lote;
	@Column(name="prod_vendido")
	@OneToMany(mappedBy="produtoPronto",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ProdutoVendido> prod_vendido;
	
}
