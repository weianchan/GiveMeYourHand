package student.assignment.taruc.givemeyourhand;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfo extends Fragment implements View.OnClickListener {

    private EditText username, phone;
    private Button confirmBtn;
    private ProgressBar loadingBar;


    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;



    public UpdateInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        username = view.findViewById(R.id.change_user_name);
        phone = view.findViewById(R.id.change_user_phone);
        confirmBtn = view.findViewById(R.id.confirm_update_info);
        loadingBar = view.findViewById(R.id.update_loading_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        confirmBtn.setOnClickListener(this);


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        loadingBar.setVisibility(View.VISIBLE);
        username.setText(currentUser.getDisplayName());
        firebaseDatabase.getReference().child("User").child(currentUser.getUid()).child("Phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone.setText(dataSnapshot.getValue(String.class));
                loadingBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm_update_info){
            if(checkValidation()){
                loadingBar.setVisibility(View.VISIBLE);
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username.getText().toString())
                        .build();
                currentUser.updateProfile(profileUpdate);
                firebaseDatabase.getReference().child("User").child(currentUser.getUid()).child("Phone").setValue(phone.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Update Success.", Toast.LENGTH_SHORT);
                        loadingBar.setVisibility(View.INVISIBLE);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new ProfileFragment()).commit();
                    }
                });
            }


        }
    }


    private boolean checkValidation(){

        String usernameTxt = username.getText().toString();
        String phoneTxt = phone.getText().toString();

        Pattern phonePattern = Pattern.compile(global.phoneReg);


        Matcher pm = phonePattern.matcher(phoneTxt);

        if(pm.find() && !usernameTxt.equals("") && !phone.equals("")){
            return true;
        }
        else if(usernameTxt.equals("")){
            Toast.makeText(getActivity(), "Username cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if (!pm.find()){
            Toast.makeText(getActivity(), "Invalid Phone Number.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Phone Number cannot be empty",Toast.LENGTH_SHORT).show();
        }

        return false;

    }
}
