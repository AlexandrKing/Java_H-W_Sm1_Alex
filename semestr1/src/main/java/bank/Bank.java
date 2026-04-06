package bank;

public class Bank {

  public void sendToAccountDeadLock(BankAccount from, BankAccount to, int amount) {
    synchronized (from) {
      System.out.println(Thread.currentThread().getName() + " заблокировал счет " + from.getId());

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      synchronized (to) {
        System.out.println(Thread.currentThread().getName() + " заблокировал счет " + to.getId());

        if (from.getBalance() >= amount) {
          from.withdraw(amount);
          to.deposit(amount);
          System.out.println(Thread.currentThread().getName() + " перевел " + amount + " с " + from.getId() + " на " + to.getId());
        } else {
          System.out.println(Thread.currentThread().getName() + " недостаточно средств на счете " + from.getId());
        }
      }
    }
  }

  public void sendToAccount(BankAccount from, BankAccount to, int amount) {
    BankAccount first = from.getId() < to.getId() ? from : to;
    BankAccount second = from.getId() < to.getId() ? to : from;

    synchronized (first) {
      synchronized (second) {
        if (from.getBalance() >= amount) {
          from.withdraw(amount);
          to.deposit(amount);
          System.out.println(Thread.currentThread().getName() + " перевел " + amount + " с " + from.getId() + " на " + to.getId());
        } else {
          System.out.println(Thread.currentThread().getName() + " недостаточно средств на счете " + from.getId());
        }
      }
    }
  }

  public void sendToAccountAlternative(BankAccount from, BankAccount to, int amount) {
    synchronized (Bank.class) {
      if (from.getBalance() >= amount) {
        from.withdraw(amount);
        to.deposit(amount);
        System.out.println(Thread.currentThread().getName() + " перевел " + amount + " с " + from.getId() + " на " + to.getId());
      } else {
        System.out.println(Thread.currentThread().getName() + " недостаточно средств на счете " + from.getId());
      }
    }
  }
}
