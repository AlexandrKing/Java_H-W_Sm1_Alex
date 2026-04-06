package bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class BankTest {

  @Test
  void testSendToAccountBasic() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 500);

    bank.sendToAccount(account1, account2, 300);

    assertEquals(700, account1.getBalance());
    assertEquals(800, account2.getBalance());
  }

  @Test
  void testSendToAccountInsufficientFunds() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 100);
    BankAccount account2 = new BankAccount(2, 500);

    bank.sendToAccount(account1, account2, 300);

    assertEquals(100, account1.getBalance());
    assertEquals(500, account2.getBalance());
  }

  @Test
  void testSendToAccountConcurrent() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    int threadCount = 10;
    int transfersPerThread = 100;
    int amount = 1;

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          for (int j = 0; j < transfersPerThread; j++) {
            if (j % 2 == 0) {
              bank.sendToAccount(account1, account2, amount);
            } else {
              bank.sendToAccount(account2, account1, amount);
            }
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await(10, TimeUnit.SECONDS);
    executor.shutdown();

    int totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(2000, totalBalance, "Общий баланс должен остаться неизменным");
  }

  @Test
  @Timeout(5)
  void testSendToAccountNoDeadlock() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    int threadCount = 4;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      final int threadNum = i;
      executor.submit(() -> {
        try {
          for (int j = 0; j < 50; j++) {
            if (threadNum % 2 == 0) {
              bank.sendToAccount(account1, account2, 1);
            } else {
              bank.sendToAccount(account2, account1, 1);
            }
            Thread.sleep(10);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } finally {
          latch.countDown();
        }
      });
    }

    boolean completed = latch.await(4, TimeUnit.SECONDS);
    executor.shutdown();

    assertTrue(completed, "Все потоки должны завершиться без дедлока");

    int totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(2000, totalBalance, "Общий баланс должен остаться неизменным");
  }

  @Test
  void testSendToAccountDeadLockScenario() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    CountDownLatch latch = new CountDownLatch(2);
    Thread[] threads = new Thread[2];

    threads[0] = new Thread(() -> {
      try {
        latch.countDown();
        latch.await();
        bank.sendToAccountDeadLock(account1, account2, 100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }, "Thread-1");

    threads[1] = new Thread(() -> {
      try {
        latch.countDown();
        latch.await();
        bank.sendToAccountDeadLock(account2, account1, 100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }, "Thread-2");

    threads[0].start();
    threads[1].start();

    threads[0].join(2000);
    threads[1].join(2000);

    boolean thread1Alive = threads[0].isAlive();
    boolean thread2Alive = threads[1].isAlive();

    if (thread1Alive) threads[0].interrupt();
    if (thread2Alive) threads[1].interrupt();

    System.out.println("Thread 1 alive: " + thread1Alive);
    System.out.println("Thread 2 alive: " + thread2Alive);

    if (!thread1Alive && !thread2Alive) {
      int totalBalance = account1.getBalance() + account2.getBalance();
      assertEquals(2000, totalBalance, "Общий баланс должен остаться неизменным");
    }
  }

  @Test
  void testSendToAccountWithDifferentIds() {
    Bank bank = new Bank();
    BankAccount accountLow = new BankAccount(1, 1000);
    BankAccount accountHigh = new BankAccount(100, 1000);

    bank.sendToAccount(accountLow, accountHigh, 100);
    bank.sendToAccount(accountHigh, accountLow, 50);
    bank.sendToAccount(accountLow, accountHigh, 25);

    assertEquals(1000 - 100 + 50 - 25, accountLow.getBalance());
    assertEquals(1000 + 100 - 50 + 25, accountHigh.getBalance());
  }

  @Test
  void testAlternativeMethodConcurrent() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    int threadCount = 5;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          for (int j = 0; j < 20; j++) {
            bank.sendToAccountAlternative(account1, account2, 1);
            Thread.sleep(5);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } finally {
          latch.countDown();
        }
      });
    }

    boolean completed = latch.await(3, TimeUnit.SECONDS);
    executor.shutdown();

    assertTrue(completed, "Все потоки должны завершиться");

    int totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(2000, totalBalance, "Общий баланс должен остаться неизменным");
  }
}
