package br.com.buyme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.MotivoPerda;

public class MotivoPerdaDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(String codigo, String descricao){
		EntityManager em = getEM();
		MotivoPerda mp = new MotivoPerda();
		try{
			em.getTransaction().begin();
			mp.setCodigo(codigo);
			mp.setDescricao(descricao);
			em.persist(mp);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void editarMotivoPerda(int id,String desc){
		EntityManager em = getEM();
		MotivoPerda mp = em.find(MotivoPerda.class, id);
		try{
			em.getTransaction().begin();
			mp.setDescricao(desc); 
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}
	
	public MotivoPerda getOneByCod(String cod){
		EntityManager em = getEM();
		MotivoPerda mp = null;
		try{
			Query query = em.createQuery("Select mp FROM MotivoPerda mp WHERE mp.codigo = :codigo");
			query.setParameter("codigo", cod);
			mp = (MotivoPerda)query.getSingleResult();	
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e);
		}finally{
			em.close();
		}
		return mp;
	}
	
	@SuppressWarnings("unchecked")
	public List<MotivoPerda> getAllMotivoPerda(){
		EntityManager em = getEM();
		List<MotivoPerda> mps = null;
		try{
			Query query = em.createQuery("Select mp FROM MotivoPerda mp");
			mps = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return mps;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getNomes(){
		EntityManager em = getEM();
		List<String> nomes = null;
		try{
			em.getTransaction().begin();
			Query query = em.createQuery("Select mp.descricao FROM MotivoPerda mp");
			nomes = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return nomes;
	}
	
	public void excluir(int id){
		EntityManager em = getEM();
		MotivoPerda mp = null;
		try{
			mp = em.find(MotivoPerda.class, id);
			em.getTransaction().begin();
			em.remove(mp);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}

}
