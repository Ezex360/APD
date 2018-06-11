package automata;

public class DFAException extends Exception{
     
    private int codigoError;
     
    public DFAException(int codigoError){
        super();
        this.codigoError=codigoError;
    }
     
    @Override
    public String getMessage(){
         
        String mensaje="";
         
        switch(codigoError){
            case 100:
                mensaje="\nError, se vacio la Pila durante el analisis de la cadena, no se pueden extraer mas elementos.";
                break;
            case 200:
                mensaje="\nError, no se encontro ninguna transicion realizable, la cadena no es reconocible para el automata.";
                break;
            case 300:
                mensaje="\nError, parece que el automata entra a un ciclo sin fin con transiciones lambda, verificarlo.";
                break;
            case 400:
                mensaje="\nError al analizar el archivo .dot, porfavor verificar su existencia y correctitud.";
                break;
            case 401:
                mensaje="\nError, automata incorrecto.";
                break;
            case 403:
                mensaje="\nError surgido al convertir el automata.";
                break;
            default:
                mensaje="\nError inesperado, no se pudo continuar con la operacion.";
                break;
        }
         
        return mensaje;
         
    }
     
}