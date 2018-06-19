package umlparser.umlparser;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class YUMLCode {
	
	public static  String Eliminate_YUML_Code(String yuml_code)
	{
		String[] entire_code = yuml_code.split(",");
		String[] unique_code = new LinkedHashSet<String>(Arrays.asList(entire_code)).toArray(new String[0]);
		String ans = String.join(",", unique_code);
		return ans;
	}
}
