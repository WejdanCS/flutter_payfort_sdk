# flutter_payfort_sdk

 Please before use this package read [PAYFORT](https://paymentservices-reference.payfort.com/docs/api/build/index.html#before-starting-your-integration-follow-these-steps) documentation .

## Platform Support
This plugin currently works only on android.
If you are a developer and want to add iOS support or fixing bugs you are welcome you can visit my Github repository [flutter_payfort_sdk](https://github.com/WejdanCS/flutter_payfort_sdk).
## Usage

To use this plugin, add `flutter_payfort_sdk` as a dependency in your pubspec.yaml file


### ScreenShots



## Example

Import the library.

```dart
import 'package:flutter_payfort_sdk/flutter_payfort_sdk.dart';
```

### 1. getDeviceId

```dart
    String? deviceId = await FlutterPayfortSdk.getDeviceId();

```

### 2. create SDK token
#####You can use our classes CreateTokenRequest and CreateTokenResponse to send data to server and return data from it
###(or you can use your own request and response but it is important to return `sdkToken` and `merchantReference` to use it in payfortsdk)

```dart
       CreateTokenRequest createTokenRequest = CreateTokenRequest(
          serviceCommand: "SDK_TOKEN",
          accessCode: dotenv.get("AccessCode"),
          merchantIdentifier: dotenv.get("MerchantIdentifier"),
          language: languageCode,
          deviceId: deviceId);
      http.Response result = await http.post(
          Uri.parse("${dotenv.get("BaseUrl")}/createSDKToken"),
          body: createTokenRequest.toJson());
      //3. return response with sdk_token and merchantReference
      CreateTokenResponse tokenRes =
          CreateTokenResponse.fromJson(jsonDecode(result.body));


```

### 3. goToPaymentActivity

```dart
  if (tokenRes.success != null && tokenRes.success == true) {
        //create object paymentActivityArgs which holds all required arguments for payfort sdk and payment activity
        PaymentActivityArgs paymentActivityArgs = PaymentActivityArgs(
            environment: ENVIRONMENT.test,
            command: COMMAND.purchase,
            customerEmail: dotenv.get("CustomerEmail"),
            languageCode: languageCode,
            currency: "SAR",
            amount: amount.toString(),
            sdkToken: tokenRes.result!.sdkToken,
            merchantReference: tokenRes.merchantReference,
            loadingMessage:
                languageCode == "ar" ? "جاري الدفع" : "Payment in Progress");
        // open native activity and send paymentActivityArgs
        PaymentActivityResult? paymentActivityResult =
            await FlutterPayfortSdk.goToPaymentActivity(paymentActivityArgs);
        //return result from native activity
        if (paymentActivityResult != null &&
            paymentActivityResult.success != null &&
            paymentActivityResult.success == true) {
          Fluttertoast.showToast(
              msg: "${paymentActivityResult.responseMessage}");
        } else {
          Fluttertoast.showToast(msg: "payment process is failed");
        }
      } else {
        Fluttertoast.showToast(msg: "payment process is failed");
      }
```


### 4. customizePaymentActivityColors

```dart
   // you can add custom colors - (OPTIONAL)
  // When you do not use this function the default colors will use
      await FlutterPayfortSdk.customizePaymentActivityColors(
          actionBarBackgroundColor:Colors.deepPurple,
          actionBartTitleColor: Colors.white,
          statusBarColor: Colors.deepPurpleAccent.shade100,
        payButtonBackgroundColor: Colors.deepPurpleAccent,
        payButtonTextColor: Colors.white
      );
```


### enum classes
```dart
/// select command name to send it to payfort sdk
enum COMMAND{
  purchase,
  authorization
}

/// select environment to send it to payfort sdk
/// test for test environment
/// and prod for production environment
enum ENVIRONMENT{
  test,
  prod

}
```


See the `main.dart` in the `example` for a complete example.


