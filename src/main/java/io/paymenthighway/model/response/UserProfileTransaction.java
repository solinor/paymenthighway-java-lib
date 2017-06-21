package io.paymenthighway.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.paymenthighway.model.response.masterpass.MasterpassInfo;
import io.paymenthighway.model.response.profile.ProfileInfo;

public class UserProfileTransaction {

  private ProfileInfo profile;

  private PartialCard card;
  private Customer customer;
  @JsonProperty("cardholder_authentication")
  private String cardholderAuthentication;

  private MasterpassInfo masterpass;

  /**
   * @return user information
   */
  public ProfileInfo getProfile() {
    return profile;
  }

  public PartialCard getCard() {
    return this.card;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getCardholderAuthentication() {
    return cardholderAuthentication;
  }

  /**
   * @return masterpass transaction information if masterpass walled was used
   */
  public MasterpassInfo getMasterpass() {
    return masterpass;
  }
}
