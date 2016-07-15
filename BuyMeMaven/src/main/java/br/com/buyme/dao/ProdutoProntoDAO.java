package br.com.buyme.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.ProdutoPronto;

public class ProdutoProntoDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
//	public void salvar(ProdutoPronto pp){
//		EntityManager em = getEM();
//		try{
//			em.getTransaction().begin();
//			em.persist(pp);
//			em.getTransaction().commit();
//		}catch(Exception e){
//			System.out.println(e);
//			em.getTransaction().rollback();
//		}finally {
//			em.close();
//		}
//	}
	
	public void salvar(String codigo, String descricao, Date data_fabricacao, Date data_validade, double valor){
		EntityManager em = getEM();
		ProdutoPronto pp = new ProdutoPronto();
		try{
			em.getTransaction().begin();
			pp.setCodigo(codigo);
			pp.setDescricao(descricao);
			pp.setData_fabricacao(data_fabricacao);
			pp.setData_validade(data_validade);
			pp.setValor(valor);
			em.persist(pp);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public ProdutoPronto getOneByCodigo(String codigo){
		EntityManager em = getEM();
		ProdutoPronto pp = null;
		try{
			Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.codigo = :cod");
			query.setParameter("cod", codigo);
			pp = (ProdutoPronto)query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			em.close();
		}
		return pp;
	}
	
	@SuppressWarnings("unused")
	public boolean verificaCodigoExistente(String codigo){
		EntityManager em = getEM();
		ProdutoPronto pp = null;
		try{
			Query query = em.createQuery("Select pp FROM ProdutoPronto pp where pp.codigo = :cod");
			query.setParameter("cod", codigo);
			pp = (ProdutoPronto)query.getSingleResult();
		}catch(Exception e){
			return false;
		}finally {
			em.close();
		}
		return true;
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
	
}
