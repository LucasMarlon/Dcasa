package projeto.emp.dcasa.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.User;
import projeto.emp.dcasa.utils.MySharedPreferences;

public class UserCadastreActivity extends AppCompatActivity {

    private AlertDialog.Builder alert;
    Context context;
    private EditText name;
    private EditText last_name;
    private EditText phone;
    private EditText email;
    private EditText password;
    private Button btn_cadastre;
    private MySharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cadastre);
        preferences = new MySharedPreferences(getApplicationContext());

        context = this;
        name = (EditText) findViewById(R.id.input_name);
        last_name = (EditText) findViewById(R.id.input_last_name);
        phone = (EditText) findViewById(R.id.input_phone);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        btn_cadastre = (Button) findViewById(R.id.btn_cadastre);
        btn_cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(name.getText().toString(),last_name.getText().toString(), email.getText().toString(), phone.getText().toString(), password.getText().toString());
                preferences.saveUser(user);
                new AlertDialog.Builder(UserCadastreActivity.this)
                        .setMessage("Cadastro realizado com sucesso!")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setView(UserCadastreActivity.this, MainActivity.class);
                            }
                        })
                        .create()
                        .show();

            }
        });


        MaskEditTextChangedListener maskPhone = new MaskEditTextChangedListener("(##)#####-####", phone);
        phone.addTextChangedListener(maskPhone);

    }

    public void showDialog(String message) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton("OK", null);
        alert.setMessage(message);
        alert.create().show();
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }


}