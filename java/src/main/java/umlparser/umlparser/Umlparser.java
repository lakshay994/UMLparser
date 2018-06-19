package umlparser.umlparser;

public class Umlparser {
	
	public static void main(String[] args) throws Exception {
		String a = args[0];
		String b = args[1];
		String c = args[2];
        if (a.equals("class")) {
            CodeParser cp = new CodeParser(b,c);
            cp.init();
        
        } else {
            System.out.println("Invalid keyword " + args[0]);
        }

    }

}