package kriek.madlibs;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


public class Choosestory extends AppCompatActivity {

    public int checker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosestory);
         /* Initialize different story options. */
        String[] storylist = {getString(R.string.simple), getString(R.string.tarzan), getString(R.string.university), getString(R.string.clothes), getString(R.string.dance)};

        /* Initialize adapter and link to ListView. */
        final ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storylist);
        final ListView storyView = (ListView) findViewById(R.id.story_list);
        storyView.setAdapter(theAdapter);

        /* Create override method for processing ListView item clicks. */
        storyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checker, long id) {




                /* Create intent to direct to next screen. */
                Intent inputstory = new Intent(Choosestory.this, Inputstory.class);
                inputstory.putExtra("checker", checker); // Send parser as well (contains story)
                startActivity(inputstory);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choosestory, menu);
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
