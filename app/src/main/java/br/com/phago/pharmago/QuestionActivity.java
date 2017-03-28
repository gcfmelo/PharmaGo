package br.com.phago.pharmago;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();
    public static final String EXTRA_CAMPAIGN_ID = "br.com.phago.pharmago.EXTRA_CAMPAIGN_ID";
    public static final String EXTRA_QUESTION_ID = "br.com.phago.pharmago.EXTRA_QUESTION_ID";
    public static final String ENABLE_PARTICIPATION_YN = "br.com.phago.pharmago.ENABLE_PARTICIPATION_YN";

    private List<Question> questionList = new ArrayList<Question>();
    private QuestionArrayAdapter questionArrayAdapter;
    private ListView questionListView; // displays Campaign info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String selectedCampaignId = intent.getStringExtra(MainActivity.EXTRA_CAMPAIGN_ID);
        final String campaign_id = selectedCampaignId;
        String selectedCampaignParticipationEnabled = intent.getStringExtra(MainActivity.ENABLE_PARTICIPATION_YN);
        final String enable_participation = selectedCampaignParticipationEnabled;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Capture the layout's TextView and set the string as its text
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(message);

        Log.i(TAG, "... onCreate");
        //dbx = new PgDatabaseHelper(getApplicationContext());
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        questionList = dbx.getQuestionsByCampaignId(Integer.parseInt(selectedCampaignId));
        //dbx.closeDB();
        dbx.close();

//        Toast.makeText(this, " BD returned " + Integer.toString(questionList.size()) + " questions", Toast.LENGTH_SHORT).show();
        // TODO DELETE ME
        // for debugging purposes
        if (questionList.size() < 1) {
            Log.i(TAG, "a lista de questões está vazia");
            // getFakeCampaignsList();
            //finish();
        }
        // create ArrayAdapter to bind xxxList to the xxxxListView (
        questionListView = (ListView) findViewById(R.id.questionListView);
        questionArrayAdapter = new QuestionArrayAdapter(this, questionList);
        questionListView.setAdapter(questionArrayAdapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                // change background color when question is clicked
                view.setBackgroundColor(Color.argb(32,8,16,128));
                //Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();

                // load the Activity with detailed info about the User Selected Questions
                String selectedQuestionId =  Integer.toString(questionList.get(position).getIdQuestion()) ;
                String selectedQuestionLabel = "| Question Label = | " + questionList.get(position).getLabel()+"|";
                String toast_value;
                String status;

                if (selectedQuestionId != null) {

                    Intent intent = new Intent(QuestionActivity.super.getApplicationContext(), OptionActivity.class);
                    intent.putExtra(EXTRA_CAMPAIGN_ID, campaign_id);
                    intent.putExtra(EXTRA_QUESTION_ID, selectedQuestionId);
                    intent.putExtra(ENABLE_PARTICIPATION_YN, enable_participation);

                    startActivity(intent);
                }
                else {
                    toast_value = "NULL";
                }
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//    <string name="action_menu_0">Refresh Campaigns</string>
//    <string name="action_menu_1">Login</string>
//    <string name="action_menu_2">Exit</string>

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_op0) {
//            Toast.makeText(this, "You selected Option 0 id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();

            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_menu_op1) {
//            Toast.makeText(this, "You selected Option 1  id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
            // TODO call Login

            //        Limpar PreferedManager
            // limpa tudo
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().commit();
            // não limpa o contador de acessos
//            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", "").commit();
//            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", "").commit();
//            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_USERNAME", "").commit();

//            Intent intentCheckIn = new Intent(CampaignActivity.super.getApplicationContext(), CheckInActivity.class);
//            intentCheckIn.putExtra(EXTRA_EMAIL, "");
//            intentCheckIn.putExtra(EXTRA_PASSWORD, "");
//            startActivity(intentCheckIn);
            finish();

            return true;
        }
        if (id == R.id.action_menu_op2) {
//            Toast.makeText(this, "You selected Option 2: RELOAD App", Toast.LENGTH_SHORT).show();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
