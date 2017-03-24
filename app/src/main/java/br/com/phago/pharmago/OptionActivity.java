package br.com.phago.pharmago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OptionActivity extends AppCompatActivity {

    private static final String TAG = OptionActivity.class.getSimpleName();
    private List<Option> optionList = new ArrayList<Option>();
    private OptionArrayAdapter optionArrayAdapter;
    private ListView optionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String selectedCampaignId = intent.getStringExtra(QuestionActivity.EXTRA_CAMPAIGN_ID);
        final String selectedQuestionId = intent.getStringExtra(QuestionActivity.EXTRA_QUESTION_ID);
        final String selectedCampaignParticipationEnabled = intent.getStringExtra(QuestionActivity.ENABLE_PARTICIPATION_YN);


        // Capture the layout's TextView and set the string as its text
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(message);

        Log.i(TAG, "... onCreate");
        //dbx = new PgDatabaseHelper(getApplicationContext());
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        optionList = dbx.getOptionsByQuestionAndCampaignId(Integer.parseInt(selectedCampaignId), Integer.parseInt(selectedQuestionId));
        //dbx.closeDB();
        dbx.close();

//        Toast.makeText(this, " BD returned " + Integer.toString(optionList.size()) + " questions", Toast.LENGTH_SHORT).show();
        // TODO DELETE ME
        // for debugging purposes
        if (optionList.size() < 1) {
            Log.i(TAG, "a lista de opções está vazia");
            getFakeOptionsList();
            //finish();
        }

        // create ArrayAdapter to bind xxxList to the xxxxListView (
        optionListView = (ListView) findViewById(R.id.optionListView);
        optionArrayAdapter = new OptionArrayAdapter(this, optionList);
        optionListView.setAdapter(optionArrayAdapter);
        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                //Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();

                // load the Activity with detailed info about the User Selected Questions
                String selectedOptionId = " | id = | " + optionList.get(position).getSequential().toString() + " |";
                String selectedOptionLabel = "| Option Label = | " + optionList.get(position).getLabel() + "|";
                String toast_value;
                String initial_user_answer = optionList.get(position).getUserAnswer();
                String current_user_answer;

                int campaign_id;

                if (selectedCampaignParticipationEnabled.equals("Y")) {
                    if (selectedOptionId != null) {
                        toast_value = selectedOptionId + "\n" + selectedOptionLabel;

                        int i = position;
                        if (optionList.get(position).getUserAnswer().equals("U")) {
                            current_user_answer = "N";
//                            Toast.makeText(OptionActivity.this, "Answer changed from 'U' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                        } else if (optionList.get(position).getUserAnswer().equals("Y")) {
                            current_user_answer = "N";
//                            Toast.makeText(OptionActivity.this, "Answer changed from 'Y' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                        } else if (optionList.get(position).getUserAnswer().equals("N")) {
                            current_user_answer = "Y";
//                            Toast.makeText(OptionActivity.this, "Answer changed from 'N' to 'Y'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                        }
                    } else {
                        toast_value = "NULL";
                    }
//                    Toast.makeText(OptionActivity.this, toast_value, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void getFakeOptionsList() {
        // for debugging
        for (int i = 1; i < 10; i++) {
            Option mOpItem = new Option();
            mOpItem.setLabel("Fake Option " + Integer.toString(i));
            mOpItem.setIdQuestion(i);
            mOpItem.setIdSponsor(50 - i);
            mOpItem.setIdCampaign(100 - i);
            mOpItem.setRightAnswer("Y");
            mOpItem.setSequential(i);
            mOpItem.setUserAnswer("U");

            optionList.add(mOpItem);
            Log.i("DEBUG DEBUG AND DEBUG", mOpItem.getLabel());
        }
    }
}
