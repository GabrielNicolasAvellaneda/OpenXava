package org.openxava.util;

import java.text.DateFormat;
import java.util.*;




/**
 * Utilidades para trabajar con fechas (<tt>java.util.Date</tt>). <p>
 * 
 * @author Javier Paniza
 */
public class Dates {
	
	private static DateFormat dateFormat;
	
	/**
	 * Con la hora a 0.
	 * Si dia, mes y año son 0 devuelve nulo.
	 */	
	public static Date create(int dia, int mes, int año) {
		if (dia == 0 && mes == 0 && año == 0) return null;
		Calendar cal = Calendar.getInstance();
		cal.set(año, mes - 1, dia, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();			
	}
	
	
	/**
	 * La fecha de hoy sin hora.
	 */
	public static Date createCurrent() {
		return removeTime(new java.util.Date());
	}
	
	/**
	 * Devuelve el dia asociado a la fecha. <p>
	 * 
	 * Si la fecha es nulo devuelve 0.
	 */
	public static int getDay(Date fecha) {
		if (fecha == null) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Devuelve el año (con 4 cifras) asociado a la fecha. <p>
	 * 
	 * Si la fecha es nulo devuelve 0.
	 */
	public static int getYear(Date fecha) {
		if (fecha == null) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * Devuelve el mes (de 1 a 12) asociado a la fecha. <p>
	 * 
	 * Si la fecha es nulo devuelve 0.
	 */
	public static int getMonth(Date fecha) {
		if (fecha == null) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		return cal.get(Calendar.MONTH) + 1;
	}
	
	
	/**
	 * Deja a cero las horas, los minutos, los segundo y los milisegundos. <p>
	 * 
	 * @param fecha Fecha de la que se quita la hora. Si es nulo se devuelve nulo.
	 * @return La misma fecha enviada como argumento. No se crea un nuevo objeto fecha.
	 */
	public static Date removeTime(Date fecha) {
		if (fecha == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.MINUTE, 0);		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		fecha.setTime(cal.getTime().getTime());
		return fecha;
	}
	
	/**
	 * Devuelve un clon pero sin horas, minutos, segundo nif milisegundos. <p>
	 * 
	 * @param fecha Fecha a clonar. Si es nulo se devuelve nulo.
	 * @return La misma fecha enviada como argumento. El argumento no se cambia sino que
	 * 	se crea un nuevo objeto fecha.
	 */
	public static Date clonWithoutTime(Date fecha) {
		if (fecha == null) return null;
		Date result = (Date) fecha.clone();
		removeTime(result);
		return result;
	}
		
	/**
	 * Crea una java.sql.Date a partir de una java.util.Date. <p>
	 * 
	 * @param fecha Si nulo devuelve nulo. <p>
	 */
	public static java.sql.Date toSQL(java.util.Date fecha) {
		if (fecha == null) return null;
		return new java.sql.Date(fecha.getTime());		
	}
	
	/**
	 * Una cadena con la fecha en formato corto según el <i>locale</i> actual.
	 * 
	 * @param fecha Si es nulo se devuelve cadena vacía.
	 * @return Nuncan nulo.
	 */
	public static String toString(java.util.Date fecha) {
		if (fecha == null) return "";
		return getDateFormat().format(fecha);			
	}
	
	private static DateFormat getDateFormat() {
		if (dateFormat == null) {
			dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		}
		return dateFormat;
	}

	/**
	 * Crea una fecha con la dd/mm/aa de la original
	 * y la hora actual. <p>
	 *  
	 * @param fecha No se cambia
	 * @return Nuncan nulo
	 */
	public static java.util.Date withTime(java.util.Date fecha) {
		if (fecha == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int año = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH);
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(new java.util.Date());
		cal.set(Calendar.YEAR, año);
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.DAY_OF_MONTH, dia);
		return cal.getTime();
	}
	
	/**
	 * Si las 2 fechas son iguales a nivel de día/mes/año
	 * ignorando la hora en la comparación. <p>
	 * 
	 * @param f1 Puede ser nulo
	 * @param f2 Puede ser nulo
	 */
	public static boolean isDifferentDay(java.util.Date f1, java.util.Date f2) {
		if (f1 == null && f2 == null) return false;
		if (f1 == null || f2 == null) return true;
		Calendar cal = Calendar.getInstance();
		cal.setTime(f1);
		int dd1 = cal.get(Calendar.DAY_OF_MONTH);
		int mm1 = cal.get(Calendar.MONTH);
		int aa1 = cal.get(Calendar.YEAR);
		cal.setTime(f2);
		int dd2 = cal.get(Calendar.DAY_OF_MONTH);
		int mm2 = cal.get(Calendar.MONTH);
		int aa2 = cal.get(Calendar.YEAR);
		return !(aa1==aa2 && mm1==mm2 && dd1==dd2);
	}
	
	/**
	 * Diferencia de dos fechas en años, meses y dias.<p> 
	 *  
	 * @param f1 y f2 en cualquier orden; ninguna de las dos puede ser nulo.
	 * @return Devuelve un mapa con la diferencia ("anyos", "meses", "dias")
	 */	
	public static DateDistance subtract(java.util.Date f1, java.util.Date f2 ) {
		DateDistance df = new DateDistance();
		if ( null == f1 || null == f2 ) return null;
		Calendar fmax = Calendar.getInstance(), fmin = Calendar.getInstance();
		int i;
		f1 = Dates.removeTime(f1);
		f2 = Dates.removeTime(f2);		
		if (f1.after(f2)) {
			fmax.setTime(f1);
			fmin.setTime(f2);
		}
		else if (f1.before(f2)) {
			fmin.setTime(f1);
			fmax.setTime(f2);
		}
		else {
			df.dias = 0;
			df.meses = 0;
			df.años = 0;
			return df;
		}
		
		for (i=0; !fmin.after(fmax); i++) {
			fmin.add(Calendar.YEAR, 1);
		}
		df.años = i-1;
		fmin.add(Calendar.YEAR, -1);
		
		for (i=0; !fmin.after(fmax); i++) {
			fmin.add(Calendar.MONTH, 1);
		}
		df.meses=i-1;
		fmin.add(Calendar.MONTH, -1);
		
		for (i=0; !fmin.equals(fmax); i++) {
			fmin.add(Calendar.DATE, 1);
		}
		df.dias=i;
		
		return df; 	
	}
	
	/**
	 * Reciba dos mapas con un número de días, meses y años y los suma. 
	 *  
	 * @param mapa1 y mapa2 deben contener "anyos", "meses" y "dias"; ninguno de los dos puede ser nulo.
	 * @return Devuelve un mapa con la suma ("anyos", "meses", "dias")
	 */	
	public static DateDistance addDateDistances(DateDistance mapa1, DateDistance mapa2 ) {
		DateDistance df=new DateDistance();
		if ( null == mapa1 || null == mapa2 ) return null;
		
		
		int anyos, meses, dias;
		dias = mapa1.dias + mapa2.dias;
		meses = dias / 30;
		dias = dias % 30;
		
		meses = meses + mapa1.meses + mapa2.meses ;
		anyos =  meses / 12 ;
		meses = meses % 12;		
				
		anyos = anyos + ( mapa1.años + mapa2.años );
		df.años=anyos;
		df.meses=meses;
		df.dias=dias;
				
		return df;
	}
	
	
	public static DateDistance subtractDateDistances(Dates.DateDistance mapa1, Dates.DateDistance mapa2 ) {
			DateDistance df = new DateDistance();
			if ( null == mapa1 || null == mapa2 ) return null;
			
		
			int anyos=0;
			int meses=0;
			int dias=0;
			dias = mapa1.dias - mapa2.dias;
			meses = mapa1.meses - mapa2.meses;
			anyos =mapa1.años - mapa2.años;
			
			if (dias<0){
				dias=dias+30;
				meses=meses-1;
			}
			if (meses<0){
				meses=meses+12;
				anyos=anyos-1;
			}
			
		df.años=anyos;
		df.meses=meses;
		df.dias=dias;
				
			return df;
		}
	

	/** 
	 * Returns number of days between startDate and endDate<p> 
	 *  
	 * @param java.util.Date startDate
	 * @param java.util.Date endDate
	 * @param boolean includeStartDate<p>
	 *  
	 */	
	  public static int getDaysInterval (Date startDate, Date endDate,
	  		boolean includeStartDate ) {
		
		startDate = Dates.removeTime(startDate);
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);		

		endDate = Dates.removeTime(endDate);		
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		
		if (includeStartDate) {
			start.add(Calendar.DATE, -1);
		}
		
		int days = 0;
		while (start.before(end)) {
			days++;
			start.add(Calendar.DATE,1);
		}
		return days;
	}

	
	public static class DateDistance {
		public  int dias;
		public  int meses;
		public  int años;
	}
}


