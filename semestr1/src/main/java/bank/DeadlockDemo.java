package bank;


public class DeadlockDemo {

  public static void main(String[] args) throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    System.out.println("Начальные балансы:");
    System.out.println("Счет 1: " + account1.getBalance());
    System.out.println("Счет 2: " + account2.getBalance());

    Thread thread1 = new Thread(() -> {
      System.out.println("Поток 1 пытается перевести с 1 на 2");
      bank.sendToAccountDeadLock(account1, account2, 100);
    }, "Transfer-1-to-2");

    Thread thread2 = new Thread(() -> {
      System.out.println("Поток 2 пытается перевести со 2 на 1");
      bank.sendToAccountDeadLock(account2, account1, 100);
    }, "Transfer-2-to-1");

    thread1.start();
    thread2.start();

    thread1.join(3000);
    thread2.join(3000);

    if (thread1.isAlive() || thread2.isAlive()) {
      System.out.println("\n⚡ ОБНАРУЖЕН ДЕДЛОК! Потоки заблокированы:");
      System.out.println("Поток 1 жив: " + thread1.isAlive());
      System.out.println("Поток 2 жив: " + thread2.isAlive());

      thread1.interrupt();
      thread2.interrupt();
    } else {
      System.out.println("\n✅ Переводы завершены успешно без дедлока");
      System.out.println("Конечные балансы:");
      System.out.println("Счет 1: " + account1.getBalance());
      System.out.println("Счет 2: " + account2.getBalance());
    }

    System.out.println("\n--- Тестирование безопасного метода ---");
    BankAccount account3 = new BankAccount(3, 1000);
    BankAccount account4 = new BankAccount(4, 1000);

    Thread thread3 = new Thread(() -> {
      bank.sendToAccount(account3, account4, 100);
    });

    Thread thread4 = new Thread(() -> {
      bank.sendToAccount(account4, account3, 100);
    });

    thread3.start();
    thread4.start();

    thread3.join();
    thread4.join();

    System.out.println("Безопасные переводы завершены:");
    System.out.println("Счет 3: " + account3.getBalance());
    System.out.println("Счет 4: " + account4.getBalance());
  }
}