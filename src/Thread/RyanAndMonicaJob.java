package Thread;

class BankAccount {
    private int balance = 100;

    public int getBalance(){
        return balance;
    }

    public void withdraw(int amount){
        balance = balance - amount;
    }


}
public class RyanAndMonicaJob implements  Runnable{
    private BankAccount account = new BankAccount();

    public static void main(String[] args){
        RyanAndMonicaJob theJob = new RyanAndMonicaJob();
        Thread One = new Thread(theJob);
        Thread Two = new Thread(theJob);
        One.setName("Ryan");
        Two.setName("Monica");
        One.start();
        Two.start();
    }

    @Override
    public void run() {
        for(int x = 0;x < 10;x++){
            makeWithdraw1(10);
            if(account.getBalance() < 0){
                System.out.println("Overdrawn!");
            }
        }
    }

    private void makeWithdraw1(int amount) {
        if(account.getBalance() >= amount){
            try {
                System.out.println(Thread.currentThread().getName() + " is about to withdraw");

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " woke up.");
            account.withdraw(amount);
            System.out.println(Thread.currentThread().getName() + " completes the withdrawal");

        }
        else{
            System.out.println("Sorry, not enough for " + Thread.currentThread().getName());
        }
    }
}
