
import 'dart:convert';
import 'package:flutter/services.dart';
import 'models/payment_activity_args.dart';
import 'models/payment_activity_result.dart';

class FlutterPayfortSdk {
  static const channel="com.example.flutter_payfort_sdk/channels";
  static const platform= MethodChannel(channel);

  static Future<String?> getDeviceId()async{
    return await platform.invokeMethod("getDeviceId");
  }
  static Future<PaymentActivityResult?> goToPaymentActivity(PaymentActivityArgs args)async{
    PaymentActivityResult?paymentActivityResult;
    try {
      dynamic result = await platform.invokeMethod(
          "goToPaymentActivity", args.toJson());
      print("result:${result}");
      Map<String,dynamic> result2=json.decode(json.encode(result)) as Map<String, dynamic>;
      if(result!=null){
        paymentActivityResult=PaymentActivityResult.fromJson(result2);

      }else{
        throw("error occurred while return data");
      }
      return paymentActivityResult;
    }catch(err){
      rethrow;
    }
  }
}
