package br.com.buyme.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mysql.jdbc.Connection;

import br.com.buyme.conexao.ConexaoBD;
import br.com.buyme.entity.RelatorioMotivoPerdaLote;
import javafx.stage.DirectoryChooser;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class RelatorioMotivoPerdaLoteDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public List<RelatorioMotivoPerdaLote> getRelatorio(String lote){
		EntityManager em = getEM();
		List<RelatorioMotivoPerdaLote> listaRetorno = new ArrayList<RelatorioMotivoPerdaLote>();
		String consulta = "Select pm.prodAnalisar.lote as lote, pm.prodAnalisar.descricao as produto, pm.motDesc as motivo, pm.quantidade as quantidade FROM ProdMot pm where 1 = 1 ";
		try{
			if(lote != null && !lote.isEmpty()){
				consulta = consulta + "and pm.prodAnalisar.lote = :lote ";
			}
			Query query = em.createQuery(consulta);
			if(lote != null && !lote.isEmpty()){
				query.setParameter("lote", lote);
			}
			
			List resultado = query.getResultList();
			
			Object object = null;
		    for (int i = 0; i < resultado.size(); i++) {
		    	RelatorioMotivoPerdaLote rmp = new RelatorioMotivoPerdaLote();
		        Object[] obj = (Object[]) resultado.get(i);
		        for (int j=0;j<obj.length;j++)
		        {
		        	if(j == 0){
		        		rmp.setLote(obj[j].toString());
		        	}else if(j == 1){
		        		rmp.setProduto(obj[j].toString());
		        	}else if(j == 2){
		        		rmp.setMotivoPerda(obj[j].toString());
		        	}else if(j == 3){
		        		rmp.setQuantidade(Integer.parseInt(obj[j].toString()));
		        	}
		        }
		        listaRetorno.add(rmp);
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
		String consulta = "select pa.lote as lote, pa.descricao as produto, pm.mot_desc as motivoPerda, pm.quantidade as quantidade from prod_mot pm inner join produto_analisar pa on pa.id = pm.prodAnalisar_id where 1=1 ";
		if(lote != null && !lote.isEmpty()){
			consulta = consulta + " and lote = '"+lote+"' ";
		}
		return consulta;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPdf(String sql){
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
			
				JasperDesign jd = JRXmlLoader.load("src/main/java/br/com/buyme/report/MotivoPerda.jrxml");
    			JRDesignQuery newQuery = new JRDesignQuery();
    			newQuery.setText(sql);
    			jd.setQuery(newQuery);
    			JasperReport jr = JasperCompileManager.compileReport(jd);
    			JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
    			JasperExportManager.exportReportToPdfFile(jp,caminhoSalvar+"/MotivoPerda.pdf");
			
    			conn.close();
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
