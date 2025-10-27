package validation;

import validation.framework.ValidationResult;
import validation.framework.Validator;
import validation.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

  @Test
  void testSuccessfulValidation() {
    User user = new User("John Doe", "john@example.com", 25, "secure123");
    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNotNullValidation() {
    User user = new User(null, "john@example.com", 25, "secure123");
    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Имя не может быть null"));
  }

  @Test
  void testSizeValidation() {
    User user = new User("A", "john@example.com", 25, "short");
    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Имя должно быть от 2 до 50 символов"));
    assertTrue(result.getErrors().contains("Пароль должен быть от 6 до 20 символов"));
  }

  @Test
  void testRangeValidation() {
    User user = new User("John Doe", "john@example.com", -5, "secure123");
    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Возраст должен быть от 0 до 150"));
  }

  @Test
  void testEmailValidation() {
    User user = new User("John Doe", "invalid-email", 25, "secure123");
    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Некорректный формат email"));
  }

  @Test
  void testMultipleErrors() {
    User user = new User(null, "invalid", -1, "short");
    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertEquals(4, result.getErrors().size());
  }

  @Test
  void testNullObject() {
    ValidationResult result = Validator.validate(null);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Validated object cannot be null"));
  }

  @Test
  void testBoundaryValues() {
    User user1 = new User("Jo", "test@example.com", 0, "password123");
    User user2 = new User("John", "test@example.com", 150, "password123");

    assertTrue(Validator.validate(user1).isValid());
    assertTrue(Validator.validate(user2).isValid());
  }

  @Test
  void testEmailBoundaryCases() {
    User validEmail = new User("John", "valid@example.com", 25, "password123");
    User invalidEmail1 = new User("John", "invalid", 25, "password123");
    User invalidEmail2 = new User("John", "invalid@", 25, "password123");
    User invalidEmail3 = new User("John", "@example.com", 25, "password123");

    assertTrue(Validator.validate(validEmail).isValid());
    assertFalse(Validator.validate(invalidEmail1).isValid());
    assertFalse(Validator.validate(invalidEmail2).isValid());
    assertFalse(Validator.validate(invalidEmail3).isValid());
  }
}
