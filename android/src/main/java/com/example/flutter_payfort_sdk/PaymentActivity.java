package com.example.flutter_payfort_sdk;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback;
import com.payfort.fortpaymentsdk.databinding.ActivityCcPaymentBinding;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;
import com.payfort.fortpaymentsdk.views.CardCvvView;
import com.payfort.fortpaymentsdk.views.CardExpiryView;
import com.payfort.fortpaymentsdk.views.CardHolderNameView;
import com.payfort.fortpaymentsdk.views.FortCardNumberView;
import com.payfort.fortpaymentsdk.views.PayfortPayButton;
import com.payfort.fortpaymentsdk.views.model.PayComponents;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private ActivityCcPaymentBinding binding;
    MySpinnerDialog myInstance;
    FragmentManager fm;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCcPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Activity activity=this;

        Bundle b = getIntent().getExtras();
        String languageCode = b.getString("language_code");

        String environment=b.getInt("environment")==0? FortSdk.ENVIRONMENT.TEST:FortSdk.ENVIRONMENT.PRODUCTION;
        String merchantReference=b.getString("merchant_reference");
        String sdkToken=b.getString("sdk_token");
        String command=b.getString("command");
        String customerEmail=b.getString("customer_email");
        String currency=b.getString("currency");
        String amount=b.getString("amount");
        String loadingMessage=b.getString("loading_message");

        setLocale(activity,languageCode);
        setTitle(R.string.app_name);
        setContentView(R.layout.payment_screen);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        PayfortPayButton payButton = findViewById(R.id.btntPay);
        payButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        payButton.setTextSize(16);
        FortCardNumberView etCardNumberView= findViewById(R.id.etCardNumberView);
        CardCvvView etCardCvv=findViewById(R.id.etCardCvv);
        CardExpiryView etCardExpiry=findViewById(R.id.etCardExpiry);
        CardHolderNameView cardHolderNameView=findViewById(R.id.cardHolderNameView);
//        create PayComponents object
        PayComponents payComponents= new PayComponents(etCardNumberView,etCardCvv,etCardExpiry,cardHolderNameView);

        FortRequest fortrequest = new FortRequest();

        try {
            if(merchantReference!=null&&sdkToken!=null){
                Map requestMap = new HashMap<>();
                requestMap.put("command", command);
                requestMap.put("customer_email", customerEmail);
                requestMap.put("currency", currency);
                requestMap.put("amount", amount);
                requestMap.put("language", languageCode);
                requestMap.put("merchant_reference", merchantReference);
                requestMap.put("sdk_token", sdkToken);
                fortrequest.setRequestMap(requestMap);

                PayFortCallback callback = new PayFortCallback() {

                    @Override
                    public void startLoading() {
                        if(myInstance==null&&fm==null){
                            fm = getSupportFragmentManager();
                            myInstance = new MySpinnerDialog(loadingMessage);
                            myInstance.setCancelable(false);


                        }
                        Log.i("...", "Loading:...");
                        if(!myInstance.isAdded()) {
                            myInstance.show(fm, "some_tag");

                        }
                    }

                    @Override
                    public void onSuccess(@NotNull Map<String, ?> requestParamsMap, @NotNull
                            Map<String, ?> fortResponseMap) {
                        Log.i("success", fortResponseMap.toString());
                        if(myInstance.getDialog().isShowing()) {
                            myInstance.dismiss();
                        }

                        Intent intent = new Intent();
                        intent.putExtra("success",true);
                        intent.putExtra("response_message",fortResponseMap.get("response_message").toString());

                        setResult(RESULT_OK, intent);
                        finish();





                    }



                    @Override
                    public void onFailure(@NotNull Map<String, ?> requestParamsMap, @NotNull Map<String, ?> fortResponseMap) {
                        Log.i("Failed:", fortResponseMap.toString());
                        if(myInstance.getDialog().isShowing()) {
                            myInstance.dismiss();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("success",false);
                        intent.putExtra("response_message",fortResponseMap.get("response_message").toString());

                        setResult(RESULT_OK, intent);
                        finish();

                    }

                };
                payButton.setup(environment, fortrequest, payComponents, callback);


            }else {
                Log.d("error","failed");
                Intent intent = new Intent();
                intent.putExtra("success",false);
                intent.putExtra("response_message","حصل خطأ ما...");

                setResult(RESULT_OK, intent);
                finish();
            }
        }
        catch (Exception ex)
        {
            Log.d("error message api:",ex.getMessage());
            Intent intent = new Intent();
            intent.putExtra("success",false);
            intent.putExtra("response_message","حصل خطأ ما...");

            setResult(RESULT_OK, intent);
            finish();

        }

    }
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }



}
