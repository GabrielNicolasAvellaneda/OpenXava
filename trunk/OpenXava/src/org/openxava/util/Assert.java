package org.openxava.util;


/**
 * Utilidad para hacer afirmaciones. <p>
 *
 * En la mayoría de los casos estas afirmaciones consisten
 * en comprobar si un objeto es nulo. Hay una variedad de
 * métodos que lanzan diferentes tipos de excepciones.<br>
 * Una de las excepciones que se lanza es <tt>AssertException</tt>, 
 * pero no es preceptivo que sea esta.<p>
 *
 * Si alguno de los métodos lanza una excepción esto suele
 * indicar un error en el software, normalmente que el
 * programador-usuario no ha cumplido con los contratos del
 * componente. <br>
 * Los mensajes no tienen que especificar el lugar en donde
 * se rompe el contrato, ni el contrato que se ha roto. La
 * traza (que se imprime siempre) indica el lugar y la clase
 * de la excepción el mótivo. Yendo a la documentación del
 * método que produce la excepción se podrá ver con detalle
 * que pasa.<br>
 *
 * Los nombres y funcionamiento coinciden con los de JUnit,
 * la diferencia está en la excepción lanzada.<br> 
 *
 * @author  Javier Paniza
 */

public class Assert {

  /**
   * Comprueba el argumento. <br>
   *
   * @exception IllegalArgumentException Si <tt>o == null</tt>.
   */
  public final static void arg(Object o) {
	if (o == null) {
	  lanzar(new IllegalArgumentException(XavaResources.getString("assert_no_null_argv")));
	}
  }
  /**
   * Comprueba los argumento. <br>
   *
   * @exception IllegalArgumentException Si <tt>o1 == null || o2 == null</tt>.
   */
  public final static void arg(Object o1, Object o2) {
	if (o1 == null || o2 == null) {
	  lanzar(new IllegalArgumentException(XavaResources.getString("assert_no_null_argv")));
	}
  }
  /**
   * Comprueba los argumento. <br>
   *
   * @exception IllegalArgumentException Si <tt>o1 == null || o2 == null || o3 == null</tt>.
   */
  public final static void arg(Object o1, Object o2, Object o3) {
	if (o1 == null || o2 == null || o3 == null) {
	  lanzar(new IllegalArgumentException(XavaResources.getString("assert_no_null_argv")));
	}
  }
  /**
   * Comprueba los argumento. <br>
   *
   * @exception IllegalArgumentException Si <tt>o1 == null || o2 == null || o3 == null || o4 == null</tt>.
   */
  public final static void arg(Object o1, Object o2, Object o3, Object o4) {
	if (o1 == null || o2 == null || o3 == null || o4 == null) {
	  lanzar(new IllegalArgumentException(XavaResources.getString("assert_no_null_argv")));
	}
  }
	/**
	 * Asserts that a condition is true. <p>
	 *
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertTrue(String message, boolean condition) {
		if (!condition)
			fail(message);
	}
	/**
	 * Asserts that a condition is true. <p>
	 *
	 * @exception AssertException  Si no se cumple la afirmación.
	 */
	static public void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}
	/**
	 * Asserts that two doubles are equal.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @param delta tolerated delta
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertEquals(double expected, double actual, double delta) {
	    assertEquals(null, expected, actual, delta);
	}
	/**
	 * Asserts that two longs are equal.
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertEquals(long expected, long actual) {
	    assertEquals(null, expected, actual);
	}
	/**
	 * Asserts that two objects are equal. <p>
	 *
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertEquals(Object expected, Object actual) {
	    assertEquals(null, expected, actual);
	}
	/**
	 * Asserts that two doubles are equal.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @param delta tolerated delta
	 * @exception AssertException  Si no se cumple la afirmación.
	 */
	static public void assertEquals(String message, double expected, double actual, double delta) {
		if (!(Math.abs(expected-actual) <= delta)) // Because comparison with NaN always returns false
			failNotEquals(message, new Double(expected), new Double(actual));
	}
	/**
	 * Asserts that two longs are equal.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertEquals(String message, long expected, long actual) {
	    assertEquals(message, new Long(expected), new Long(actual));
	}
	/**
	 * Asserts that two objects are equal. If they are not
	 * an AssertionFailedError is thrown.
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertEquals(String message, Object expected, Object actual) {
		if (expected == null && actual == null)
			return;
		if (expected != null && expected.equals(actual))
			return;
		failNotEquals(message, expected, actual);
	}
	/**
	 * Asserts that an object isn't null.
	 *
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertNotNull(Object object) {
		assertNotNull(null, object);
	}
	/**
	 * Asserts that an object isn't null.
	 *
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertNotNull(String message, Object object) {
		assertTrue(message, object != null); 
	}
	/**
	 * Asserts that an object is null.
	 *
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertNull(Object object) {
		assertNull(null, object);
	}
	/**
	 * Asserts that an object is null.
	 *
	 * @exception AssertException  Si no se cumple la afirmación.
	 */
	static public void assertNull(String message, Object object) {
		assertTrue(message, object == null); 
	}
	/**
	 * Asserts that two objects refer to the same object. <p>
	 *
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertSame(Object expected, Object actual) {
	    assertSame(null, expected, actual);
	}
	/**
	 * Asserts that two objects refer to the same object. <p>
	 *
	 * @param message the detail message for this assertion
	 * @param expected the expected value of an object
	 * @param actual the actual value of an object
	 * @exception AssertException  Si no se cumple la afirmación.	 
	 */
	static public void assertSame(String message, Object expected, Object actual) {
		if (expected == actual)
			return;
		failNotSame(message, expected, actual);
	}
/**
 * Lanza una AssertException e imprime la traza. <p>
 */
public final static void fail() {
	lanzar(new AssertException());
}
/**
 * Lanza una AssertException e imprime la traza. <p>
 */
public final static void fail(String mensaje) {
	lanzar(new AssertException(mensaje));
}
	static private void failNotEquals(String message, Object expected, Object actual) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		fail(formatted+"esperado:<"+expected+"> pero fue:<"+actual+">");
	}
	static private void failNotSame(String message, Object expected, Object actual) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		fail(formatted+" esperado el mismo");
	}
  // Lanza la excepción, pero imprime la traza
  private static void lanzar(RuntimeException ex) {
	try {
	  throw ex;
	}
	catch (RuntimeException ex2) {
	  ex.printStackTrace();
	  throw ex2;
	}
  }
}
