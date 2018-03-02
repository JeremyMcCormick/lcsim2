package org.lcsim.parameters;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.lcsim.parameters.api.Parameter;
import org.lcsim.parameters.impl.ParameterImpl;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ParametersTest extends TestCase {
	public void testParameters() {
		
		//
		// Define test parameters.
		//
		
		// Parameters representing Java primitive type classes.  
		Parameter<Byte>      byteP =    new ParameterImpl<Byte>      ( "ByteP",      new Byte      ("1")    );
		Parameter<Short>     shortP =   new ParameterImpl<Short>     ( "ShortP",     new Short     ("1")    );
		Parameter<Integer>   integerP = new ParameterImpl<Integer>   ( "IntegerP",   new Integer   ("1")    );
		Parameter<Long>      longP =    new ParameterImpl<Long>      ( "LongP",      new Long      ("1")    );
		Parameter<Float>     floatP =   new ParameterImpl<Float>     ( "FloatP",     new Float     ("1.0")  );
		Parameter<Double>    doubleP =  new ParameterImpl<Double>    ( "DoubleP",    new Double    ("1.0")  );
		Parameter<Boolean>   booleanP = new ParameterImpl<Boolean>   ( "BooleanP",   new Boolean   ("true") );
		Parameter<Character> charP =    new ParameterImpl<Character> ( "CharacterP", new Character ('1')    );
		
		// Integer list.		
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);
		integerList.add(2);
		integerList.add(3);
		Parameter<List<Integer>> integerListP = new ParameterImpl<List<Integer>>("IntegerListP", integerList);
		
		// List of lists, similar to a 2D array.
		List<List<Integer>> integerListList = new ArrayList<List<Integer>>();
		for (int i=0; i<3; i++) {
			List<Integer> aList = new ArrayList<Integer>();
			aList.add(1);
			aList.add(2);
			aList.add(3);
			integerListList.add(aList);
		}
		Parameter<List<List<Integer>>> integerListListP = new ParameterImpl<List<List<Integer>>>("IntegerListListP", integerListList);

		//
		// Test assertions.
		//
		
		assertEquals(byteP.value(),    new Byte("1"));
		assertEquals(shortP.value(),   new Short("1"));
		assertEquals(integerP.value(), new Integer("1"));
		assertEquals(longP.value(),    new Long("1"));
		assertEquals(floatP.value(),   new Float("1.0"));
		assertEquals(doubleP.value(),  new Double("1.0"));
		assertEquals(booleanP.value(), new Boolean("true"));
		assertEquals(charP.value(),    new Character('1'));
				
		for (int i=0; i<3; i++) {
			assertTrue(integerListP.value().get(i) == (i+1));
		}
		
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				assertTrue(integerListListP.value().get(i).get(j) == (j+1));
			}
		}
	}
}
