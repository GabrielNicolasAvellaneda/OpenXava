package org.openxava.validators.meta;

import java.util.*;

import org.openxava.util.*;
import org.openxava.validators.meta.xmlparse.*;

/**
 * Inserte aquí la descripción del tipo.
 * 
 * @author Javier Paniza
 */
public class MetaValidators {

	private static Collection primitiveTypes;
	private static Map metaValidators;
	private static Map metaValidatorsRequired;
	
	public static void _addMetaValidator(MetaValidator newMetaValidator) throws XavaException {
		if (metaValidators == null) {
			throw new XavaException("only_from_parse", "MetaValidators._addMetaValidator");
		}
		metaValidators.put(newMetaValidator.getName(), newMetaValidator);
	}
	public static void _addMetaValidatorRequired(MetaValidatorRequired newMetaValidator) throws XavaException {
		if (metaValidatorsRequired == null) {
			throw new XavaException("only_from_parse", "MetaValidators._addMetaValidatorRequired");
		}
		if (!Is.emptyString(newMetaValidator.getForType())) {
			metaValidatorsRequired.put(newMetaValidator.getForType(), newMetaValidator);
		}
		else if (!Is.emptyString(newMetaValidator.getForStereotype())) {
			metaValidatorsRequired.put(newMetaValidator.getForStereotype(), newMetaValidator);
		}		
		else {
			throw new XavaException("required_validator_type_or_stereotype_required");
		}
	}
	
	/**
	 * 
	 * @return Nulo si no lo encuentra
	 */
	private static MetaValidatorRequired buscarDePapa(String paraClase)
		throws XavaException {
		try {
			if (esEstereotipo(paraClase)) return null;
			if (esTipoPrimitivo(paraClase))
				return null;
			while (!paraClase.equals("java.lang.Object")) {
				paraClase = Class.forName(paraClase).getSuperclass().getName();
				MetaValidatorRequired v =
					(MetaValidatorRequired) metaValidatorsRequired.get(paraClase);
				if (v != null)
					return v;
			}
			return null;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible encontrar validador para "
					+ paraClase
					+ ". Clase no encontrada: "
					+ ex.getMessage());
		}
	}
	
	private static boolean esEstereotipo(String paraClase) {
		return paraClase.indexOf(".") < 0;
	}
	
	/**
	 * 
	 * @return boolean
	 * @param nombreClase java.lang.String
	 */
	private static boolean esTipoPrimitivo(String nombreClase) {
		return getPrimitiveTypes().contains(nombreClase);
	}
	/**
	 * 
	 * @return java.util.Collection
	 */
	private static Collection getPrimitiveTypes() {
		if (primitiveTypes == null) {
			primitiveTypes = new ArrayList();
			primitiveTypes.add("boolean");
			primitiveTypes.add("byte");
			primitiveTypes.add("char");
			primitiveTypes.add("short");
			primitiveTypes.add("int");
			primitiveTypes.add("long");
			primitiveTypes.add("float");
			primitiveTypes.add("double");
		}
		return primitiveTypes;
	}
	/**
	 * @exception XavaException Si no está registrado el validador con ese nombre, u otro problema.
	 */
	public static MetaValidator getMetaValidator(String nombre) throws XavaException {
		if (metaValidators == null) {
			metaValidators = new HashMap();
			metaValidatorsRequired = new HashMap();
			ValidatorsParser.configurarValidadores();
		}
		MetaValidator v = (MetaValidator) metaValidators.get(nombre);
		if (v == null) {
			throw new XavaException("validator_no_registered", nombre);
		}
		return v;
	}
	/**
	 * @return Nulo si no existe un validador para esa clase.
	 */
	public static MetaValidatorRequired getMetaValidatorRequiredFor(String tipoOEstereotipo)
		throws XavaException {
		if (metaValidatorsRequired == null) {
			metaValidators = new HashMap();
			metaValidatorsRequired = new HashMap();
			ValidatorsParser.configurarValidadores();
		}
		MetaValidatorRequired v =
			(MetaValidatorRequired) metaValidatorsRequired.get(tipoOEstereotipo);
		if (v == null) {
			v = buscarDePapa(tipoOEstereotipo);
			if (v != null) {
				metaValidatorsRequired.put(tipoOEstereotipo, v);
			}
		}
		return v;
	}
}