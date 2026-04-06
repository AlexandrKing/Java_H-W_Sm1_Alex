package bank;

public class BankAccount {
  private final int id;
  private int balance;

  public BankAccount(int id, int initialBalance) {
    this.id = id;
    this.balance = initialBalance;
  }

  public int getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  public void deposit(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма депозита должна быть положительной");
    }
    balance += amount;
  }

  public void withdraw(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма снятия должна быть положительной");
    }
    if (amount > balance) {
      throw new IllegalArgumentException("Недостаточно средств на счете");
    }
    balance -= amount;
  }

  @Override
  public String toString() {
    return "BankAccount{id=" + id + ", balance=" + balance + '}';
  }
}
