package br.com.buyme.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mysql.jdbc.Connection;

import br.com.buyme.conexao.ConexaoBD;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.util.Utils;
import javafx.stage.DirectoryChooser;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class RelatorioPerdaProdutoDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("alpes");
		return factory.createEntityManager();
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public List<ProdutoAnalisar> getRelatorio(String lote){
		EntityManager em = getEM();
		List<ProdutoAnalisar> listaRetorno = new ArrayList<ProdutoAnalisar>();
		String consulta = "Select pa.descricao, pa.lote, SUM(pa.quantidade) as quantidade, SUM(pa.quantidadeProduzida) as quantidadeProduzida, SUM(pa.quantidadePerda) as quantidadePerda FROM ProdutoAnalisar pa where analisado = 1 ";
		try{
			if(lote != null && !lote.isEmpty()){
				consulta = consulta + "and pa.lote = :lote ";
			}
			consulta = consulta + "group by pa.descricao, pa.lote";
			Query query = em.createQuery(consulta);
			if(lote != null && !lote.isEmpty()){
				query.setParameter("lote", lote);
			}
			
			List resultado = query.getResultList();
			
			Object object = null;
		    for (int i = 0; i < resultado.size(); i++) {
		    	ProdutoAnalisar pa = new ProdutoAnalisar();
		        Object[] obj = (Object[]) resultado.get(i);
		        for (int j=0;j<obj.length;j++)
		        {
		        	if(j == 0){
		        		pa.setDescricao(obj[j].toString());
		        	}else if(j == 1){
		        		pa.setLote(obj[j].toString());
		        	}else if(j == 2){
		        		pa.setQuantidade(Integer.parseInt(obj[j].toString()));
		        	}else if(j == 3){
		        		pa.setQuantidadeProduzida(Integer.parseInt(obj[j].toString()));
		        	}else{
		        		pa.setQuantidadePerda(Integer.parseInt(obj[j].toString()));
		        	}
		        }
		        listaRetorno.add(pa);
		    }
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return listaRetorno;
	}
	
	public String getQuery(String lote){
		String consulta = "select descricao, lote, SUM(quantidade) as quantidade, SUM(quantidade_produzida) as quantidade_produzida, SUM(quantidadePerda) as quantidadePerda from produto_analisar where analisado = 1 ";
		if(lote != null && !lote.isEmpty()){
			consulta = consulta + " and lote = '"+lote+"' ";
		}
		consulta = consulta + "group by descricao,lote";
		return consulta;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPdf(String sql, int quantPerdido, String lote){
		try{
			String caminhoSalvar = "";
        	DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
             
            if(selectedDirectory == null){
                System.out.println("Não selecionou nada");
            }else{
                caminhoSalvar = selectedDirectory.getAbsolutePath();
            }
            if(!caminhoSalvar.isEmpty()){
            	Connection conn = new ConexaoBD().getConnection();
    			HashMap param = new HashMap();
    			param.put("dataExport", Utils.getDate(new Date()));
    			if(lote != null && !lote.isEmpty()){
    				param.put("paramLote", lote);
    				JasperDesign jd = JRXmlLoader.load("src/main/java/br/com/buyme/report/PerdaProduto.jrxml");
        			JRDesignQuery newQuery = new JRDesignQuery();
        			newQuery.setText(sql);
        			jd.setQuery(newQuery);
        			JasperReport jr = JasperCompileManager.compileReport(jd);
        			JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
        			JasperExportManager.exportReportToPdfFile(jp,caminhoSalvar+"/PerdaProduto.pdf");
    			}else{
    				JasperDesign jd = JRXmlLoader.load("src/main/java/br/com/buyme/report/PerdaProdutoSemLote.jrxml");
        			JRDesignQuery newQuery = new JRDesignQuery();
        			newQuery.setText(sql);
        			jd.setQuery(newQuery);
        			JasperReport jr = JasperCompileManager.compileReport(jd);
        			JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
        			JasperExportManager.exportReportToPdfFile(jp,caminhoSalvar+"/PerdaProduto.pdf");
    			}
    			conn.close();
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
