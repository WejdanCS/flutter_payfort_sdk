import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:flutter_payfort_sdk/enums/enums.dart';
import 'package:flutter_payfort_sdk/flutter_payfort_sdk.dart';
import 'package:flutter_payfort_sdk/models/create_token_request.dart';
import 'package:flutter_payfort_sdk/models/create_token_response.dart';
import 'package:flutter_payfort_sdk/models/payment_activity_args.dart';
import 'package:flutter_payfort_sdk/models/payment_activity_result.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;

void main() async {
  await dotenv.load(fileName: ".env");
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  bool isLoading = false;
  String languageCode = "en";
  int amount = 100;

  _openActivity() async {
    try {
      setState(() {
        isLoading = true;
      });
      //1. get deviceId
      String? deviceId = await FlutterPayfortSdk.getDeviceId();
      //2. create SDK token
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

      if (tokenRes.success != null && tokenRes.success == true) {
        //4. create object paymentActivityArgs which holds all required arguments for payfort sdk and payment activity
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
        //4. open native activity and send paymentActivityArgs
        PaymentActivityResult? paymentActivityResult =
            await FlutterPayfortSdk.goToPaymentActivity(paymentActivityArgs);
        //5. return result from native activity
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
      setState(() {
        isLoading = false;
      });
    } catch (err) {
      setState(() {
        isLoading = false;
      });
      Fluttertoast.showToast(msg: "payment process is failed");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          ElevatedButton(
            onPressed: isLoading
                ? null
                : () async {
                    await _openActivity();
                  },
            child: const Text("Pay"),
          ),
          isLoading ? const CircularProgressIndicator() : Container()
        ],
      ),
    ));
  }
}
