package cs735_835.remoteBank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteAccountImpl extends UnicastRemoteObject implements RemoteAccount {
	private static final long serialVersionUID = 1L;
	private long accountNumber;
	private volatile int cents;

	protected RemoteAccountImpl() throws RemoteException {
		super();
	}
	
	protected RemoteAccountImpl(long accountNumber) throws RemoteException {
		this();
		this.accountNumber = accountNumber;
	}

	@Override
	public long accountNumber() throws RemoteException {
		return this.accountNumber;
	}

	@Override
	public Record deposit(int cents) throws RemoteException {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(cents < 0){
				System.err.println("Invalid deposit request for amount - " + cents);
				throw new BankException("Invalid deposit request for amount - " + cents);
			}
			this.cents = this.cents + cents;
			return new Record(Record.DEPOSIT_RECORD_TYPE, this.cents);
		}
	}

	@Override
	public Record withdraw(int cents) throws RemoteException {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(cents < 0 || cents > this.cents){
				System.err.println("Invalid withdrawal request for amount - " + cents);
				throw new BankException("Invalid withdrawal request for amount - " + cents);
			}
			this.cents = this.cents - cents;
			return new Record(Record.WITHDRAWAL_RECORD_TYPE, this.cents);
		}
	}

	@Override
	public Record getBalance() throws RemoteException {
		// TODO Auto-generated method stub
		return new Record(Record.BALANCE_RECORD_TYPE, this.cents);
	}

}
