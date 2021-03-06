package csedu.homeclick.androidhomeclick.activities.sign_in_fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserAuthInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;


public class SignInFragment extends Fragment implements View.OnClickListener {
    private Button sendLink;
    private TextInputEditText editEmail;
    private UserService userService;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        bindWidgets(view);
        sendLink.setOnClickListener(this);

        return view;
    }

    private void bindWidgets(View view) {
        editEmail = view.findViewById(R.id.editEmail);
        sendLink = view.findViewById(R.id.sendSignInLink);
        userService = new UserService();
    }


    @Override
    public void onClick(View v) {
        sendLink.setEnabled(false);
        final View view = v;
        if(checkData()) {
            String email = Objects.requireNonNull(editEmail.getText()).toString().trim();
            userService.signIn(email, view.getContext().getApplicationContext(), new UserAuthInterface.SendLinkToUserListener<String>() {
                @Override
                public void OnSendLinkSuccessful(String toastMessage) {
                    sendLink.setEnabled(true);
                    Toast.makeText(view.getContext().getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnSendLinkFailed(String error) {
                    sendLink.setEnabled(true);
                    Toast.makeText(view.getContext().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private Boolean checkData() {
        Boolean allOkay = true;
        String  email = Objects.requireNonNull(editEmail.getText()).toString().trim();


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter valid email");
            allOkay = false;
        }
        return allOkay;

    }
}