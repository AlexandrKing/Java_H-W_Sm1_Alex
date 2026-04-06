package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

  @Test
  void testDeposit() {
    BankAccount account = new BankAccount(1, 100);
    account.deposit(50);
    assertEquals(150, account.getBalance());
  }

  @Test
  void testWithdraw() {
    BankAccount account = new BankAccount(1, 100);
    account.withdraw(50);
    assertEquals(50, account.getBalance());
  }

  @Test
  void testWithdrawInsufficientFunds() {
    BankAccount account = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> account.withdraw(150));
  }

  @Test
  void testDepositNegativeAmount() {
    BankAccount account = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> account.deposit(-50));
  }

  @Test
  void testWithdrawNegativeAmount() {
    BankAccount account = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50));
  }

  @Test
  void testGetId() {
    BankAccount account = new BankAccount(42, 100);
    assertEquals(42, account.getId());
  }
}
