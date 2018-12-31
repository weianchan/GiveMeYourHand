package student.assignment.taruc.givemeyourhand;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fragmentManager = getSupportFragmentManager();

        getSupportActionBar().hide();

        if(savedInstanceState == null){
            fragmentManager.beginTransaction().replace(R.id.login_fragmentContainer, new LoginFragment(), global.LOGIN_FRAGMENT ).commit();
        }

    }
}
