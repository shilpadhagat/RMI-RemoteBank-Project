// $Id: BankException.java 38 2016-03-31 21:50:38Z abcdef $

package cs735_835.remoteBank;

/**
 * Banking exceptions.  Instances of this class are immutable and serializable.  They have no cause
 * (i.e., {@code getCause} returns {@code null}).
 *
 * @author Michel Charpentier
 * @version 2.0
 */
public class BankException extends RuntimeException {

  private static final long serialVersionUID = -3011912973465419856L;

  BankException(String message) {
    super(message);
  }
}