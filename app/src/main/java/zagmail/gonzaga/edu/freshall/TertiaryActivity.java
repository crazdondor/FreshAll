package zagmail.gonzaga.edu.freshall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class TertiaryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tertiary);

        BottomNavigationView bottomNavigationView =  findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent0 = new Intent(TertiaryActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.nav_add:
                        Intent intent1 = new Intent(TertiaryActivity.this, SecondaryActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_message:
                        break;

                    case R.id.nav_user:
                        Intent intent3 = new Intent(TertiaryActivity.this, FourthActivity.class);
                        startActivity(intent3);
                        break;

                }

                return false;
            }
        });
    }

}
