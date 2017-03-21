package br.com.phago.pharmago;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Gustavo on 09/03/2017.

 Option Class:
 ===========================
 private Integer idSponsor;
 private Integer idCampaign;
 private Integer idQuestion;
 private Integer sequential;
 ---------------------------
 private String label;
 private String rightAnswer;
 private String userAnswer;
 ===========================
 */

class OptionArrayAdapter extends ArrayAdapter<Option>{

    // class for reusing views as list items ("Option") scroll off and onto screen
    private static class ViewHolder{
        TextView optionId;     // sequential
        TextView optionLabel;
        TextView optionRightAnswer;   // Y/N
        TextView optionUserAnswer;      // Y/N/U   U=Undefined
        CheckBox checkBoxUserSelection;
    }

    public OptionArrayAdapter(Context context, List<Option> oplist){
        super(context, -1, oplist);
        // the argument "-1" indicates that we use a custom layout in this App so we can display moe than one TextView
    }

    // creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // get CampaignListClass object for this specified ListView position
        Option op = getItem(position);
        Log.d(TAG,op.toString());
        ViewHolder viewHolder;  // object that reference's list item's views
        // check for reusable ViewHolder from ListView item that scrolled
        // off screen; otherwise, create a new ViewHolder
        if(convertView == null) {// no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.option_list_item, parent, false);
            viewHolder.optionId = (TextView) convertView.findViewById(R.id.textViewOptionId);
            viewHolder.optionLabel = (TextView) convertView.findViewById(R.id.textViewOptionLabel);
            viewHolder.optionRightAnswer = (TextView) convertView.findViewById(R.id.textViewRightAnswer);
            viewHolder.optionUserAnswer = (TextView) convertView.findViewById(R.id.textViewUserAnswer);
            viewHolder.checkBoxUserSelection = (CheckBox) convertView.findViewById(R.id.checkBoxUserSelection);

            convertView.setTag(viewHolder);

        } else {  // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // see https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

//         get data from Question object and place into views
        Context context = getContext(); // for loading String resources
        viewHolder.optionId.setText(context.getString(R.string.option_id, Integer.toString(op.getSequential())));
        viewHolder.optionLabel.setText(context.getString(R.string.option_label, (op.getLabel())));
        viewHolder.optionRightAnswer.setText(context.getString(R.string.option_right_answer, op.getRightAnswer()));
        viewHolder.optionUserAnswer.setText(context.getString(R.string.option_user_answer, op.getUserAnswer()));
        if (op.getUserAnswer().equals("U")) {
            viewHolder.checkBoxUserSelection.setText(R.string.user_answer_undefined);
            viewHolder.checkBoxUserSelection.setEnabled(true);
        } else if (op.getUserAnswer().equals("N")){
            viewHolder.checkBoxUserSelection.setText(R.string.user_answer_false);
            viewHolder.checkBoxUserSelection.setChecked(false);
            viewHolder.checkBoxUserSelection.setEnabled(true);
        } else if (op.getUserAnswer().equals("Y")){
            viewHolder.checkBoxUserSelection.setText(R.string.user_answer_true);
            viewHolder.checkBoxUserSelection.setChecked(true);
            viewHolder.checkBoxUserSelection.setEnabled(true);
        } else {
            viewHolder.checkBoxUserSelection.setText("WTF!");
        }
        ;

        return convertView; // return completed list item to display
    }
}
