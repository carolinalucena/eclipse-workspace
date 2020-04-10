package codenation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class Desafio {
	
	public static void main (String[] args) throws IOException, NoSuchAlgorithmException {
	
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
	    
	    System.out.println(cifrado.length());
	    
	    char letra;
	    int novaPosicao;
	    String decifrado = "";
	    
	    for (int i = 0; i < cifrado.length(); i++) {
	    	
	    	if (cifrado.charAt(i) == ' ' || cifrado.charAt(i) == ',' || cifrado.charAt(i) == '.') {
	    		letra = cifrado.charAt(i);
	    	}
	    	else {
	    		  	novaPosicao = (cifrado.charAt(i) - numeroCasas);
	    	
	    		  	if (novaPosicao < 97) {
	    		  		novaPosicao = novaPosicao - 96;
	    		  		novaPosicao = novaPosicao + 122;
	    		  	}
	    		  	letra = (char) (novaPosicao);	
	    	}
	    	//System.out.println(letra);
	    	decifrado = decifrado + letra;
	    }
	    System.out.println(decifrado);
	    
	    jsonTexto.put("decifrado", decifrado);
	    System.out.println(jsonTexto.getString("decifrado"));
	    
	    MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    digest.reset();
        digest.update(decifrado.getBytes("utf8"));
        String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        
        System.out.println(sha1);
        
        jsonTexto.put("resumo_criptografico", sha1);
        
	    System.out.println(jsonTexto.toString());
	    
	    FileWriter arq1 = new FileWriter("answer.json");
		
		PrintWriter gravarArq1 = new PrintWriter(arq1);
		 
	    gravarArq1.printf(jsonTexto.toString() + "\n");
		
	    arq1.close();
	    
	    URL urlPost = new URL("https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=da44dd95f36dd641abdf3a677fc68c705c57dfca");
	    HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();

	    //String auth = "Bearer " + oauthToken;
	    //connection.setRequestProperty("Authorization", basicAuth);
	    
	    File file = new File("answer.json");
	    
	    String boundary = UUID.randomUUID().toString();
	    connection.setRequestMethod("POST");
	    connection.setDoOutput(true);
	    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

	    DataOutputStream request = new DataOutputStream(connection.getOutputStream());

	    request.writeBytes("--" + boundary + "\r\n");
	    request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
	    request.writeBytes("Criptografia de Julio Cesar" + "\r\n");

	    request.writeBytes("--" + boundary + "\r\n");
	    request.writeBytes("Content-Disposition: form-data; name=\"answer\"; filename=\"" + "answer.json" + "\"\r\n\r\n");
	    request.writeBytes(jsonTexto.toString() + "\n");
	    request.writeBytes("\r\n");

	    request.writeBytes("--" + boundary + "--\r\n");
	    request.flush();
	    int respCode = connection.getResponseCode();

	    System.out.println(respCode);
	}
}
