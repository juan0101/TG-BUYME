package br.com.buyme.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.ProdutoAnalisar;

public class ProdutoAnalisarDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(int quantidade, String descricao, Date data_fabricacao, Date data_validade, double valor, boolean analisado){
		EntityManager em = getEM();
		try{
			ProdutoAnalisar pa = new ProdutoAnalisar();
			em.getTransaction().begin();
			pa.setQuantidade(quantidade);
			pa.setDescricao(descricao);
			pa.setData_fabricacao(data_fabricacao);
			pa.setData_validade(data_validade);
			pa.setValor(valor);
			pa.setValorTotal(valor * quantidade);
			pa.setAnalisado(analisado);
			em.persist(pa);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void excluir(int id){
		EntityManager em = getEM();
		ProdutoAnalisar pa = null;
		try{
			pa = em.find(ProdutoAnalisar.class, id);
			em.getTransaction().begin();
			em.remove(pa);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void finalizarAnalise(int id, int quantidadeProduzida){
		EntityManager em = getEM();
		ProdutoAnalisar pa = null;
		try{
			pa = em.find(ProdutoAnalisar.class, id);
			em.getTransaction().begin();
			pa.setQuantidadeProduzida(quantidadeProduzida);
			pa.setAnalisado(true);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ProdutoAnalisar> getAllAnalisado(boolean analisado){
		EntityManager em = getEM();
		List<ProdutoAnalisar> pas = null;
		try{
			Query query = em.createQuery("Select pa FROM ProdutoAnalisar pa where pa.analisado = :analisado");
			query.setParameter("analisado", analisado);
			pas = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return pas;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoAnalisar> getAll(){
		EntityManager em = getEM();
		List<ProdutoAnalisar> pas = null;
		try{
			Query query = em.createQuery("Select pa FROM ProdutoAnalisar pa");
			pas = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return pas;
	}
	
	public void editarAnalise(int id, boolean analisado){
		EntityManager em = getEM();
		ProdutoAnalisar pa = em.find(ProdutoAnalisar.class, id);
		try{
			em.getTransaction().begin();
			pa.setAnalisado(analisado);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}

}
