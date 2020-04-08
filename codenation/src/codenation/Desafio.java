package codenation;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

public class Desafio {
	
	public static void main (String[] args) throws IOException {
	
		String urlCodenation = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=da44dd95f36dd641abdf3a677fc68c705c57dfca";
		String linha;
		String texto = "";
		
		URL url = new URL(urlCodenation);
		
		URLConnection conn = url.openConnection();
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		while ((linha = buffer.readLine()) != null){

			System.out.println(linha);
			
				texto = texto + linha;
		}
			
		FileWriter arq = new FileWriter("answer.json");
		
		PrintWriter gravarArq = new PrintWriter(arq);
		 
	    gravarArq.printf(texto + "\n");
		
	    arq.close();
	    
	    JSONObject jsonTexto = new JSONObject(texto);
	    
	    int numeroCasas = jsonTexto.getInt("numero_casas");
	    String cifrado = jsonTexto.getString("cifrado");
	    
	    System.out.println(numeroCasas);
	    System.out.println(cifrado);
	    
	    String[] alfabeto = new String[26];
	    
	    
	}
}
