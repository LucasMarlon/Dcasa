package projeto.emp.dcasa.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.utils.MySharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login2;
    private MySharedPreferences userLogged;
    private AlertDialog.Builder alert;
    private Button btn_register2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        userLogged = new MySharedPreferences(getApplicationContext());

        btn_register2 = (Button) findViewById(R.id.btn_register2);
        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView(LoginActivity.this, UserCadastreActivity.class);
            }
        });

        btn_login2 = (Button) findViewById(R.id.btn_login2);
        btn_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLogged.isUserLoggedIn()) {
                    setView(LoginActivity.this, MainActivity.class);
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("Realize seu cadastro!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setView(LoginActivity.this, UserCadastreActivity.class);
                                }
                            })
                            .create()
                            .show();
                }

            }
        });
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    public void showDialog(String message) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton("OK", null);
        alert.setMessage(message);
        alert.create().show();
    }

}