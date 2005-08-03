package org.openxava.validators.meta;

import java.util.*;

import org.openxava.util.*;
import org.openxava.validators.meta.xmlparse.*;

/**
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
	 * @return Null if not found
	 */
	private static MetaValidatorRequired findFromParent(String forType)
		throws XavaException {
		try {
			if (isStereotype(forType)) return null;
			if (isPrimitiveType(forType))
				return null;
			while (!forType.equals("java.lang.Object")) {
				forType = Class.forName(forType).getSuperclass().getName();
				MetaValidatorRequired v =
					(MetaValidatorRequired) metaValidatorsRequired.get(forType);
				if (v != null)
					return v;
			}
			return null;
		} 
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new XavaException("class_not_found_searching_validator", forType, ex.getMessage());
		}
	}
	
	private static boolean isStereotype(String forType) {
		return forType.indexOf(".") < 0;
	}
	
	private static boolean isPrimitiveType(String className) {
		return getPrimitiveTypes().contains(className);
	}

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
	 * @exception XavaException If the validator is not registered or another problem.
	 */
	public static MetaValidator getMetaValidator(String name) throws XavaException {
		if (metaValidators == null) {
			metaValidators = new HashMap();
			metaValidatorsRequired = new HashMap();
			ValidatorsParser.configureValidators();
		}
		MetaValidator v = (MetaValidator) metaValidators.get(name);
		if (v == null) {
			throw new XavaException("validator_no_registered", name);
		}
		return v;
	}
	
	/**
	 * @return Null if a validator for the clase is not found.
	 */
	public static MetaValidatorRequired getMetaValidatorRequiredFor(String typeOrStereotype)
		throws XavaException {
		if (metaValidatorsRequired == null) {
			metaValidators = new HashMap();
			metaValidatorsRequired = new HashMap();
			ValidatorsParser.configureValidators();
		}
		MetaValidatorRequired v =
			(MetaValidatorRequired) metaValidatorsRequired.get(typeOrStereotype);
		if (v == null) {
			v = findFromParent(typeOrStereotype);
			if (v != null) {
				metaValidatorsRequired.put(typeOrStereotype, v);
			}
		}
		return v;
	}
	
}