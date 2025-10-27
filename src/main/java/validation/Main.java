package validation;

import validation.framework.ValidationResult;
import validation.framework.Validator;
import validation.model.User;

public class Main {
  public static void main(String[] args) {
    User user = new User();
    user.setName("A");
    user.setEmail("invalid-email");
    user.setAge(200);
    user.setPassword("123");

    ValidationResult result = Validator.validate(user);

    System.out.println("Валидация прошла успешно: " + result.isValid());
    if (!result.isValid()) {
      System.out.println("Ошибки валидации:");
      for (String error : result.getErrors()) {
        System.out.println("- " + error);
      }
    }
  }
}
