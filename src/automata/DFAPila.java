package automata;

import java.util.Set;
import java.util.Stack;

import utils.Quintuple;

public final class DFAPila{

	public static final Character Lambda = '_';
    public static final Character Joker = '@';
    public static final Character Inicial = 'Z';
    
    
	
    private Stack<Character> stack; //the stack of the automaton

    
    
    State initial;
    Character stackInitial;
    Set<State> states;
    Set<Character> alphabet;
    Set<Character> stackAlphabet; //Alphabet of the stack
    Set<Quintuple<State,Character,Character,String,State>> transitions; //delta function
    Set<State> finalStates;


    /**
     * Constructor of the class - returns a DFAPila object
     * @param states - states of the DFAPila
     * @param alphabet - the alphabet of the automaton 
     * @param stackAlphabet - the alphabet of the stack
     * @param transitions - transitions of the automaton
     * @param stackInitial - a Character which represents the initial element of the stack
     * @param initial - initial State of the automaton
     * @param final_states - acceptance states of the automaton
     * @throws IllegalArgumentException
     */
    public DFAPila(
            Set<State> states,
            Set<Character> alphabet,
            Set<Character> stackAlphabet,
            Set<Quintuple<State, Character,Character,String, State>> transitions,
            Character stackInitial,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException,DFAException
    {
        this.states = states;
        this.alphabet = alphabet;
        this.stackAlphabet = stackAlphabet;
        stackAlphabet.add(Lambda); //the character lambda is used in the stack to know when do a pop
        stackAlphabet.add(Joker); //the mark of the stack
        this.transitions = transitions;
        this.stackInitial = stackInitial;
        this.initial = initial;
        this.finalStates = final_states;
        stack = new Stack<Character>();
        stack.push(stackInitial); //insert the mark in the stack
        if (!rep_ok()){
            throw new  DFAException(401);
        }
        System.out.println("Is a DFA Pila");
    }
        
    
    public State delta(State from, Character c){
		for(Quintuple x : transitions){
            if(x.first().equals(from) && x.second().equals(c) ){
                return (State) x.fifth();
            }
        }
    	return null;
    }

    public Quintuple delta_stack(State from, Character c, Character s){
         for(Quintuple x : transitions)
            if(x.first().equals(from) && x.second().equals(c) && x.third().equals(s))
                return x;
        return null;
    }
        
    public boolean acceptsFinalState(String string) throws DFAException {
        State actual = initial;
        State temp;
        int loop = 0;
        for(int i=0;i<string.length();i++){
            loop++;
            if (loop == 1000){ throw new DFAException(300); }
            temp = delta(actual,string.charAt(i));
            if(temp == null){
                temp = delta(actual,Lambda);
                i--;
            }
            if(temp == null){ 
                throw new DFAException(200);
            } 
            else
                actual = temp;
        }
        if(finalStates.contains(actual))
            return true;
        else
    	   return false;
    }

    public boolean acceptsEmptyStack(String string) throws DFAException {

        stack = new Stack<Character>();
        stack.push(stackInitial); //insert the mark in the stack
        State actual = initial;
        Quintuple transition;
        Character top;

        for(int i=0;i<string.length();i++){
            if(stack.isEmpty()) { throw new DFAException(100); }
            top = stack.pop();
            transition = delta_stack(actual,string.charAt(i),top);
            if(transition == null){
                transition = delta_stack(actual,Lambda,top);
            }
            if(transition == null){ throw new DFAException(200); }                
            String cad_pila = (String) transition.fourth();
            if(cad_pila.charAt(0) != Lambda)
                for(int j=cad_pila.length()-1;j>=0;j--)
                    stack.push(cad_pila.charAt(j));
            actual = (State) transition.fifth();
        }
        Boolean lamba_end_loop = false;
        while(!lamba_end_loop){
            lamba_end_loop = true;
            if(!stack.isEmpty()){
                transition = delta_stack(actual,Lambda,stack.pop());
                if(transition != null){
                    lamba_end_loop = false;
                    String cad_pila = (String) transition.fourth();
                    if(cad_pila.charAt(0) != Lambda)
                        for(int j=cad_pila.length()-1;j>=0;j--)
                            stack.push(cad_pila.charAt(j));
                    actual = (State) transition.fifth();
                }
            }
        }

        if(stack.isEmpty())
            return true;
        return false;

    }

    public State initial(){ return initial; };

    public Character stackInitial(){ return stackInitial; };

    public Set<State> states(){ return states; };

    public Set<Character> alphabet(){ return alphabet; };

    public Set<Character> stackAlphabet(){ return stackAlphabet; };

    public Set<Quintuple<State,Character,Character,String,State>> transitions() { return transitions; }

    public Set<State> finalStates(){ return finalStates; };


    public boolean rep_ok() {
        //Si no posee estado inicial
        if(initial == null){
            System.out.println("El automata no posee estado inicial!");
            return false;
        }
        //Si posee alguna transicion mal formada
        for(Quintuple x : transitions)
            if(x.first()==null || x.second()==null || x.third()==null || x.fourth()==null || x.fifth()==null){
                System.out.println("Se encontro una transicion mal formada!");
                return false;
            }
        //Si no es determinista
        for(Quintuple x : transitions){
            for(Quintuple y : transitions){
                if((x.first().equals(y.first())) && (!x.second().equals(y.second())) && (x.fifth().equals(y.fifth())) ){
                    System.out.println("El automata no es determinista!");
                    return false;
                }
                if((x.first().equals(y.first())) && (x.second().equals(y.second()))  && (!x.third().equals(y.third())) && (x.fifth().equals(y.fifth())) ){
                    System.out.println("El automata no es determinista!");
                    return false;
                }
            }
        }
        return true;
    }

   
	
}
