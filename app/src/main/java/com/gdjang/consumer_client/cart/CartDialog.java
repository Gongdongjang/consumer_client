package com.gdjang.consumer_client.cart;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gdjang.consumer_client.R;

public class CartDialog extends Dialog {

    ImageView btn_shutdown, btn_date, btn_time;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Spinner PurchaseNumSpinner;
    CheckBox BringBasketCheck;
    TextView PopupProdName, PopupProdNum, PopupProdPrice;
    Button GoCart;
    Button GoShopping;
    ImageView imageView;

    Boolean selectNum = false;

    Context mContext;

    CartDialog cartDialog;

    public CartDialog(@NonNull Context context, String user_id) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Log.d("유저아이디", user_id);

        cartDialog = this;

        GoCart = findViewById(R.id.Cart_GoCart);
        GoShopping = findViewById(R.id.Cart_GoShopping);
        GoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDialog.dismiss();
                Intent i = new Intent(v.getContext(), CartListActivity.class);
                i.putExtra("user_id",user_id);
                v.getContext().startActivity(i);
            }
        });

        GoShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDialog.dismiss();
            }
        });
    }
}
