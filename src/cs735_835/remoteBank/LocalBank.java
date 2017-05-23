// $Id: LocalBank.java 39 2016-04-01 00:30:35Z cs735a $

package cs735_835.remoteBank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class LocalBank extends UnicastRemoteObject implements Bank {
	private static final AtomicLong nextAccountNumber = new AtomicLong();
	private static final long serialVersionUID = 1L;
	public final String name;
	private Map<Long, RemoteAccount> accountMap;

  public LocalBank(String name) throws RemoteException {
    this.name = name;
    this.accountMap = new ConcurrentHashMap<Long, RemoteAccount>();
    //throw new AssertionError("not implemented");
  }

  @Override
  public String toString() {
    return "LocalBank: " + name;
  }

  public long[] getAllAccounts() {
    //throw new AssertionError("not implemented");
	  long[] accountIds = new long[this.accountMap.size()];
	  int idx = 0;
	  for(Long accountId : this.accountMap.keySet()){
		  accountIds[idx] = accountId;
		  idx++;
	  }
	  return accountIds;
  }

  public long openAccount() throws RemoteException {
	  long accountNumber = nextAccountNumber.incrementAndGet();
	  System.out.println("Opening account with account# - " + accountNumber);
    //throw new AssertionError("not implemented");
	  this.accountMap.put(accountNumber, new RemoteAccountImpl(accountNumber));
	  return accountNumber;
  }

  public RemoteAccount getRemoteAccount(long number) throws RemoteException{
	RemoteAccount remoteAccount = this.accountMap.get(number);
	if(remoteAccount == null){
		throw new RemoteException("Account doesn't exist for account# " + number);
	}
    return remoteAccount;
  }

  public BufferedAccount getBufferedAccount(long number) {
    //throw new AssertionError("not implemented");
	  RemoteAccount remoteAccount = this.accountMap.get(number);
	  if(remoteAccount == null){
		  System.err.println("No account exists for account# " + number);
		  throw new BankException("No account exists for account# " + number);
	  }
	  BufferedAccount bufferedAccount = null;
	  try{
		  bufferedAccount = new BufferedAccount(remoteAccount);
	  }catch(Exception e){
		  System.err.println("Exception - " + e);
	  }
	  return bufferedAccount;
  }

  public int closeAccount(long number) throws RemoteException{
    //throw new AssertionError("not implemented");
	  RemoteAccount remoteAccount = this.accountMap.get(number);
	  System.out.println("Closing account with account# - " + number);
	  if(remoteAccount == null){
		  System.err.println("No account exists for account# " + number);
		  throw new BankException("No account exists for account# " + number);
	  }
	  int balance = remoteAccount.getBalance().getCents();
	  this.accountMap.remove(number);
	  return balance;
  }

  public Record requestOperation(long number, Operation operation) throws RemoteException {
    //throw new AssertionError("not implemented");
	  RemoteAccount remoteAccount = this.accountMap.get(number);
	  if(remoteAccount == null){
		  System.err.println("No account exists for account# " + number);
		  throw new BankException("No account exists for account# " + number);
	  }
	  
	  if(operation == null){
		  System.err.println("Operation is null");
		  throw new BankException("Operation is null");
	  }
	  
	  if(operation instanceof OperationDeposit){
		  return remoteAccount.deposit(operation.getCents());
	  }else if(operation instanceof OperationWithdraw){
		  return remoteAccount.withdraw(operation.getCents());
	  }else if(operation instanceof OperationBalance){
		  return remoteAccount.getBalance();
	  }
	  throw new BankException("Unsupported operation - " + operation);
  }

  public Map<Long, Integer> currentBalances() { // non remote method
    //throw new AssertionError("not implemented");
	  Map<Long, Integer> currentBalances = new HashMap<Long, Integer>();
	  synchronized (this) {
		  for(Long accountNumber : this.accountMap.keySet()){
			  try{
				  currentBalances.put(accountNumber, this.accountMap.get(accountNumber).getBalance().getCents());
			  }catch(Exception e){
				  System.err.println("Omiting accountid " + accountNumber + "because of exception " + e);
			  }
		  }
	  }
	  return currentBalances;
  }
}
