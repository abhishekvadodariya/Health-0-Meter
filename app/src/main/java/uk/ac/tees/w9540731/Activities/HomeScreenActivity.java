package uk.ac.tees.w9540731.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import uk.ac.tees.w9540731.Fragments.HeartRateFragment;
import uk.ac.tees.w9540731.Fragments.ProfileFragment;
import uk.ac.tees.w9540731.Fragments.RecordsFragment;
import uk.ac.tees.w9540731.Fragments.StepsFragment;
import uk.ac.tees.w9540731.HelperClasses.Constants;
import uk.ac.tees.w9540731.R;
import uk.ac.tees.w9540731.Services.StepService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class HomeScreenActivity extends AppCompatActivity {

    private BottomNavigationView navView ;
    private FragmentTransaction fragmentTransaction ;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){

                case R.id.navigation_records :
                    showFragment(new RecordsFragment());
                    return true;

                case R.id.navigation_heart :
                    showFragment(new HeartRateFragment());
                    return true;

               /* case R.id.profile :
                    showFragment(new ProfileFragment());
                    return true;*/

                default:
                    showFragment(new StepsFragment());
                    return true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

       navView = findViewById(R.id.nav_view);

       navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
       navView.setSelectedItemId(R.id.navigation_steps);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("834735646582-rdpb8ehk0u2qehnrn6hbnq6b6be07mn5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent startIntent = new Intent(this, StepService.class);
        startIntent.setAction(Constants.START_FOREGROUND);
        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, StepService.class);
        stopIntent.setAction(Constants.STOP_FOREGROUND);
        startService(stopIntent);
    }

    private void showFragment(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,frag).commit();
        manager.executePendingTransactions();
    }
}
