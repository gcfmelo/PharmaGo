package br.com.phago.pharmago;

import android.content.Intent;
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
import android.widget.Toast;

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
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(getApplicationContext());
        optionList = dbx.getOptionsByQuestionAndCampaignId(Integer.parseInt(selectedCampaignId), Integer.parseInt(selectedQuestionId));
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

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

                //Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();

                // load the Activity with detailed info about the User Selected Questions
                String selectedOptionId = " | id = | " + optionList.get(position).getSequential().toString() + " |";
                String selectedOptionLabel = "| Option Label = | " + optionList.get(position).getLabel() + "|";
                Toast.makeText(OptionActivity.this, selectedOptionId+selectedOptionLabel, Toast.LENGTH_SHORT).show();
                String toast_value;
                String initial_user_answer = optionList.get(position).getUserAnswer();
                String current_user_answer;
                Option selectedOption = optionList.get(position);

                int campaign_id;

                if (selectedCampaignParticipationEnabled.equals("Y")) {
                    if (selectedOptionId != null) {
                        toast_value = selectedOptionId + "\n" + selectedOptionLabel;


                        int i = position;
                        if (optionList.get(position).getUserAnswer().equals("U")) {
                            selectedOption.setUserAnswer("N");
                            PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(getApplicationContext());
                            dbx.updateOption(selectedOption);
                            dbx.close();

//                            Toast.makeText(OptionActivity.this, "Answer changed from 'U' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                        } else if (optionList.get(position).getUserAnswer().equals("Y")) {
                            selectedOption.setUserAnswer("N");
                            PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(getApplicationContext());
                            dbx.updateOption(selectedOption);
                            dbx.close();

//                            Toast.makeText(OptionActivity.this, "Answer changed from 'Y' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                        } else if (optionList.get(position).getUserAnswer().equals("N")) {
                            selectedOption.setUserAnswer("Y");
                            PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(getApplicationContext());
                            dbx.updateOption(selectedOption);
                            dbx.close();
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


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
