package br.com.buyme.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.entity.ProdutoVendido;

public class ProdutoVendidoDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("alpes");
		return factory.createEntityManager();
	}
	
	public void salvar(String produto, int quantidade, String cliente, Date data, double valor, ProdutoPronto pp){
		EntityManager em = getEM();
		ProdutoVendido pv = new ProdutoVendido();
		try{
			em.getTransaction().begin();
			pv.setProduto(produto);
			pv.setQuantidade(quantidade);
			pv.setCliente(cliente);
			pv.setData(data);
			pv.setValor(valor);
			pv.setProdutoPronto(pp);
			em.persist(pv);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoVendido> getAll(){
		EntityManager em = getEM();
		List<ProdutoVendido> pv = null;
		try{
			Query query = em.createQuery("Select pv FROM ProdutoVendido e");
			pv = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return pv;
	}
}
