package br.com.buyme.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
	
	public static String getFormatedDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	
	@SuppressWarnings("deprecation")
	public static Date getDateParcela(Date dOrigem, int dPagamento, int m){
		Date data = new Date();
		if(dOrigem.getMonth() == 11){
			data.setMonth(1);
			data.setYear(dOrigem.getYear() + 1);
		}else{
			data.setMonth(dOrigem.getMonth()+m);
		}
		try{
			if(data.getMonth() == 1){
				if(dPagamento > 28){
					if(isAnoBisexto(data.getYear())){
						data.setDate(29);
					}else{
						data.setDate(28);
					}
				}else{
					data.setDate(dPagamento);
				}
			}else{
				if(data.getMonth() == 0 || data.getMonth() == 2 || data.getMonth() == 4 || data.getMonth() == 6 ||
						data.getMonth() == 7 || data.getMonth() == 9 || data.getMonth() == 11){
					data.setDate(dPagamento);
				}else if(data.getMonth() == 3 || data.getMonth() == 5 || 
						data.getMonth() == 8 || data.getMonth() == 10){
					if(dPagamento > 30){
						data.setDate(30);
					}else{
						data.setDate(dPagamento);
					}
				}else{
					System.out.println("DATA FORA DOS PARAMETROS");
				}
			}
			return data;
		}catch (Exception e){
			e.printStackTrace();
			return data;
		}
	}
	
	public static boolean isAnoBisexto(int ano){
		if ( ( ano % 4 == 0 && ano % 100 != 0 ) || ( ano % 400 == 0 ) ){
            return true;
        }
        else{
            return false;
        }
	}
	
	public static Date getLocalDateToDate(LocalDate date){
		return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static boolean isNumber(String s){
		for (int i = 0; i < s.length(); i++) {
			if(!Character.isDigit(s.charAt(i))){
				return false;
			}
	    }
		return true;
	}
	
	public static boolean isNumberMonetario(String s){
		try{
			double valor = Double.parseDouble(s);
			if(valor>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			for (int i = 0; i < s.length(); i++) {
				if(!Character.isDigit(s.charAt(i))){
					return false;
				}
		    }
			int valor = Integer.parseInt(s);
			if(valor>0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static boolean isCPF(String CPF) {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		    if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
		        CPF.equals("22222222222") || CPF.equals("33333333333") ||
		        CPF.equals("44444444444") || CPF.equals("55555555555") ||
		        CPF.equals("66666666666") || CPF.equals("77777777777") ||
		        CPF.equals("88888888888") || CPF.equals("99999999999") ||
		       (CPF.length() != 11))
		       return(false);

		    char dig10, dig11;
		    int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		    try {
		// Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 10;
		      for (i=0; i<9; i++) {              
		// converte o i-esimo caractere do CPF em um numero:
		// por exemplo, transforma o caractere '0' no inteiro 0         
		// (48 eh a posicao de '0' na tabela ASCII)         
		        num = (int)(CPF.charAt(i) - 48); 
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if ((r == 10) || (r == 11))
		         dig10 = '0';
		      else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

		// Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 11;
		      for(i=0; i<10; i++) {
		        num = (int)(CPF.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if ((r == 10) || (r == 11))
		         dig11 = '0';
		      else dig11 = (char)(r + 48);

		// Verifica se os digitos calculados conferem com os digitos informados.
		      if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
		         return(true);
		      else return(false);
		    } catch (Exception e) {
		        return(false);
		    }
		  }
	
	/**
	 * Método main usado para testes dos métodos utilizados
	 * @param args
	 */
	
	public static void main(String[] args) {
		
	}
	
}
