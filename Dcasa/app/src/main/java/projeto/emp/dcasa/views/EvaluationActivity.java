package projeto.emp.dcasa.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.Professional;
import projeto.emp.dcasa.models.User;
import projeto.emp.dcasa.utils.MySharedPreferences;

public class EvaluationActivity extends AppCompatActivity {

    private MySharedPreferences preferences;

    private HashMap<String, Object> professionalDetails;
    private TextView tv_profession_name;
    private TextView tv_name_professional;
    private TextView tv_cpf_number;
    private RatingBar rat_bar_total;
    Button btn_evaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        preferences = new MySharedPreferences(getApplicationContext());
        professionalDetails = preferences.getProfessionalDetails();

        User user = new User("Maria");
        Professional professionalSelected = new Professional();

        tv_profession_name = (TextView) findViewById(R.id.tv_profession_name);
        tv_profession_name.setText((String)professionalDetails.get(MySharedPreferences.KEY_TYPE_PROFESSIONAL));

        tv_name_professional =  (TextView) findViewById(R.id.tv_name);
        tv_name_professional.setText((String)professionalDetails.get(MySharedPreferences.KEY_NAME_PROFESSIONAL));


        tv_cpf_number = (TextView) findViewById(R.id.tv_cpf_number);
        tv_cpf_number.setText((String)professionalDetails.get(MySharedPreferences.KEY_CPF_PROFESSIONAL));

        rat_bar_total = (RatingBar) findViewById(R.id.totality_bar);
        rat_bar_total.setRating((Float)professionalDetails.get(MySharedPreferences.KEY_NUM_EVALUATIONS));
        rat_bar_total.isIndicator();

        btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                preferences.deselectProfessional();
                new AlertDialog.Builder(EvaluationActivity.this)
                        .setMessage("Avaliação realizada com sucesso!")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setView(EvaluationActivity.this, MainActivity.class);
                            }
                        })
                        .create()
                        .show();
            }
        });

    }
    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

}
