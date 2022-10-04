
import '../enums/enums.dart';

class PaymentActivityArgs {
  ENVIRONMENT? environment;
  COMMAND? command;
  String? customerEmail;
  String? languageCode;
  String? currency;
  String? amount;
  String? sdkToken;
  String? merchantReference;
  String? loadingMessage;

  PaymentActivityArgs(
      {required this.environment,
        required this.command,
       required this.customerEmail,
       required this.languageCode,
       required this.currency,
       required this.amount,
       required this.sdkToken,
       required this.merchantReference,
        required this.loadingMessage
      });

  PaymentActivityArgs.fromJson(Map<String, dynamic> json) {
    environment = json['environment'];
    command = json['command'];
    customerEmail = json['customer_email'];
    languageCode = json['language_code'];
    currency = json['currency'];
    amount = json['amount'];
    sdkToken = json['sdk_token'];
    merchantReference = json['merchant_reference'];
    loadingMessage=json["loading_message"];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['environment'] = environment==ENVIRONMENT.test?0:1;
    data['command'] = command==COMMAND.purchase?"PURCHASE":"AUTHORIZATION";
    data['customer_email'] = customerEmail;
    data['language_code'] = languageCode;
    data['currency'] = currency;
    data['amount'] = amount;
    data['sdk_token'] = sdkToken;
    data['merchant_reference'] = merchantReference;
    data["loading_message"]=loadingMessage;
    return data;
  }
}

