package projeto.emp.dcasa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.Professional;
import projeto.emp.dcasa.utils.MySharedPreferences;

public class EvaluationActivity extends AppCompatActivity {

    private MySharedPreferences preferences;

    private Professional professionalSelected;
    private TextView tv_profession_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        preferences = new MySharedPreferences(getApplicationContext());


//        professionalSelected = preferences.getProfessionalDetails();

        tv_profession_name = (TextView) findViewById(R.id.tv_profession_name);
        tv_profession_name.setText(professionalSelected.getType().getType());


    }

}
