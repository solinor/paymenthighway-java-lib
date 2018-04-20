package io.paymenthighway.formBuilders;

public class SiirtoParametersBuilder
    extends GenericPaymentParametersBuilder<SiirtoParametersBuilder>
    implements SiirtoParametersInterface {

    public SiirtoParametersBuilder(
        String method,
        String signatureKeyId,
        String signatureSecret,
        String account,
        String merchant,
        String baseUrl,
        String successUrl,
        String failureUrl,
        String cancelUrl,
        Long amount,
        String orderId,
        String description,
        String referenceNumber
    ) {
        super(
            method,
            signatureKeyId,
            signatureSecret,
            account,
            merchant,
            baseUrl,
            successUrl,
            failureUrl,
            cancelUrl,
            amount.toString(),
            "EUR",
            orderId,
            description
        );
        addNameValuePair(FormBuilderConstants.SPH_REFERENCE, referenceNumber);
        serviceUri = "/form/view/siirto";
    }

    /**
     * @param phoneNumber Customer phone number with country code e.q. +358449876543. Makes it easier for the customer to identify himself.
     * @return SiirtoParametersInterface
     */
    public SiirtoParametersInterface phoneNumber(String phoneNumber) {
        addNameValuePair(FormBuilderConstants.SPH_PHONE_NUMBER, phoneNumber);
        return this;
    }

}