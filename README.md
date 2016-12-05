# Payment Highway Java Client
Payment Highway Java API Library

This is an example implementation of the communication with the Payment Highway API using Java. The Form API and Payment API implement the basic functionality of the Payment Highway.

This code is provided as-is, use it as inspiration, reference or drop it directly into your own project and use it.

For full documentation on the PaymentHighway API visit our developer website: https://paymenthighway.fi/dev/

The Java Client is a Maven project built so that it will work on Java 1.7 and Java 1.8. It requires the following third party frameworks: Apache HttpComponents and Jackson JSON. It also uses JUnit test packages.

**_Note:_**
At version 1.5 we changed `FormBuilder` to use _bulder pattern_. Old methods are deprecated. From now on optional parameters can be added to forms more easily (see [Example: How to use optional parameters](#example)). 

## Installation

### Maven

Add sonatype-snapshots repository to pom.xml:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <name>Sonatype Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

Add as dependency:

```xml
    <dependencies>
        <dependency>
            <groupId>io.paymenthighway</groupId>
            <artifactId>paymenthighway</artifactId>
            <version>1.5-SNAPSHOT</version>
        </dependency>
    </dependencies>
```


# Structure 

* `io.paymenthighway`

Contains API classes. Use these to create Payment Highway API requests.

* `io.paymenthighway.connect`

Contains the actual classes that are responsible of the communication with Payment Highway.

* `io.paymenthighway.exception`

Contains a custom authentication exception.

* `io.paymenthighway.json`

Contains classes that serialize and deserialize objects to and from JSON.

* `io.paymenthighway.model`

Data structures that will be serialized and deserialized

* `io.paymenthighway.security`

Contains classes that take care of keys and signatures.

# Overview

## FormBuilder
Start with building the HTTP form parameters by using the FormParameterBuilder. 

- `FormBuilder`

Create an instance of the builder, then use the generate methods to receive a list of parameters for each API call.

    import io.paymenthighway.FormBuilder;

### Initializing the builder


    String method = "POST";
    String signatureKeyId = "testKey";
    String signatureSecret = "testSecret";
    String account = "test";
    String merchant = "test_merchantId";
    String serviceUrl = "https://v1-hub-staging.sph-test-solinor.com";

    FormBuilder formBuilder = new FormBuilder(method, signatureKeyId, signatureSecret, account, merchant, serviceUrl);
    
### Example common parameters for the following form generation functions
 
    String successUrl = "https://example.com/success";
    String failureUrl = "https://example.com/failure";
    String cancelUrl = "https://example.com/cancel";
    String language = "EN";

### Example add card parameters

    FormContainer formContainer = formBuilder.addCardParameters(successUrl, failureUrl, cancelUrl, language).build()

    // read form parameters
    String httpMethod = formContainer.getMethod();
    String actionUrl = formContainer.getAction();
    List<NameValuePair> fields = formContainer.getFields();
    
    System.out.println("Initialized form with request-id: " + formContainer.getRequestId());

    for (NameValuePair field : fields) {
        field.getName();
        field.getValue();
    }
#### Optional parameters
 Parameter | type 
-----------|------
acceptCvcRequired | bool
skipFormNotifications | bool
exitIframeOnResult | bool
exitIframeOn3ds | bool
use3ds | bool

#### <a name="example"></a> Example: How to use optional parameters
    
    FormContainer formContainer = formBuilder.addCardParameters(successUrl, failureUrl, cancelUrl, language)
        .skipFormNotifications(true)
        .exitIframeOnResult(true)
        .use3ds(false)
        .build()
    
### Example payment parameters 

    String amount = "1990";
    String currency = "EUR";
    String orderId = "1000123A";
    String description = "A Box of Dreams. 19,90€";

     FormContainer formContainer = formBuilder.paymentParameters(
                successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description)
                .build();

    // read form parameters
    String httpMethod = formContainer.getMethod();
    String actionUrl = formContainer.getAction();
    List<NameValuePair> fields = formContainer.getFields();
    
    System.out.println("Initialized form with request-id: " + formContainer.getRequestId());

    for (NameValuePair field : fields) {
        field.getName();
        field.getValue();
    }
#### Optional parameters
 Parameter | type 
-----------|------
skipFormNotifications | bool
exitIframeOnResult | bool
exitIframeOn3ds | bool
use3ds | bool        	

### Example add card and payment parameters

    String amount = "1990";
    String currency = "EUR";
    String orderId = "1000123A";
    String description = "A Box of Dreams. 19,90€";

    FormContainer formContainer = formBuilder.addCardAndPaymentParameters(
                successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description)
                .build();

    // read form parameters
    String httpMethod = formContainer.getMethod();
    String actionUrl = formContainer.getAction();
    List<NameValuePair> fields = formContainer.getFields();

    System.out.println("Initialized form with request-id: " + formContainer.getRequestId());

    for (NameValuePair field : fields) {
        field.getName();
        field.getValue();
    }
#### Optional parameters
 Parameter | type 
-----------|------
skipFormNotifications | bool
exitIframeOnResult | bool
exitIframeOn3ds | bool
use3ds | bool        

### Example pay with token and CVC
    String amount = "1990";
    String currency = "EUR";
    String orderId = "1000123A";
    String description = "A Box of Dreams. 19,90€";
    UUID token = UUID.fromString("*TOKEN*");
    
    FormContainer formContainer = formBuilder.payWithTokenAndCvcParameters(
                successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description, token)
                .build();

    // read form parameters
    String httpMethod = formContainer.getMethod();
    String actionUrl = formContainer.getAction();
    List<NameValuePair> fields = formContainer.getFields();

    System.out.println("Initialized form with request-id: " + formContainer.getRequestId());

    for (NameValuePair field : fields) {
        field.getName();
        field.getValue();
    }
    
#### Optional parameters
 Parameter | type 
-----------|------
skipFormNotifications | bool
exitIframeOnResult | bool
exitIframeOn3ds | bool
use3ds | bool         

### Example MobilePay form payment

    String amount = "1990";
    String currency = "EUR";
    String orderId = "1000123A";
    String description = "A Box of Dreams. 19,90€";

    FormContainer formContainer = formBuilder.mobilePayParametersBuilder(successUrl, failureUrl, cancelUrl,
                language, amount, currency, orderId, description)
                .build();

    // read form parameters
    String httpMethod = formContainer.getMethod();
    String actionUrl = formContainer.getAction();
    List<NameValuePair> fields = formContainer.getFields();

    System.out.println("Initialized form with request-id: " + formContainer.getRequestId());

    for (NameValuePair field : fields) {
        field.getName();
        field.getValue();
    }
#### Optional parameters
 Parameter | type |
-----------|------| -------
exitIframeOnResult | bool |
shopLogoUrl | string | The logo must be 250x250 pixel in .png format and must be hosted on a HTTPS (secure) server.
phoneNumber | string | Customer phone number with country code e.q. +358449876543.
shopName | string |  Max 100 AN. If omitted, the merchant name from PH is used.
subMerchantId | string | Max 15 AN. Should only be used by a Payment Facilitator customer
subMerchantName | string | Max 21 AN. Should only be used by a Payment Facilitator customer

_MobilePay payment is to be committed as any other Form Payment_

Each method returns a FormContainer object which provides required hidden fields for the HTML form to make a successful transaction to Form API. The builder will generate a request id, timestamp, and secure signature for the transactions, which are included in the FormContainer fields.

In order to charge a card given in the Form API, the corresponding transaction id must be committed by using Payment API.

In addition, after the user is redirected to one of your provided success, failure or cancel URLs, you should validate the request parameters and the signature.

### Example validateFormRedirect

    SecureSigner secureSigner = new SecureSigner(signatureKeyId, signatureSecret);

    if ( ! secureSigner.validateFormRedirect(requestParams)) {
      throw new Exception("Invalid signature!");
    }


## PaymentApi

In order to do safe transactions, an execution model is used where the first call to /transaction acquires a financial transaction handle, later referred as “ID”, which ensures the transaction is executed exactly once. Afterwards it is possible to execute a debit transaction by using the received id handle. If the execution fails, the command can be repeated in order to confirm the transaction with the particular id has been processed. After executing the command, the status of the transaction can be checked by executing the PaymentAPI.transactionStatus("id") request. 

In order to be sure that a tokenized card is valid and is able to process payment transactions the corresponding tokenization id must be used to get the actual card token. 

    import io.paymenthighway.PaymentAPI;

### Initializing the Payment API

    String serviceUrl = "https://v1-hub-staging.sph-test-solinor.com";
    String signatureKeyId = "testKey";
    String signatureSecret = "testSecret";
    String account = "test";
    String merchant = "test_merchantId";

    try (PaymentAPI paymentAPI = new PaymentAPI(serviceUrl, signatureKeyId, signatureSecret, account, merchant)) {
        // Payment API usage
    }
        
### Example Commit Form Transaction

    String transactionId = ""; // get sph-transaction-id as a GET parameter
    String amount = "1999";
    String currency = "EUR";
    CommitTransactionResponse response = paymentAPI.commitTransaction(transactionId, amount, currency);

### Example Init transaction

	InitTransactionResponse initResponse = paymentAPI.initTransaction();
	
### Example Tokenize (get the actual card token by using token id)

	TokenizationResponse tokenResponse = paymentAPI.tokenize("tokenizationId");
			
### Example Debit with Token

    Token token = new Token("id");
    long amount = 1095;
    String currency = "EUR";
    TransactionRequest transaction = new TransactionRequest.Builder(token, amount, currency).build();
    TransactionResponse response = paymentAPI.debitTransaction("transactionId", transaction);

### Example Revert

	TransactionResponse response = paymentAPI.revertTransaction("transactionId", "amount");

### Example Transaction Status

	TransactionStatusResponse status = paymentAPI.transactionStatus("transactionId");
	
### Example Daily Batch Report

	ReportResponse report = paymentAPI.fetchDailyReport("yyyyMMdd");
	
### Example Order Status

    OrderSearchResponse orderSearchResponse = paymentAPI.searchOrders("order");
	

# Errors

Payment Highway API can raise exceptions for several reasons. Payment Highway authenticates each request and if there is invalid parameters or a signature mismatch, a HttpResponseException is raised.

The Payment Highway Java client also authenticates response messages, and in case of signature mismatch an AuthenticationException will be raised.

	try {
 		// Use Payment Highway's bindings...
	} catch (AuthenticationException e) {
 		// signals a failure to authenticate Payment Highway response
	} catch (HttpResponseException e) {
  		// Signals a non 2xx HTTP response.
  		// Invalid parameters were supplied to Payment Highway's API
	} catch (IOException e) {
  		// Signals that an I/O exception of some sort has occurred
	} catch (Exception e) {
  		// Something else happened
	}

It is recommended to gracefully handle exceptions from the API.

# Help us make it better

Please tell us how we can make the API better. If you have a specific feature request or if you found a bug, please use GitHub issues. Fork these docs and send a pull request with improvements.

