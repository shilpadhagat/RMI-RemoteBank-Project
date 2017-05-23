// $Id: Record.java 38 2016-03-31 21:50:38Z abcdef $

package cs735_835.remoteBank;

/**
 * Records of bank operations.
 *
 * @author Michel Charpentier
 * @version 2.0
 */
public class Record implements java.io.Serializable {

  private static final long serialVersionUID = 6469802641474828290L;

  public static final String DEPOSIT_RECORD_TYPE = "deposit to account #%s";
  public static final String WITHDRAWAL_RECORD_TYPE = "withdrawal from account #%s";
  public static final String BALANCE_RECORD_TYPE = "balance of account #%s";

  /**
   * Record type (deposit, withdrawal or balance).
   *
   * @see #DEPOSIT_RECORD_TYPE
   * @see #WITHDRAWAL_RECORD_TYPE
   * @see #BALANCE_RECORD_TYPE
   */
  public final String type;

  /**
   * The amount associated with the operation (always nonnegative).
   */
  public final int cents;

  /**
   * String representation.
   *
   * @return a string of the form : {@code "type: $xx.xx"}
   * @see #type
   */
  @Override
  public String toString() {
    return String.format("%s: $%.2f", type, cents / 100.0);
  }

  Record(String type, int cents) {
    this.type = type;
    this.cents = cents;
  }

	public String getType() {
		return type;
	}
	
	public int getCents() {
		return cents;
	}

/*
  // for monitoring and debugging purposes
  public final String threadName = Thread.currentThread().getName();
  public final String hostName;
  {
    String h;
    try {
      h = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      h = "unknown";
    }
    hostName = h;
  }
*/
}
