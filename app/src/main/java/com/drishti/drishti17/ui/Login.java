package com.drishti.drishti17.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.Student;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.AuthUtil;
import com.drishti.drishti17.util.Global;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.drishti.drishti17.ui.factory.ProgressDialog;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    @BindView(R.id.fullscreen_content)
    View mContentView;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.fullscreen_content_controls)
    View mControlsView;
    @BindView(R.id.g_button)
    Button gPlus;
    @BindView(R.id.fb_button)Button fbButton;
    boolean flag = true;
    boolean autoLogin=true;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "GoogleActivity";
    CallbackManager callbackManager;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    PropertyValuesHolder p6 = PropertyValuesHolder.ofFloat("scaleX", 1f);
                    PropertyValuesHolder p7 = PropertyValuesHolder.ofFloat("scaleY", 1f);
                    PropertyValuesHolder p8 = PropertyValuesHolder.ofFloat("y", logo.getY() / 2);
                    ObjectAnimator anim3 = ObjectAnimator.ofPropertyValuesHolder(logo, p6, p7, p8);
                    anim3.setDuration(1000);
                    anim3.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            show();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    anim3.start();
                }
            }
        });
        hide();
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setAlpha(0f);
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("alpha", 1f);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("scaleX", 1.5f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("scaleY", 1.5f);
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(logo, p1, p2, p3);
        anim.setDuration(2000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PropertyValuesHolder p4 = PropertyValuesHolder.ofFloat("scaleX", 1.45f);
                PropertyValuesHolder p5 = PropertyValuesHolder.ofFloat("scaleY", 1.45f);
                ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(logo, p4, p5);
                anim1.setDuration(100);
                anim1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.g_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    final ApiInterface service = ApiClient.getService();
                    AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
                        @Override
                        public void tokenObtained(String token) {
                            Call<Student> call=service.login(token);
                            call.enqueue(new Callback<Student>() {
                                @Override
                                public void onResponse(Call<Student> call, Response<Student> response) {
                                    Student student=response.body();
                                    if(student.registered){
                                        Global.isguest=false;
                                        startActivity(new Intent(Login.this,Home.class));
                                        finish();
                                    }else{
                                        if(autoLogin){
                                            FirebaseAuth.getInstance().signOut();
                                            autoLogin=false;
                                        }else {
                                            startActivity(new Intent(Login.this, MainRegister.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Student> call, Throwable t) {
                                    Log.d("Login","Fail");
                                }
                            });
                        }
                    });

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        gPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLogin=false;
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        callbackManager=CallbackManager.Factory.create();
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLogin=false;
                LoginManager.getInstance().logInWithReadPermissions(Login.this,Arrays.asList("email","public_profile"));
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login",loginResult+"");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error","error");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(TAG, "sucess");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d(TAG, result.getStatus().toString());
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.showProgressDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.disMissProgressDialog();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.showProgressDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.disMissProgressDialog();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
