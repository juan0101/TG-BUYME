package br.com.buyme.entity;

import lombok.Data;

@Data
public class RelatorioMotivoPerdaLote {
	
	private String lote;
	private String produto;
	private String motivoPerda;
	private int quantidade;

}
