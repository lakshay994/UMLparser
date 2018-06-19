package umlparser.umlparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GenerateDiagram {

	public static void generateDiagram(String grammar, String outpath)
	{
		try {            
            URL url = new URL("https://yuml.me/diagram/plain/class/" +grammar+ ".png");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed because of a error");
            }
            OutputStream os = new FileOutputStream(new File(outpath));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = conn.getInputStream().read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            os.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
	}
}
