package student.assignment.taruc.givemeyourhand;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        View mLayout = findViewById(R.id.sign_in_layout);
        mLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

    }
}
