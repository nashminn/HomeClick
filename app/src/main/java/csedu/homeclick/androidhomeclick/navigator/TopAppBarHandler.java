package csedu.homeclick.androidhomeclick.navigator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;


import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.activities.AdFeed;
import csedu.homeclick.androidhomeclick.activities.Data_Policy;
import csedu.homeclick.androidhomeclick.activities.Filter;
import csedu.homeclick.androidhomeclick.activities.Terms_Of_Use;
import csedu.homeclick.androidhomeclick.activities.UserSignIn;
import csedu.homeclick.androidhomeclick.connector.UserService;
import csedu.homeclick.androidhomeclick.database.UserAuth;

public class TopAppBarHandler extends Activity implements MenuItem.OnMenuItemClickListener{
    private Toolbar toolbar;
    private Activity activity;
    private final UserService userService;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(UserAuth.isSignedIn()) {
            menu.getItem(2).setTitle("Sign out");
        } else {
            menu.getItem(2).setTitle("Sign in");
        }

        return super.onPrepareOptionsMenu(menu);
    }


    private TopAppBarHandler(Toolbar toolbar, Activity activity) {
        this.activity = activity;
        this.toolbar = toolbar;
        userService = new UserService();
        toolbar.inflateMenu(R.menu.top_app_bar);
        activity.invalidateOptionsMenu();
        onPrepareOptionsMenu(toolbar.getMenu());
    }

    public static TopAppBarHandler getInstance(Toolbar toolbar, Activity activity) {
        return new TopAppBarHandler(toolbar, activity);
    }



    public void handle() {
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        UserAuth.setInstance();
        switch (item.getItemId()) {
            case R.id.sign_in_sign_out:
                if(userService.isSignedIn()) {
                    Log.i("signin", "user is signed in");
                    userService.signOut(activity.getApplicationContext());
                    toolbar.getMenu().getItem(2).setTitle("Sign in");
                    Intent targetIntent = new Intent(activity.getApplicationContext(), AdFeed.class);
                    activity.startActivity(targetIntent);
                } else {
                    Intent targetIntent = new Intent(activity.getApplicationContext(), UserSignIn.class);

                    activity.startActivity(targetIntent);
                }
                break;
            case R.id.about_app:

                break;

            case R.id.subitem1:
                Intent targetIntent = new Intent(activity.getApplicationContext(), Data_Policy.class);
                activity.startActivity(targetIntent);
                break;

            case R.id.subitem2:
                Intent targetIntent3 = new Intent(activity.getApplicationContext(), Terms_Of_Use.class);
                activity.startActivity(targetIntent3);
                break;

            case R.id.filter:

                Intent targetIntent1 = new Intent(activity.getApplicationContext(), Filter.class);
                activity.startActivity(targetIntent1);

                break;
        }
        return true;
    }

}
