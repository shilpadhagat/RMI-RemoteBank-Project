// $Id: BufferedAccount.java 38 2016-03-31 21:50:38Z abcdef $

package cs735_835.remoteBank;

import java.rmi.RemoteException;

public class BufferedAccount implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private RemoteAccount account;
	private int localCents;
	private long accountNumber;
	private int localChangesInCents;
	  public BufferedAccount(RemoteAccount account) throws RemoteException {
	    //throw new AssertionError("not implemented");
		  this.account = account;
		  this.accountNumber = account.accountNumber();
		  this.localCents = account.getBalance().getCents();
		  this.localChangesInCents = 0;
	  }
	
	  public long accountNumber() {
	    //throw new AssertionError("not implemented");
		  /*try{
		  return this.account.accountNumber();
		  }catch(Exception e){
			  System.err.println("Exception - " + e);
		  }
		  return -1;*/
		  return this.accountNumber;
	  }
	
	  public void deposit(int cents) {
	    //throw new AssertionError("not implemented");
		  /*try{
			  this.account.deposit(cents);
		  }catch(Exception e){
			  System.err.println("Exception - " + e);
		  }*/
		  //synchronized (this) {
			  if(cents < 0){
					System.err.println("BufferedAccount - Invalid deposit request for amount - " + cents);
					throw new BankException("BufferedAccount - Invalid deposit request for amount - " + cents);
			  }
			  this.localChangesInCents = this.localChangesInCents + cents;
		  //}
	  }
	
	  public void withdraw(int cents) {
	    //throw new AssertionError("not implemented");
		  /*try{
			  this.account.withdraw(cents);
		  }catch(Exception e){
			  System.err.println("Exception - " + e);
		  }*/
		  
		  //synchronized (this) {
			  if(cents < 0 || this.localCents + this.localChangesInCents - cents < 0){
				  System.err.println("BufferedAccount - Not enough balance. Cannot withdraw " + cents + " cents.");
				  throw new BankException("BufferedAccount - Not enough balance. Cannot withdraw " + cents + " cents.");
			  }
			  this.localChangesInCents = this.localChangesInCents - cents;
		  //}
	  }
	
	  public int balance() {
	    //throw new AssertionError("not implemented");
		  /*try{
			  return this.account.getBalance().getCents();
		  }catch(Exception e){
			  System.err.println("Exception - " + e);
		  }
		  return -1;*/
		  //synchronized (this) {
			  return this.localCents + this.localChangesInCents;
		  //}
	  }
	
	  public Record sync() throws RemoteException {
	    //throw new AssertionError("not implemented");
		  //synchronized (this) {
			  if(this.localChangesInCents > 0 ){
				  this.account.deposit(this.localChangesInCents);
			  }else if(this.localChangesInCents < 0){
				  this.account.withdraw(-this.localChangesInCents);
			  }
			  this.localChangesInCents = 0;
			  return this.account.getBalance();
		  //}
	  }
}
