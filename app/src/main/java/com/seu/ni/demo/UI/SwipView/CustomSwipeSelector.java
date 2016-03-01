package com.seu.ni.demo.UI.SwipView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.roughike.swipeselector.OnSwipeItemSelectedListener;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.seu.ni.demo.R;

public class CustomSwipeSelector extends AppCompatActivity {
    SwipeSelector mSwipeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_swipe_selector);
        mSwipeSelector = (SwipeSelector) findViewById(R.id.id_swipe_selector);
        mSwipeSelector.setItems(
                new SwipeItem(0, "title1", "text 1"),
                new SwipeItem(1, "title2", "text 2"),
                new SwipeItem(2, "title3", "text 3")
        );
        mSwipeSelector.setOnItemSelectedListener(new OnSwipeItemSelectedListener() {
            @Override
            public void onItemSelected(SwipeItem swipeItem) {
                int id = (int) swipeItem.value;
                switch (id) {
                    case 0:
                        Toast.makeText(CustomSwipeSelector.this, "1", Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        Toast.makeText(CustomSwipeSelector.this, "2", Toast.LENGTH_SHORT);
                        break;
                    case 2:
                        Toast.makeText(CustomSwipeSelector.this, "3", Toast.LENGTH_SHORT);
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_swipe_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
