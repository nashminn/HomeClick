package csedu.homeclick.androidhomeclick.activities.sign_in_fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.connector.UserAuthInterface;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.structure.User;


public class SignUpFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SignUpFragment";
    private TextInputEditText name, email, phoneNumber;
    private CheckBox age, agreement;
    private Button sendLink;

    private UserService userService;



    public SignUpFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        bindWidgets(view);

        sendLink.setOnClickListener(this);

        return view;
    }

    private void bindWidgets(View view) {
        email = view.findViewById(R.id.sign_up_email);
        name = view.findViewById(R.id.sign_up_name);
        phoneNumber = view.findViewById(R.id.sign_up_phone_number);

        age = view.findViewById(R.id.age_restriction);
        agreement = view.findViewById(R.id.policies);

        sendLink = view.findViewById(R.id.sendNewSignUpLink);

        userService = new UserService();
    }

    @Override
    public void onClick(View v) {
        sendLink.setEnabled(false);
        final View view = v;
        if(checkData()) {
            User user = getUser();
            userService.signInNewUser(user, view.getContext().getApplicationContext(), new UserAuthInterface.SendLinkToUserListener<User>() {
                @Override
                public void OnSendLinkSuccessful(String toastMessage) {
                    sendLink.setEnabled(true);
                    Toast.makeText(view.getContext().getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnSendLinkFailed(String error) {
                    sendLink.setEnabled(true);
                    Log.i(TAG, error);
                    Toast.makeText(view.getContext().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private User getUser() {
        String emailStr = Objects.requireNonNull(email.getText()).toString().trim();
        String nameStr = Objects.requireNonNull(name.getText()).toString().trim();
        String phoneNum = Objects.requireNonNull(phoneNumber.getText()).toString().trim();
        User newUser = new User(nameStr, emailStr, phoneNum);
        return newUser;
    }

    private Boolean checkData() {
        Boolean allOkay = true;
        String emailStr = Objects.requireNonNull(email.getText()).toString().trim();
        String nameStr = Objects.requireNonNull(name.getText()).toString().trim();
        String phoneNum = Objects.requireNonNull(phoneNumber.getText()).toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("Enter valid email");
            allOkay = false;
        }

        if(!Patterns.PHONE.matcher(phoneNum).matches()) {
            phoneNumber.setError("Enter valid phone number");
            allOkay = false;
        }

        if(nameStr.isEmpty()) {
            name.setError("Name cannot be empty");
            allOkay = false;
        }

        if(!age.isChecked()) {
            allOkay = false;
        }

        if(!agreement.isChecked()) {
            allOkay = false;
        }

        return allOkay;
    }
}