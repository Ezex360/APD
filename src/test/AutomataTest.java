package automata;
import utils.*;


import org.junit.*;
import static org.junit.Assert.*;

public class AutomataTest {

@Test
public void test1() {
	DFAPila automata = null;
	try{
		automata = DFAConvertions.Dot2DFA("automata.dot");
		assertTrue("Acepta estado final",automata.acceptsFinalState("aab"));
	}catch(Exception e){}
}

@Test
public void test2() {
	DFAPila automata = null;
	try{
		automata = DFAConvertions.Dot2DFA("automata.dot");
		assertTrue("Acepta pila vacia",automata.acceptsEmptyStack("ab"));
	}catch(Exception e){}
}


@Test
public void test3() {
	DFAPila automata = null;
	try{
		automata = DFAConvertions.Dot2DFA("automata.dot");
		assertTrue("No acepta pila vacia",!automata.acceptsEmptyStack("aab"));
	}catch(Exception e){}

}

@Test
public void test4() {
	DFAPila automata = null;
	try{
		automata = DFAConvertions.Dot2DFA("automata.dot");
		assertTrue("No acepta estado final",!automata.acceptsFinalState("a"));
	}catch(Exception e){}
}

}
