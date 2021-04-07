
package parserxmltojson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/** 
 *
 * @author lucas
 */
public class ParserXmlToJson {
    static String webService = "https://api.factmaven.com/xml-to-json/?xml=";
    static int codigoSucesso = 200;
    
    public static void main(String[] args) throws Exception {
        try {
            String xmlFilePath = "src/parserxmltojson/input.xml";
            String fileString = readLineByLineJava8( xmlFilePath );

            URL url = new URL(webService+fileString);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != codigoSucesso)
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

            BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
            String jsonEmString = converteJsonEmString(resposta);

            System.out.println(jsonEmString);
            try (PrintWriter out = new PrintWriter("src/parserxmltojson/output.json")) {
                out.println(jsonEmString);
            }
        } catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }
    
    
    private static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
        String resposta, jsonEmString = "";
        while ((resposta = buffereReader.readLine()) != null) {
            jsonEmString += resposta;
        }
        return jsonEmString;
    }
     
    private static String readLineByLineJava8(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return contentBuilder.toString();
    }
    
}
