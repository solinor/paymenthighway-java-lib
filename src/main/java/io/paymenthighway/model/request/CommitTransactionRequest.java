package io.paymenthighway.model.request;

/**
 * Commit transaction request POJO
 */
public class CommitTransactionRequest {

  String amount;
  String currency;
  boolean blocking = true;

  public CommitTransactionRequest(String amount, String currency) {
    this.amount = amount;
    this.currency = currency;
  }

  /**
   * If the blocking parameter is set to false,
   * call will return immediately, without waiting
   * for the transaction to be fully processed.
   */
  public CommitTransactionRequest(String amount, String currency, boolean blocking) {
    this.amount = amount;
    this.currency = currency;
    this.blocking = blocking;
  }

  public String getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public boolean isBlocking() {
    return blocking;
  }
}
