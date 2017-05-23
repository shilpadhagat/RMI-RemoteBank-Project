package cs735_835;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

import cs735_835.remoteBank.Bank;
import cs735_835.remoteBank.BankException;
import cs735_835.remoteBank.BankServer;
import cs735_835.remoteBank.BufferedAccount;
import cs735_835.remoteBank.Operation;
import cs735_835.remoteBank.RemoteAccount;

public class LoadTestClient {
	private final Bank bank;
	private static Random random = new Random();
	  private LoadTestClient(String service)
	      throws RemoteException, NotBoundException, MalformedURLException {
		  System.out.println("Service - " + service);
	    bank = (Bank) java.rmi.Naming.lookup(service);
	  }

	  private void createAccount() throws RemoteException {
	    System.out.printf("account created: %d%n", bank.openAccount());
	  }

	  private void closeAccount(long id) throws RemoteException {
	    System.out.printf("account closed with balance: %d cents%n", bank.closeAccount(id));
	  }

	  private void runTestsRemote(long number) throws RemoteException {
	    RemoteAccount account = bank.getRemoteAccount(number);
	    System.out.println(account.getBalance());
	    Scanner in = new Scanner(System.in); // not closed on purpose
	    try {
	      while (true) {
	        //String line = in.nextLine().trim();
	    	  String line;
	    	  int choices = random.nextInt(4);
	    	  if(choices == 0){
	    		  line = "0";
	    	  }else if(choices == 1){
	    		  line = "" + (random.nextInt(100) + random.nextDouble());
	    	  }else if(choices == 2){
	    		  line = "" + (0-random.nextInt(100) - random.nextDouble());
	    	  }else{
	    		  line = "";
	    	  }
	        if (line.isEmpty()) {
	          System.out.println(account.getBalance());
	          continue;
	        }
	        double amount = Double.parseDouble(line);
	        int cents = (int) Math.round(amount * 100);
	        if (amount == 0)
	          break;
	        if (amount > 0) {
	          System.out.println(account.deposit(cents));
	          System.out.println(account.getBalance());
	        } else {
	          try {
	            System.out.println(account.withdraw(-cents));
	            System.out.println(account.getBalance());
	          } catch (BankException e) {
	            System.out.printf("bank exception: %s%n", e.getMessage());
	          }
	        }
	      }
	    } catch (java.util.NoSuchElementException e) {
	      /* EOF */
	    }
	  }

	  private void runTestsOperation(long account) throws RemoteException {
	    System.out.println(bank.requestOperation(account, Operation.getBalance()));
	    Scanner in = new Scanner(System.in); // not closed on purpose
	    try {
	      while (true) {
	        //String line = in.nextLine().trim();
	    	  String line;
	    	  int choices = random.nextInt(4);
	    	  if(choices == 0){
	    		  line = "0";
	    	  }else if(choices == 1){
	    		  line = "" + (random.nextInt(100) + random.nextDouble());
	    	  }else if(choices == 2){
	    		  line = "" + (0-random.nextInt(100) - random.nextDouble());
	    	  }else{
	    		  line = "";
	    	  }
	        if (line.isEmpty()) {
	          System.out.println(bank.requestOperation(account, Operation.getBalance()));
	          continue;
	        }
	        double amount = Double.parseDouble(line);
	        int cents = (int) Math.round(amount * 100);
	        if (amount == 0)
	          break;
	        if (amount > 0) {
	          System.out.println(bank.requestOperation(account, Operation.deposit(cents)));
	          System.out.println(bank.requestOperation(account, Operation.getBalance()));
	        } else {
	          try {
	            System.out.println(bank.requestOperation(account, Operation.withdraw(-cents)));
	            System.out.println(bank.requestOperation(account, Operation.getBalance()));
	          } catch (BankException e) {
	            System.out.printf("bank exception: %s%n", e.getMessage());
	          }
	        }
	      }
	    } catch (java.util.NoSuchElementException e) {
	      /* EOF */
	    }
	  }

