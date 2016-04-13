package br.com.buyme.util;

import java.security.MessageDigest;

public class Utils {

	public static String senhaSha256(String senha){
		try{
			MessageDigest digs = MessageDigest.getInstance("SHA-256");
			digs.update((new String(senha)).getBytes("UTF8"));
			String senhaCript  = new String(digs.digest());
			return senhaCript;
		}catch (Exception e){
			System.out.println("Erro ao criptografar" + e);
			return "";
		}
	}
	
}
