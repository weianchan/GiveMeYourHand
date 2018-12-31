package student.assignment.taruc.givemeyourhand;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View view;

    private EditText email, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox showPassword;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);

        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_btn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.create_account);
        showPassword = view.findViewById(R.id.show_hide_password);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                }
                else{
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
            }
        });

        return view;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_btn:
                Toast.makeText(getActivity(), "Sign in", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forgot_password:
                Toast.makeText(getActivity(), "Forgot Password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create_account:
                Toast.makeText(getActivity(), "Create Account", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
