package br.com.phago.pharmago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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


        // Capture the layout's TextView and set the string as its text
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(message);

        Log.i(TAG, "... onCreate");
        //dbx = new PgDatabaseHelper(getApplicationContext());
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        questionList = dbx.getQuestionsByCampaignId(Integer.parseInt(selectedCampaignId));
        //dbx.closeDB();
        dbx.close();

        Toast.makeText(this, " BD returned " + Integer.toString(questionList.size()) + " questions", Toast.LENGTH_SHORT).show();
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
}
