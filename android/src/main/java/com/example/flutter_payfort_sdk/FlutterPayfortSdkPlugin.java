package com.example.flutter_payfort_sdk;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payfort.fortpaymentsdk.FortSdk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
//  private Context context;
  private int REQUEST_CODE=0;
  private Result nativeResult;
  private FlutterActivity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "com.example.flutter_payfort_sdk/channels");
//    context=flutterPluginBinding.getApplicationContext();
    System.out.println("from onAttachedToEngine");
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

//                goToSecondActivity(languageCode);
      Intent i = new Intent(activity.getContext(), PaymentActivity.class);
      i.putExtra("language_code", languageCode);
      i.putExtra("environment", environment);
      i.putExtra("merchant_reference", merchantReference);
      i.putExtra("sdk_token", sdkToken);
      i.putExtra("command", command);
      i.putExtra("customer_email", customerEmail);
      i.putExtra("currency", currency);
      i.putExtra("amount", amount);
      i.putExtra("loading_message",loadingMessage);

      activity.startActivityForResult(i, REQUEST_CODE);

    }else if(call.method.equals("getDeviceId")){
      String _deviceId= FortSdk.getDeviceId(activity.getContext());
      result.success(_deviceId);

    }else{
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
    activity = (FlutterActivity)binding.getActivity();
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
        // A contact was picked.  Here we will just display it
        // to the user.
        // resultData=data;
        System.out.println("data:");
//                System.out.println();

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
