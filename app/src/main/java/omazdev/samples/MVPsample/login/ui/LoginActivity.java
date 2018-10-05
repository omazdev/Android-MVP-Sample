package omazdev.samples.MVPsample.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import omazdev.samples.MVPsample.MainActivity.MainActivity;
import omazdev.samples.MVPsample.R;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    LoginButton btnLogin;

    private CallbackManager callbackManager;

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            navigateToSearchScreen();
        }

        callbackManager = CallbackManager.Factory.create();
        btnLogin.setReadPermissions(Arrays.asList(EMAIL));
//        btnLogin.setPublishPermissions("publish_actions");
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                navigateToSearchScreen();
            }

            @Override
            public void onCancel() {
               Toast.makeText(getApplicationContext(), R.string.login_cancel_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                String msgError = error.getLocalizedMessage();
                Toast.makeText(getApplicationContext(), msgError, Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        AppEventsLogger.activateApp(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        AppEventsLogger.deactivateApp(this);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void navigateToSearchScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
