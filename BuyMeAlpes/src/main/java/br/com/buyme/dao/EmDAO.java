package br.com.buyme.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmDAO {

	private static EmDAO emDao;
	private EntityManager em;
	
	public EmDAO(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("alpes");
		em = factory.createEntityManager();
	}
	
	public static synchronized EmDAO getInstance(){
		if(emDao == null){
			emDao = new EmDAO();
		}
		return emDao;
	}
	
	public EntityManager getEntityManager(){
		return this.em;
	}
	
}
