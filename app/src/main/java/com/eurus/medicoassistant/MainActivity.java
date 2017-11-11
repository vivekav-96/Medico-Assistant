package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

/**
 * This activity will be the first activity where the
 * user can register his phone number.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText editText;
    private CardView registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAllViews();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(registerButton)) {
            String mobileNumber = editText.getText().toString();
            if (mobileNumber.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.mobile_num_empty),
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                registerMobileNumber(mobileNumber);
            }
        }
    }

    private void initAllViews() {
        editText = findViewById(R.id.tiet_mob_num);
        registerButton = findViewById(R.id.but_reg_mob_num);
    }

    /**
     * The OTP generation Logic goes here.
     *
     * @param mobileNumber (input type is set as phone number)
     */
    private void registerMobileNumber(String mobileNumber) {

    }
}
