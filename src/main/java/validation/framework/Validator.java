package validation.framework;

import validation.annotations.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {

  private static final String EMAIL_PATTERN =
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();

    if (object == null) {
      result.addError("Validated object cannot be null");
      return result;
    }

    Class<?> clazz = object.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      try {
        Object value = field.get(object);

        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull notNull = field.getAnnotation(NotNull.class);
          if (value == null) {
            result.addError(notNull.message());
          }
        }

        if (field.isAnnotationPresent(Size.class) && value instanceof String) {
          Size size = field.getAnnotation(Size.class);
          String stringValue = (String) value;
          int length = stringValue.length();

          if (length < size.min() || length > size.max()) {
            result.addError(size.message());
          }
        }

        if (field.isAnnotationPresent(Range.class) && value != null) {
          Range range = field.getAnnotation(Range.class);

          if (value instanceof Integer) {
            int intValue = (Integer) value;
            if (intValue < range.min() || intValue > range.max()) {
              result.addError(range.message());
            }
          } else if (value instanceof Long) {
            long longValue = (Long) value;
            if (longValue < range.min() || longValue > range.max()) {
              result.addError(range.message());
            }
          }
        }

        if (field.isAnnotationPresent(Email.class) && value instanceof String) {
          Email email = field.getAnnotation(Email.class);
          String stringValue = (String) value;

          if (stringValue != null && !isValidEmail(stringValue)) {
            result.addError(email.message());
          }
        }

      } catch (IllegalAccessException e) {
        result.addError("Cannot access field: " + field.getName());
      }
    }

    return result;
  }

  private static boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    return pattern.matcher(email).matches();
  }
}