	  private void runTestsBuffered(long number) throws RemoteException {
	    BufferedAccount account = bank.getBufferedAccount(number);
	    System.out.printf("balance: %d cents%n", account.balance());
	    //Scanner in = new Scanner(System.in); // not closed on purpose
	    try {
	      while (true) {
	        //String line = in.nextLine().trim();
	    	  String line;
	    	  int choices = random.nextInt(4);
	    	  if(choices == 0){
	    		  line = "0";
	    	  }else if(choices == 1){
	    		  line = "" + (random.nextInt(100) + random.nextDouble());
	    	  }else if(choices == 2){
	    		  line = "" + (0-random.nextInt(100) - random.nextDouble());
	    	  }else{
	    		  line = "";
	    	  }
	    	  
	        if (line.isEmpty()) {
	          System.out.printf("balance: %d cents%n", account.balance());
	          continue;
	        }
	        double amount = Double.parseDouble(line);
	        int cents = (int) Math.round(amount * 100);
	        if (amount == 0) {
	          System.out.println(account.sync());
	          System.out.printf("balance: %d cents%n", account.balance());
	          break;
	        }
	        if (amount > 0) {
	          account.deposit(cents);
	          System.out.printf("balance: %d cents%n", account.balance());
	        } else {
	          try {
	            account.withdraw(-cents);
	            System.out.printf("balance: %d cents%n", account.balance());
	          } catch (BankException e) {
	            System.out.printf("bank exception: %s%n", e.getMessage());
	          }
	        }
	      }
	    } catch (java.util.NoSuchElementException e) {
	      /* EOF */
	    }
	  }
	
	private static void runTestClient(String[] args) throws Exception{
		//String[] args = {"localhost:55555", null, null};
		System.out.println("args[0] is " + args[0]);
	    String service = "rmi://" + args[0] + "/" + BankServer.BANK_NAME;

	    LoadTestClient client = new LoadTestClient(service);
	    try {
	      if (args.length == 1) {
	        client.createAccount();
	        return;
	      }
	      long id = Long.parseLong(args[1]);
	      if (id < 0) {
	        client.closeAccount(-id);
	        return;
	      }
	      String type = (args.length > 2) ? args[2] : "R";
	      switch (type) {
	        case "0":
	          client.runTestsBuffered(id);
	          break;
	        case "1":
	          client.runTestsOperation(id);
	          break;
	        default:
	          client.runTestsRemote(id);
	      }
	    } catch (Exception e) {
	      System.out.printf("bank exception: %s%n", e.getMessage());
	    }
	}
	
	private static String[] randomArr(String hostPort, int numberOfCreatedAccounts, int choice){
		
		String[] arr;
		switch(choice){
			/*case 0:
				// Create account
				arr = new String[1];
				arr[0] = hostPort;
				//counter++;
				break;
			/*case 1:
				// Close account
				arr = new String[2];
				arr[0] = hostPort;
				arr[1] = ""+(-1-random.nextInt(counter));
				break;*/
			case 1:
				// Buffered tests
				arr = new String[3];
				arr[0] = hostPort;
				arr[1] = "" + (1+random.nextInt(numberOfCreatedAccounts));
				arr[2] = "0";
				break;
			case 2:
				// operation tests
				arr = new String[3];
				arr[0] = hostPort;
				arr[1] = "" + (1+random.nextInt(numberOfCreatedAccounts));
				arr[2] = "1";
				break;
			default:
				// Remote tests
				arr = new String[2];
				arr[0] = hostPort;
				arr[1] = "" + (1+random.nextInt(numberOfCreatedAccounts));
				break;
		}
		return arr;
	}
	
	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter server host port!!");
		String hostPort = in.next();
		System.out.println("Enter the number of operations you want to perform randomly");
		int numberOfOperations = in.nextInt();
		System.out.println("Enter your choice - 0 - Create accounts for setup, 1 - Buffered, 2 - Operation  and 3 or default - Remote Test");
		int choice = in.nextInt();
		int numberOfCreateOperations = 100;
		if(numberOfOperations > 0){
			int counter = 0;
			String[] arr;
			if(choice == 0 ){
				arr = new String[1];
				arr[0] = hostPort;
				while(counter < numberOfCreateOperations){
					runTestClient(arr);
					counter++;
				}
			}else{
				while(counter < numberOfOperations){
					arr = randomArr(hostPort, numberOfCreateOperations, choice);
					runTestClient(arr);
					counter++;
				}
			}
		}
	}
}
