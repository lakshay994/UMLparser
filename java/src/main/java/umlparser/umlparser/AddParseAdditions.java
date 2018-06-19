package umlparser.umlparser;

import java.util.HashMap;
import java.util.Set;

public class AddParseAdditions {
	

	
	
	public static  String parseAdditions()
	{
		String ans ="";
        Set<String> keys = CodeParser.mapClassConn.keySet(); // get all keys
        for (String i : keys) {
            String[] classes = i.split("-");
            String a = classes[0];
            String b = classes[1];
            ans += CodeParser.map.get(a) ? "[<<interface>>;" + a + "]" : "[" + a + "]";
            ans += CodeParser.mapClassConn.get(i);
            ans += CodeParser.map.get(b) ? "[<<interface>>;" + b + "]" : "[" + b + "]";
            ans += ",";
        }
        return ans;

	}

}
