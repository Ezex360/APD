package automata;
import utils.DFAConvertions;

import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		DFAPila automata = null;
		Boolean finish=false;
        while (!finish) {
            clearScreen();
            System.out.println("Menu");
            System.out.println("1- Leer Automata Pila Deterministico desde archivo");
            System.out.println("2- Verificar por Estado Final - Pila Vacia");
            System.out.println("3- Salir");
            String opt = leerTeclado();
            try{
	            switch (opt) {
	            	case "1":
	            		System.out.println("Ingrese la direccion del archivo a leer, en caso de no indicarlo, se tomara el archivo \"automata.dot\" por defecto ");
	            		String file = leerTeclado();
	            		if(file.equals("")){
	            			System.out.println("Leyendo archivo por defecto\n");
	            			file = "automata.dot";
	            		}
	            		automata = DFAConvertions.Dot2DFA(file);
	            		break;
	            	case "2":
	            		if(automata == null){
	            			System.out.println("Lea un automata primero porfavor.");
	            			break;
	            		}
	            		System.out.println("Ingrese la cadena a comprobar por el automata");
	            		String cadena = leerTeclado();
	            		System.out.println("Comprobando...");
	            		Boolean final_state = automata.acceptsFinalState(cadena);
	            		System.out.println("Acepta por Estado Final: "+final_state);
	            		Boolean empty_stack = automata.acceptsEmptyStack(cadena);
	            		System.out.println("Acepta por Pla Vacia: "+empty_stack);
	            		if(final_state && !empty_stack){
	            			System.out.println("Desea convertirlo a Automata Pila por Pila Vacia? (ingrese 'si' o 'no')");
	            			if(leerTeclado().equals("si")){
	            				System.out.println("Ingrese el nombre completo del archivo a escribir");
			            		String file2convert = leerTeclado();
			            		if(file2convert.equals("")){
			            			System.out.println("No se ingreso un nombre de archivo, vuelva a realizar la operacion nuevamente.");
			            			break;
			            		}
		            			DFAPila new_automata = DFAConvertions.toEmptyStack(automata);
		            			DFAConvertions.DFA2Dot(new_automata,file2convert);
	            			}else{ System.out.println("No se realizara la conversion."); }
	            		}
	            		if(!final_state && empty_stack){
	            			System.out.println("Desea convertirlo a Automata Pila por Estado Final? (ingrese 'si' o 'no')");
	            			if(leerTeclado().equals("si")){
	            				System.out.println("Ingrese el nombre completo del archivo a escribir");
			            		String file2convert = leerTeclado();
			            		if(file2convert.equals("")){
			            			System.out.println("No se ingreso un nombre de archivo, vuelva a realizar la operacion nuevamente.");
			            			break;
			            		}
		            			DFAPila new_automata = DFAConvertions.toFinalState(automata);
		            			DFAConvertions.DFA2Dot(new_automata,file2convert);
	            			}else{ System.out.println("No se realizara la conversion."); }
	            		}
	            		break;
	            	case "3":
	            		finish = true;
	            		break;
	            	default:
	            		System.out.println("No es una opcion valida");
	            		break;
	            }
        	}catch(DFAException e){System.err.println(e.getMessage());}
            System.out.println("Pulse enter para continuar");
            leerTeclado();
            clearScreen();
        }
	}

    //Funcion para leer cadenas por teclado.
    public static String leerTeclado(){
        String entradaTeclado = "";
        Scanner entradaEscaner = new Scanner (System.in); 
        entradaTeclado = entradaEscaner.nextLine();
        return entradaTeclado;
    }

    //Funcion para limpiar la pantalla.
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}