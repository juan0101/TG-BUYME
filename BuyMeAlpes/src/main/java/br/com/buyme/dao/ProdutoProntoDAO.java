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
import br.com.buyme.entity.ProdutoPronto;
import javafx.stage.DirectoryChooser;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ProdutoProntoDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(int codigo, String descricao, Date data_fabricacao, Date data_validade, int quantidade, String lote){
		EntityManager em = getEM();
		try{
			em.getTransaction().begin();
			
			/**
			 * CONSULTA QUE VERIFICA SE JA EXISTE UMA PRODUÇÃO DO MESMO PRODUTO 
			 * COM A MESMA DATA DE FABRICAÇÃO E DATA VALIDADE
			 * 
			 * CASO EXISTA: O TOTAL DE PRODUTOS PRODUZIDOS AGORA SERÁ SOMADO NA PRODUÇÃO JÁ EXISTENTE NO BANCO
			 * CASO NÃO EXISTA: CRIA UM NOVO PRODUTO NO BANCO DE DADOS COM OS DADOS NECESSARIOS
			 */
			ProdutoPronto pp = null;
			try{
				Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.codigo = :codigo and pp.dataFabricacao = :dataFabricacao and "
						+ "pp.dataValidade = :dataValidade");
				query.setParameter("codigo", codigo);
				query.setParameter("dataFabricacao", data_fabricacao);
				query.setParameter("dataValidade", data_validade);
				pp = (ProdutoPronto)query.getSingleResult();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(pp != null){
				pp.setQuantidade(pp.getQuantidade() + quantidade);
			}else{
				pp = new ProdutoPronto();
				pp.setCodigo(codigo);
				pp.setDescricao(descricao);
				pp.setDataFabricacao(data_fabricacao);
				pp.setDataValidade(data_validade);
				pp.setQuantidade(quantidade);
				pp.setLote(lote);
			}
			em.persist(pp);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoPronto> getListByProdutoAnalisado(ProdutoAnalisar pa){
		EntityManager em = getEM();
		List<ProdutoPronto> pps = null;
		try{
			Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.produtoAnalisar = :pa");
			query.setParameter("pa", pa);
			pps = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return pps;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoPronto> getListByDescricao(String descricao){
		EntityManager em = getEM();
		List<ProdutoPronto> pps = null;
		try{
			Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.descricao = :descricao");
			query.setParameter("descricao", descricao);
			pps = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return pps;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoPronto> getListByCodigo(int codigo){
		EntityManager em = getEM();
		List<ProdutoPronto> pps = null;
		try{
			Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.codigo = :cod");
			query.setParameter("cod", codigo);
			pps = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			em.close();
		}
		return pps;
	}
	
	public ProdutoPronto getOneById(int id){
		EntityManager em = getEM();
		ProdutoPronto pp = null;
		try{
			pp = em.find(ProdutoPronto.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			em.close();
		}		
		return pp;
	}
	
	public void removerById(int id){
		try{
			EntityManager em = getEM();
			ProdutoPronto pp = em.find(ProdutoPronto.class, id);
			try{
				em.getTransaction().begin();
				em.remove(pp);
				em.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * MÉTODO QUE DIMINUI A QUANTIDADE DE PRODUTO PRONTO NO ESTOQUE
	 * SE A QUANTIDADE FOR = 0, O PRODUTO SERÁ REMOVIDO
	 * @param id
	 * @param quantidade
	 */
	public void removerQuantidadeById(ProdutoPronto produtoP, int quantidade){
		try{
			EntityManager em = getEM();
			ProdutoPronto pp = em.find(ProdutoPronto.class, produtoP.getId());
			try{
				em.getTransaction().begin();
				pp.setQuantidade(pp.getQuantidade() - quantidade);
				if(pp.getQuantidade() <= 0){
					em.remove(pp);
				}
				em.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * MÉTODO QUE FAZ A CONSULTA DA QUANTIDADE DE PRODUTO NO ESTOQUE
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public List<ProdutoPronto> verificaProdutoEstoque(String produto){
		EntityManager em = getEM();
		List<ProdutoPronto> listaRetorno = new ArrayList<ProdutoPronto>();
		String consulta = "Select pp.codigo,pp.descricao,SUM(pp.quantidade) from ProdutoPronto pp where 1=1 ";
		try{
			em.getTransaction().begin();
			if(produto != null){
				consulta = consulta + "and pp.descricao = :produto ";
			}
			consulta = consulta + "group by pp.descricao";
			
			Query query = em.createQuery(consulta);
			if(produto != null){
				query.setParameter("produto", produto);
			}
			
			List resultado = query.getResultList();
			
			Object object = null;
		    for (int i = 0; i < resultado.size(); i++) {
		    	ProdutoPronto pp = new ProdutoPronto();
		        Object[] obj = (Object[]) resultado.get(i);
		        for (int j=0;j<obj.length;j++)
		        {
		        	if(j == 0){
		        		pp.setCodigo(Integer.parseInt(obj[j].toString()));
		        	}else if(j == 1){
		        		pp.setDescricao(obj[j].toString());
		        	}else{
		        		pp.setQuantidade(Integer.parseInt(obj[j].toString()));
		        	}
		        }
		        listaRetorno.add(pp);
		    }
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return listaRetorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioEstoquePdf(String sql, int totalProdutos){
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
    			param.put("totalProdutos", totalProdutos);
    			JasperDesign jd = JRXmlLoader.load("src/main/java/br/com/buyme/report/VerificaEstoque.jrxml");
    			JRDesignQuery newQuery = new JRDesignQuery();
    			newQuery.setText(sql);
    			jd.setQuery(newQuery);
    			JasperReport jr = JasperCompileManager.compileReport(jd);
    			JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
    			JasperExportManager.exportReportToPdfFile(jp,caminhoSalvar+"/Estoque.pdf");
    			conn.close();
            }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
