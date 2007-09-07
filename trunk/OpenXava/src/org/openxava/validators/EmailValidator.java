package org.openxava.validators;

import org.openxava.util.*;
import org.openxava.validators.IPropertyValidator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 
 * @author Janesh Kodikara
 */

public class EmailValidator implements IPropertyValidator {

    final private Pattern pat=Pattern.compile(".+@.+\\.[a-z]+");

    public void validate(Messages errors, Object value, String propertyName, String modelName) throws Exception {
        if (value == null || value.toString().length() ==0 ) return;
        Matcher matcher = pat.matcher(value.toString());
        if (! matcher.find()) {
           errors.add("email_validation_error", propertyName);
           return;
        }
    }

}