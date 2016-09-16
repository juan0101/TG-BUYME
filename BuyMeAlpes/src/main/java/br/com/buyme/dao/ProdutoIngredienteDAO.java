package br.com.buyme.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoIngrediente;

public class ProdutoIngredienteDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoIngrediente> getList(Produto prod){
		EntityManager em = getEM();
		List<ProdutoIngrediente> ingredientes = new ArrayList<ProdutoIngrediente>();
		try{
			Query query = em.createQuery("Select pi FROM ProdutoIngrediente pi where pi.produto = :produto");
			query.setParameter("produto", prod);
			ingredientes = query.getResultList();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return ingredientes;
	}
	
	public void deleteListProdIng(List<Integer> ids){
		EntityManager em = getEM();
		try{
			for(Integer id: ids){
				ProdutoIngrediente prodIng = null;
				prodIng = em.find(ProdutoIngrediente.class, id);
				if(prodIng != null){
					em.getTransaction().begin();
					em.remove(prodIng);
					em.getTransaction().commit();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
}
