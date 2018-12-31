package student.assignment.taruc.givemeyourhand;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private static EditText email, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox showPassword;


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

        

        return view;

    }

    @Override
    public void onClick(View view) {

    }
}
