package com.example.flutter_payfort_sdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payfort.fortpaymentsdk.FortSdk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

import static android.app.Activity.RESULT_OK;

/** FlutterPayfortSdkPlugin */
public class FlutterPayfortSdkPlugin implements FlutterPlugin, MethodCallHandler , ActivityAware, PluginRegistry.ActivityResultListener {
  private MethodChannel channel;
  private int REQUEST_CODE=0;
  private Result nativeResult;
  private FlutterActivity activity;
  String actionBarBackgroundColor;
  String actionBartTitleColor;
  String statusBarColor;
  String payButtonBackgroundColor;
  String payButtonTextColor;
  boolean setCustomColors=false;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "com.example.flutter_payfort_sdk/channels");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if(call.method.equals("goToPaymentActivity")){
      nativeResult=result;
      String languageCode = call.argument("language_code");
      int environment=call.argument("environment");
      String merchantReference=call.argument("merchant_reference");
      String sdkToken=call.argument("sdk_token");
      String command=call.argument("command");
      String customerEmail=call.argument("customer_email");
      String currency=call.argument("currency");
      String amount=call.argument("amount");
      String loadingMessage=call.argument("loading_message");

      Intent i = new Intent(activity.getApplicationContext(), PaymentActivity.class);
      i.putExtra("language_code", languageCode);
      i.putExtra("environment", environment);
      i.putExtra("merchant_reference", merchantReference);
      i.putExtra("sdk_token", sdkToken);
      i.putExtra("command", command);
      i.putExtra("customer_email", customerEmail);
      i.putExtra("currency", currency);
      i.putExtra("amount", amount);
      i.putExtra("loading_message",loadingMessage);
      i.putExtra("set_custom_colors",setCustomColors);
        if(setCustomColors==true) {
          i.putExtra("action_bar_background_color", Color.parseColor(actionBarBackgroundColor));
          i.putExtra("action_bar_title_color", Color.parseColor(actionBartTitleColor));
          i.putExtra("status_bar_color", Color.parseColor(statusBarColor));
          i.putExtra("pay_button_background_color", Color.parseColor(payButtonBackgroundColor));
          i.putExtra("pay_button_text_color", Color.parseColor(payButtonTextColor));
        }


      activity.startActivityForResult(i, REQUEST_CODE);

    }else if(call.method.equals("getDeviceId")){
      String _deviceId= FortSdk.getDeviceId(activity.getApplicationContext());
      result.success(_deviceId);

    }else if(call.method.equals("customizePaymentActivityColors")){
      setCustomColors=true;
      actionBarBackgroundColor=call.argument("action_bar_background_color");
      actionBartTitleColor=call.argument("action_bar_title_color");
      statusBarColor=call.argument("status_bar_color");
      payButtonBackgroundColor=call.argument("pay_button_background_color");
      payButtonTextColor=call.argument("pay_button_text_color");
      result.success(true);

    }
    else{
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
    System.out.println("onAttachedToActivity:");
    activity = (FlutterActivity) binding.getActivity();
    binding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
    Map result1 = new HashMap<>();
    if (requestCode == REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        if (data != null) {
          result1.put("success", data.getBooleanExtra("success", false));
          result1.put("response_message", data.getStringExtra("response_message"));
          nativeResult.success(result1
          );

        } else {
          result1.put("success", false);
          nativeResult.success(result1
          );
        }

        return true;

      }else {
        result1.put("success", false);
        nativeResult.success(result1
        );

      }
    }else
    {
      result1.put("success", false);
      nativeResult.success(result1
      );

    }
    return  false;

  }

}
