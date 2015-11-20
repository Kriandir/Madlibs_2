package kriek.madlibs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class Inputstory extends AppCompatActivity {
    public InputStream stream;
    public Story parsing;
    public TextView placeholder;
    public TextView placeholdercount;
    public TextView textshow;
    public TextToSpeech translator;
    public EditText inputtext;
    public boolean  textspeaker = false;
    public int checker;
    public boolean errorcheck = false;
    public int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputstory);

        translator = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {



                translator.speak("Please enter a " + parsing.getNextPlaceholder().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
                textspeaker = true;
            }

        });
              /* define all variables used and fetch story
              */
        textshow = (TextView) findViewById(R.id.storynum);
        placeholder = (TextView) findViewById(R.id.placeholders);
        placeholdercount = (TextView) findViewById(R.id.wordscount);
        inputtext = (EditText) findViewById(R.id.editText);
        Bundle extras = getIntent().getExtras();
        checker = extras.getInt("checker");
        getStory(checker);



          /* Initialize text to speech object. */


    }

    /* Method for starting text to speech functionality. */
    public void talkToText(View view) {
        int requestcode = 1;
        /* convert said words to text. */
        Intent convertTalk = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        convertTalk.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        convertTalk.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        convertTalk.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say a" + placeholder);
        try {
            startActivityForResult(convertTalk, requestcode);
        } catch (ActivityNotFoundException anfe) {
            textshow.setText(getString(R.string.talker));
        }

    }

    /* Method for receiving and processing spoken data. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent input) {

        super.onActivityResult(requestCode, resultCode, input);

        /* Receive and process spoken text. */
        ArrayList<String> list = input.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        String spokenText = list.get(0);
        processInput(spokenText);
    }

    public void inputWord (View z){
        // Get word from textfield
        String input = String.valueOf(inputtext.getText());
        inputtext.setText("");

        /* Check for correct input. */
        if (!input.matches("[a-zA-Z\\s]+")&& errorcheck == false) {
            textshow.setText(getString(R.string.errorinput));
            errorcheck = true;
        }
        else {
            processInput(input);
            // pass input to story and reset errorcheck
//            parsing.fillInPlaceholder(input);
//            if (errorcheck == true){
//                textshow.setText(getString(R.string.inputword));
//                errorcheck = false;
//            }
        }

//        // get words left
//        counter = parsing.getPlaceholderRemainingCount();
//
//        // go to storyteller screen when done
//        if (counter == 0) {
//
//            Intent storyteller = new Intent(this, Storyteller.class);
//            storyteller.putExtra("parsing", parsing); // Send parser as well (contains story)
//            startActivity(storyteller);
//        }
//
//        // update words
//        resetText();
//        placeholder.append(" " + parsing.getNextPlaceholder().toLowerCase());
//        placeholdercount.append(" " + counter);
    }
    /* Method for handling the spoken/written input of the user. */
    public void processInput(String input) {

        /* Send input to parser. */
        parsing.fillInPlaceholder(input);
        if (errorcheck == true){
            textshow.setText(getString(R.string.inputword));
            errorcheck = false;
        }

        /* Proceed to next screen when all placeholders are filled. */
        counter = parsing.getPlaceholderRemainingCount();

        // go to storyteller screen when done
        if (counter == 0) {

            Intent storyteller = new Intent(this, Storyteller.class);
            storyteller.putExtra("parsing", parsing); // Send parser as well (contains story)
            startActivity(storyteller);
        }
        /* If tts is ready, prompt the required word type through spoken text. */
        else if (textspeaker) {
            translator.speak("Please enter a " + parsing.getNextPlaceholder().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
        }

        /* Update category and words left- values. */
        resetText();
        placeholder.append(" " + parsing.getNextPlaceholder().toLowerCase());
        placeholdercount.append(" " + counter);
    }

    /* load story
     */
    public void getStory(int checker){
        switch(checker){
            case 0:
                stream = this.getResources().openRawResource(R.raw.madlib0_simple);
                break;
            case 1:
                stream = this.getResources().openRawResource(R.raw.madlib1_tarzan);
                break;
            case 2:
                stream = this.getResources().openRawResource(R.raw.madlib2_university);
                break;
            case 3:
                stream = this.getResources().openRawResource(R.raw.madlib3_clothes);
                break;
            case 4:
                stream = this.getResources().openRawResource(R.raw.madlib4_dance);
        }

        parseStory(stream);

    }
    /* Method to save the current state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        /* send checker id and story element
         */
        outState.putInt("checker",checker);
        outState.putSerializable("parsing", parsing);
    }

    /* Method in order to retrieve data after pause
       also enables rotation
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {

        super.onRestoreInstanceState(inState);

        /* retrieve checker id and get story element back
        */
        checker = inState.getInt("checker");
        parsing = (Story) inState.getSerializable("parsing");

        /* reset text and fetch placeholder
        */
        resetText();
        placeholder.append(" " + parsing.getNextPlaceholder().toLowerCase());
        placeholdercount.append(" " + parsing.getPlaceholderRemainingCount());
    }
    /* parse selected story
     */
    public void parseStory(InputStream stream){

        /* Create new story object with selected stream and show placeholder information. */
            parsing = new Story(stream);
            placeholder.append(" " + parsing.getNextPlaceholder());
            placeholdercount.append(" " + parsing.getPlaceholderCount());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inputstory, menu);
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
    /* reset the text
     */
    public void resetText(){
        
        placeholder.setText(getString(R.string.placeholders));
        placeholdercount.setText(getString(R.string.placeholderscount));
    }
}

