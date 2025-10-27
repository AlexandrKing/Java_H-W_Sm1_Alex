package validation.framework;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  private boolean isValid;
  private List<String> errors;

  public ValidationResult() {
    this.isValid = true;
    this.errors = new ArrayList<>();
  }

  public boolean isValid() {
    return isValid;
  }

  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }

  public void addError(String error) {
    this.errors.add(error);
    this.isValid = false;
  }

  @Override
  public String toString() {
    return "ValidationResult{" +
        "isValid=" + isValid +
        ", errors=" + errors +
        '}';
  }
}
