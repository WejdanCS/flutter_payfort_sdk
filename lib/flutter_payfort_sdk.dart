
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_payfort_sdk/models/colors_ext.dart';
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
  static Future<void>customizePaymentActivityColors(
      {
     required Color actionBarBackgroundColor,
        required Color actionBartTitleColor,
        required Color statusBarColor,
        required Color payButtonBackgroundColor,
        required Color payButtonTextColor

      }
      )async{
    try {
       await platform.invokeMethod(
          "customizePaymentActivityColors",{
            "action_bar_background_color": actionBarBackgroundColor.toHex(),
        "action_bar_title_color":actionBartTitleColor.toHex(),
        "status_bar_color":statusBarColor.toHex(),
        "pay_button_background_color":payButtonBackgroundColor.toHex(),
        "pay_button_text_color":payButtonTextColor.toHex()

          });
    }catch(err){
      rethrow;
    }

  }
}
