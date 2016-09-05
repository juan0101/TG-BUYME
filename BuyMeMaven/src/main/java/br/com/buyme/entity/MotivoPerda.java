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
@Table(name="motivo_perda")
@Data
public class MotivoPerda {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Column(name="prod_mot")
	@OneToMany(mappedBy="motPerda",fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<ProdMot> prod_mot;
	
}
