// $Id: Operation.java 38 2016-03-31 21:50:38Z abcdef $

package cs735_835.remoteBank;

public abstract class Operation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int cents;
	public Operation(int cents) {
		super();
		this.cents = cents;
	}
	
	public int getCents() {
		return cents;
	}

	public static Operation deposit(int cents) {
    //throw new AssertionError("not implemented");
		return new OperationDeposit(cents);
  }

  public static Operation withdraw(int cents) {
    //throw new AssertionError("not implemented");
	  return new OperationWithdraw(cents);
  }

  public static Operation getBalance() {
    //throw new AssertionError("not implemented");
	  return new OperationBalance(-1);
  }
}