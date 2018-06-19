package umlparser.umlparser;

import java.util.ArrayList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainParser {

	
	HashMap<String, Boolean> relationMap;
    HashMap<String, String> MapofClassConn;

	
	
	public MainParser(HashMap<String, Boolean> relationMap, HashMap<String, String> MapofClassConn) {
		this.relationMap=relationMap;
	    this.MapofClassConn=MapofClassConn;
	}

	  public String Mainparser(CompilationUnit cu) {
	        
		  	String class_name = "";
	        String class_name_2 = "";
	        String method_name = "";
		  	String ans = "";
	        
	        String field_name = "";
	        String add_params = ",";
	        ArrayList<String> public_field_list = new ArrayList<String>(); ;
	        List<TypeDeclaration> TypeDeclaration_list= cu.getTypes();
	        boolean next_parameter = false;
	        Node n = TypeDeclaration_list.get(0);
;
	        ClassOrInterfaceDeclaration class_Interface = (ClassOrInterfaceDeclaration) n;
	        



	        class_name = class_Interface.isInterface() ? "[" + "<<interface>>;" : "[";
	        class_name += class_Interface.getName();
	        class_name_2 = class_Interface.getName();

	        
	        for (final Iterator<BodyDeclaration> iterator = ((TypeDeclaration) n).getMembers().iterator(); iterator.hasNext();) {
				final BodyDeclaration body_declaration = iterator.next();
				if (body_declaration instanceof ConstructorDeclaration) {
	                if (!class_Interface.isInterface() &&((ConstructorDeclaration) body_declaration).getDeclarationAsString().startsWith("public") ) {
	                	method_name = NextParameters(method_name, next_parameter);
	                	method_name += "+ " + ((ConstructorDeclaration) body_declaration).getName() + "(";
	                    
	                    for (final Iterator<Node> contructor_iterator = ((ConstructorDeclaration) body_declaration).getChildrenNodes().iterator(); contructor_iterator.hasNext();) {
							final Object obj = contructor_iterator.next();
							if (obj instanceof Parameter) {
	                            final String param_class = ((Parameter) obj).getType().toString();
	                            final String param_name = ((Parameter) obj).getChildrenNodes().get(0).toString();
	                            method_name += param_name + " : " + param_class;
	                            if ( !relationMap.get(class_name_2) && relationMap.containsKey(param_class)) {
	                            	add_params += "[" + class_name_2+ "] uses -.->";
	                            	add_params += relationMap.get(param_class) ? "[<<interface>>;" + param_class + "]" : "[" + param_class + "]";

	                            }
	                            add_params += ",";
	                        }
						}
	                    method_name += ")";
	                    next_parameter = true;
	                }
	            }
			}
	        
	        
	        for (Iterator<BodyDeclaration> method_iterator = ((TypeDeclaration) n).getMembers().iterator(); method_iterator.hasNext();) {
				final BodyDeclaration body_declaration = method_iterator.next();
				if (body_declaration instanceof MethodDeclaration)
					if (!class_Interface.isInterface()&&((MethodDeclaration) body_declaration).getDeclarationAsString().startsWith("public")) {
						
	                    if (((MethodDeclaration) body_declaration).getName().startsWith("set") || ((MethodDeclaration) body_declaration).getName().startsWith("get")) {
	                        getset(public_field_list, body_declaration);
	                    } else {
	                    	
	                    	method_name = NextParameters(method_name, next_parameter);
	                    	method_name += "+ " + ((MethodDeclaration) body_declaration).getName() + "(";
	                        
	                        
	                        for (Iterator<Node> child_node = ((MethodDeclaration) body_declaration).getChildrenNodes().iterator(); child_node.hasNext();) {
								final Object obj = child_node.next();
								if (obj instanceof Parameter) {
	                                final String param_class = ((Parameter) obj).getType().toString();
	                                final String param_name = ((Parameter) obj).getChildrenNodes().get(0).toString();
	                                method_name += param_name + " : " + param_class;
	                                add_params = paramClassAddition2(class_name_2, add_params, param_class);
	                            }else {
	                                final String methodBody[] = obj.toString().split(" ");
	                                for (int i = 0; i < methodBody.length; i++) {
										final String x = methodBody[i];
										if (relationMap.containsKey(x) && !relationMap.get(class_name_2)) {
											add_params += "[" + class_name_2 + "] uses -.->";
											add_params += relationMap.get(x) ? "[<<interface>>;" + x + "]" : "[" + x + "]";

											add_params += ",";
	                                    }
									}
	                            }
							}
	                        method_name += ") : " + ((MethodDeclaration) body_declaration).getType();
	                        next_parameter = true;
	                    }
	                }
			}
	        field_name = FieldParser(class_name_2, field_name, public_field_list, n);
	        
	        
	        
	        // Adding extending classes to the add param
	        add_params = Inheritancecheck(class_name_2, add_params, class_Interface);
	        
	        
	        //Adding implementing classes to the add param
	        add_params = Implementationcheck(class_name_2, add_params, class_Interface);
	        // Combine className, methods and fields
	        ans += class_name;
	        ans = AddMultipleMethods(ans, field_name);
	        ans = AddMultipleMethods(ans, method_name);
	        ans += "]";
	        ans += add_params;
	        return ans;
}

	private String AddMultipleMethods(String result, String params) {
		if (!params.isEmpty()) {
			ScopeBracket cb = new ScopeBracket();

			result += "|" + cb.modifyBrackets(params);
		}
		return result;
	}

	private String Implementationcheck(String class_name_2, String add, ClassOrInterfaceDeclaration classInterface) {
		List<ClassOrInterfaceType> list;
		if (classInterface.getImplements() != null) {
		     list = (List<ClassOrInterfaceType>) classInterface.getImplements();
		    for (ClassOrInterfaceType x : list) {
		    	add += "[" + class_name_2 + "] " + "-.-^ " + "["
		                + "<<interface>>;" + x + "]";
		    	add += ",";
		    }
		}
		return add;
	}

	private String Inheritancecheck(String class_name_2, String add_params, ClassOrInterfaceDeclaration classInterface) {
		if (classInterface.getExtends() != null) {
			add_params += "[" + class_name_2 + "] " + "-^ " + classInterface.getExtends();
			add_params += ",";
		}
		return add_params;
	}

	private String FieldParser(String class_name_2, String field_names, ArrayList<String> public_field_list, Node n) {
		boolean next_argument = false;
		ScopeBracket cb = new ScopeBracket();
		for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) n).getMembers()) {
		    if (bodyDeclaration instanceof FieldDeclaration) {
		    	
		        String scope = cb.scopeFinder(bodyDeclaration.toStringWithoutComments().substring(0,bodyDeclaration.toStringWithoutComments().indexOf(" ")));
		        String class_name = cb.modifyBrackets(((FieldDeclaration) bodyDeclaration).getType().toString());
		        String field_name = ((FieldDeclaration) bodyDeclaration).getChildrenNodes().get(1).toString();
		        
		        //field_name = equalFieldName(bodyDeclaration, field_name);
		        if (field_name.contains("="))
					field_name = ((FieldDeclaration) bodyDeclaration).getChildrenNodes().get(1).toString()
				            .substring(0, ((FieldDeclaration) bodyDeclaration).getChildrenNodes().get(1)
				                    .toString().indexOf("=") - 1);
		        if (scope.equals("-") && public_field_list.contains(field_name.toLowerCase())) {
					scope = "+";
				}
		        String dependencies = "";
		        boolean multiplicity = false;
		        
		        if (class_name.contains("(")) {
		        	dependencies = class_name.substring(class_name.indexOf("(") + 1,
		        			class_name.indexOf(")"));
		        	multiplicity = true;
		        } 
		        else if (relationMap.containsKey(class_name)) {
		        	dependencies = class_name;
		        }
		        if (dependencies.length() > 0 && relationMap.containsKey(dependencies)) {
		            String scope_param = "-";

		            Relations_connector(class_name_2, dependencies, multiplicity, scope_param);
		        }
		        if ((scope == "+" || scope == "-")) {
		            field_names = ArgumentChange(field_names, scope, class_name, field_name, next_argument);
		            next_argument = true;
		        } else {
				}
		    }

		}
		return field_names;
	}

	private void Relations_connector(String class_name_2, String dependency, boolean multi, String scope_param) {
		if (MapofClassConn.containsKey(dependency + "-" + class_name_2)) {
			scope_param = MapofClassConn.get(dependency + "-" + class_name_2);
		    if (multi)
		    	scope_param = "*" + scope_param;
		    MapofClassConn.put(dependency + "-" + class_name_2,scope_param);
		} else {
		    if (multi)
		    	scope_param += "*";
		    MapofClassConn.put(class_name_2 + "-" + dependency, scope_param);
		}
	}

	private String ArgumentChange(String resulting_field, String scope, String class_name,
			String field_name,  boolean next_argument) {
		if (next_argument)
		{
			resulting_field += "; ";
		}
		resulting_field += scope + " " + field_name + " : " + class_name;
		return resulting_field;
	}

	  
	  
	  
	  
	  private String NextParameters(String method_names, boolean next_parameter) {
			if (next_parameter)
				method_names += ";";
			return method_names;
		}

	  
	  
		private void getset(final ArrayList<String> public_field_list, final BodyDeclaration bd) {
			final String varName = ((MethodDeclaration) bd).getName().substring(3);
			public_field_list.add(varName.toLowerCase());
		}
		
		
		

		private String  paramClassAddition2(String class_name_2, String addition_names, final String class_params) {
			if ((relationMap.containsKey(class_params) && !relationMap.get(class_name_2))) {
				
				addition_names += "[" + class_name_2 + "] uses -.->";
				addition_names += relationMap.get(class_params) ? "[<<interface>>;" + class_params + "]" : "[" + class_params + "]";

			}
			addition_names += ",";
			return addition_names;
		}
		
	

	  }