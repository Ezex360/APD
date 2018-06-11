package utils;

import java.io.*;
import java.util.Set;
import java.util.HashSet;

import automata.*;
import automata.DFAException;

public class DFAConvertions{

	public static DFAPila Dot2DFA(String filename) throws DFAException{
		Set<State> estados = new HashSet<>();
		Set<Character> alfabeto = new HashSet<>();
		Set<Character> alfabeto_pila = new HashSet<>();
		State inicial = null;
		Set<State> finales = new HashSet<>();
		Set<Quintuple<State,Character,Character,String,State>> transitions = new HashSet<>();

		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File (filename);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			System.out.println("Archivo: ");
			String linea;
			String inic = "inic";
			while((linea=br.readLine())!=null){
				System.out.println(linea);
				//Busqueda del estado inicial
				if(linea.contains("point")){
					inic = linea.substring(0,linea.indexOf("[")).trim();					
				}
				if(linea.contains(inic) && linea.contains("->")){
					String nombre = linea.substring(linea.indexOf(">")+1,linea.indexOf(";")).trim();	
					inicial = new State(nombre);
					estados.add(inicial);	
				}
				//Busqueda de estados finales
				if(linea.contains("doublecircle")){
					String[] parts = linea.split(";");
					if(parts.length != 1){
						parts = parts[1].split(" ");
						for(int i=0;i<parts.length;i++){
							State fin = new State(parts[i]);
							finales.add(fin);
							estados.add(fin);
						}
					}else{
						String nombre = linea.substring(0,linea.indexOf("[")).trim();
						State fin = new State(nombre);
						finales.add(fin);
						estados.add(fin);
					}
				}
				//Busqueda de transiciones
				if(linea.contains("->") && !linea.contains(inic)){
					//Obtengo datos
					String[] parts = linea.split("->");
					String origen = parts[0].trim();
					String destino = parts[1].substring(0, parts[1].indexOf("[")).trim();
					parts = parts[1].split("\"");
					String flecha = parts[1].trim();
					parts = flecha.split("/");					
					Character alf_car = parts[0].charAt(0);
					Character alf_pila = parts[1].charAt(0);
					alfabeto.add(alf_car);
					alfabeto_pila.add(alf_pila);
					String cad_pila = parts[2];
					State origen_state = new State(origen);
					State destino_state = new State(destino);
										estados.add(origen_state);
					estados.add(destino_state);
					//Genero transicion
					Quintuple<State,Character,Character,String,State> new_transition;
					new_transition = new Quintuple<>(origen_state,alf_car,alf_pila,cad_pila,destino_state);
					//Añado al conjunto de transiciones
					transitions.add(new_transition);
				}
			}
			System.out.println("Fin del archivo\n ");
			//Imprimo resultados obtenidos
			System.out.println("Datos extraidos ");
			System.out.println("Estados: ");
			System.out.println(estados);
			System.out.println("Alfabeto: ");
			System.out.println(alfabeto);
			System.out.println("Alfabeto pila: ");
			System.out.println(alfabeto_pila);
			System.out.print("Estado Inicial: ");
			System.out.print(inicial);
			System.out.print("\nEstados Finales: ");
			System.out.print(finales);
			System.out.println("\nTransiciones: ");
			System.out.println(transitions);
			System.out.println("");

			DFAPila result = new DFAPila(estados,alfabeto,alfabeto_pila,transitions,'@',inicial,finales);
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new DFAException(400);
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
				throw new DFAException(400);
			}
		}
		/*
		System.out.println("\nTest Time ");

		System.out.print("Dos state iguales?? ");
		State s_uno = new State("uno");
		State s_dos = new State("uno");
		System.out.print( s_uno.equals(s_dos)+"\n");

		System.out.print("Dos quintuples iguales??");
		Quintuple<State,Character,Character,String,State> q_uno = new Quintuple(s_uno,"a","a","a",s_dos);
		Quintuple<State,Character,Character,String,State> q_dos = new Quintuple(s_dos,"a","a","a",s_uno);
		System.out.print( q_uno.equals(q_dos)+"\n");

		System.out.print("añade quintuples iguales al set?");
		Set<Quintuple<State,Character,Character,String,State>> set_test = new HashSet();
		set_test.add(q_uno);
		set_test.add(q_dos);
		set_test.add(q_dos);
		System.out.print(set_test+"\n");

		for(Quintuple x : transitions)
            for(Quintuple y : transitions)
                if((x.first().equals(y.first())) && (x.second().equals(y.second())) && (!x.fifth().equals(y.fifth())) )
					System.out.println("no determinismo encontrado");
		*/

	}

	public static void DFA2Dot(DFAPila automata,String file) throws DFAException{
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(file);
            pw = new PrintWriter(fichero);

            pw.println("digraph{");
            pw.println("inic[shape=point];");
            pw.println("inic->"+automata.initial().toString()+";");
            for(Quintuple x : automata.transitions())
            	pw.println(x.first().toString()+"->"+x.fifth().toString()+"[label=\""+x.second().toString()+"/"+x.third().toString()+"/"+x.fourth().toString()+"\"];");
            for(State y : automata.finalStates())
           		pw.println(y.toString()+"[shape=doublecircle];");
            pw.println("}");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}

	public static DFAPila toFinalState(DFAPila convert) throws DFAException{

		State inicial;
	    Set<State> estados = convert.states();
	    Set<Character> alfabeto = convert.alphabet();
	    Set<Character> alfabeto_pila = convert.stackAlphabet(); //Alphabet of the stack
	    Set<Quintuple<State,Character,Character,String,State>> transitions = convert.transitions(); //delta function
	    Set<State> finales = new HashSet<>();

		inicial = new State("Initial");
		State final_state = new State("Final");
		finales.add(final_state);
		Quintuple<State,Character,Character,String,State> new_transition;
		new_transition = new Quintuple<>(inicial,convert.Lambda,'Z',"@Z",convert.initial());
		transitions.add(new_transition);
		for (State actual : estados){
			new_transition = new Quintuple<>(actual,convert.Lambda,'Z',convert.Lambda.toString(),final_state);
			transitions.add(new_transition);
		}
		estados.add(final_state);
		estados.add(inicial);
		alfabeto_pila.add('Z');
		DFAPila result = null;
		try{
			result = new DFAPila(estados,alfabeto,alfabeto_pila,transitions,'Z',inicial,finales);
		}catch(DFAException e){throw new DFAException(403);}
		return result;
		
	}

	public static DFAPila toEmptyStack(DFAPila convert) throws DFAException{

		State inicial;
	    Set<State> estados = convert.states();
	    Set<Character> alfabeto = convert.alphabet();
	    Set<Character> alfabeto_pila = convert.stackAlphabet(); //Alphabet of the stack
	    Set<Quintuple<State,Character,Character,String,State>> transitions = convert.transitions(); //delta function
	    Set<State> finales = convert.finalStates();

		inicial = new State("Initial");
		State tramp_state = new State("Tramp");
		estados.add(tramp_state);
		Quintuple<State,Character,Character,String,State> new_transition;
		new_transition = new Quintuple<>(inicial,convert.Lambda,'Z',"@Z",convert.initial());
		transitions.add(new_transition);
		for (State actual : finales){
			for (Character letra : alfabeto_pila){
				new_transition = new Quintuple<>(actual,convert.Lambda,letra,convert.Lambda.toString(),tramp_state);
				transitions.add(new_transition);
			}
		}
		for (Character letra : alfabeto_pila){
			new_transition = new Quintuple<>(tramp_state,convert.Lambda,letra,convert.Lambda.toString(),tramp_state);
			transitions.add(new_transition);
		}
		estados.add(inicial);
		alfabeto_pila.add('Z');
		DFAPila result = null;
		try{
			result = new DFAPila(estados,alfabeto,alfabeto_pila,transitions,'Z',inicial,finales);
		}catch(DFAException e){throw new DFAException(403);}
		return result;
		
	}


}

