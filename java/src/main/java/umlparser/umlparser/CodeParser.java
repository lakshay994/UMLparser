package umlparser.umlparser;

import java.io.*;
import java.util.*;
import java.lang.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class CodeParser {
    final String inPath;
    final String outPath;
    public static HashMap<String, Boolean> map;
    public static HashMap<String, String> mapClassConn;
    String yumlCode;
    ArrayList<CompilationUnit> Compilation_Unit_Array;

    CodeParser(String inPath, String outFile) {
        this.inPath = inPath;
        this.outPath = inPath + "\\" + outFile + ".png";
        map = new HashMap<String, Boolean>();
        mapClassConn = new HashMap<String, String>();
        yumlCode = "";
    }

    public void init() throws Exception {
    	MainParser mp = new MainParser(map, mapClassConn);
    	Compilation_Unit_Array = get_All_CompilationUnits(inPath);
        Relation_Map_Builder(Compilation_Unit_Array);
        for (CompilationUnit cu : Compilation_Unit_Array)
            yumlCode += mp.Mainparser(cu);
        	//yumlCode += parser(cu);
        yumlCode += AddParseAdditions.parseAdditions();
        yumlCode = YUMLCode.Eliminate_YUML_Code(yumlCode);
        System.out.println("Unique Code: " + yumlCode);
        GenerateDiagram.generateDiagram(yumlCode, outPath);
    }

    

   


    

    private void Relation_Map_Builder(ArrayList<CompilationUnit> Compilation_Unit_Array) {
        for (CompilationUnit cu : Compilation_Unit_Array) {
            List<TypeDeclaration> list = cu.getTypes();
            for (Node node : list) {
                ClassOrInterfaceDeclaration obj = (ClassOrInterfaceDeclaration) node;
                map.put(obj.getName(), obj.isInterface()); // false means it is a class,
                                                           // true means it is a interface
            }
        }
    }


    private ArrayList<CompilationUnit> get_All_CompilationUnits(String inPath) throws Exception    
    {
        ArrayList<CompilationUnit> array = new ArrayList<CompilationUnit>();
        File folder = new File(inPath);
        CompilationUnit compilation_unit;
	    try
	    {
	    	if (folder.exists()) {
	        	
	        	for (File f : folder.listFiles()) {
	                if (f.isFile() && f.getName().endsWith(".java")) {
	                    FileInputStream input = new FileInputStream(f);
	                    
	                    	compilation_unit = JavaParser.parse(input);
	                        array.add(compilation_unit);
	                } 
	            }
	        }
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Problem occured while opening file");
        }

	    return array;

    }     
    
    
}
