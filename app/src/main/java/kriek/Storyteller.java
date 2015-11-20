package kriek.madlibs;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Storyteller extends AppCompatActivity {
    public  TextView storytell;
    public  Story parsing;
    public String curstory;
    public boolean  textspeaker = false;
    public TextToSpeech translator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyteller);

        translator = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {



                translator.speak("" +curstory, TextToSpeech.QUEUE_FLUSH, null);
                textspeaker = true;
            }

        });

        //getting the story and putting it unto the screen
        storytell = (TextView) findViewById(R.id.storyviewer);
        Intent calledActivity = getIntent();
        parsing = (Story) calledActivity.getSerializableExtra("parsing");
        curstory = parsing.toString();
        storytell.setText(curstory);
    }
    public void onButtonclick(View view){
        Intent reset =new Intent(this,MainActivity.class);
        startActivity(reset);
    }

    /* Method to save the current state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        /* Pass story.
        */
        outState.putString("story", curstory);

    }

    /* Method for restoring data
       also enables turning of screen.
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {

        super.onRestoreInstanceState(inState);

        /* Retrieve story
        */
        curstory = inState.getString("story");
        storytell.setText(curstory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_storyteller, menu);
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
