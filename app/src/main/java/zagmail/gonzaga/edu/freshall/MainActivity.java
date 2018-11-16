package zagmail.gonzaga.edu.freshall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;

                    case R.id.nav_add:
                        Intent intent1 = new Intent(MainActivity.this, SecondaryActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_message:
                        Intent intent2 = new Intent(MainActivity.this, TertiaryActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_user:
                        Intent intent3 = new Intent(MainActivity.this, FourthActivity.class);
                        startActivity(intent3);
                        break;

                }

                return false;
            }
        });
    }

}
