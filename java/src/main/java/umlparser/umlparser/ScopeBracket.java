package umlparser.umlparser;

public class ScopeBracket {

	
	
public static String scopeFinder(String scope) {
        
        if(scope.equals("private"))
        {
        	return "-";
        }
        else if(scope.equals("public"))
        {
        	return "+";
        }
        else
        {
        	return "";
        }
    }

	public static String modifyBrackets(String x) {
	    x = x.replace("[", "(");
	    x = x.replace("]", ")");
	    x = x.replace("<", "(");
	    x = x.replace(">", ")");
	    return x;
	}


}
