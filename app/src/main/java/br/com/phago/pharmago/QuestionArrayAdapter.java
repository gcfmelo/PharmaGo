package br.com.phago.pharmago;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Gustavo on 09/03/2017.
 */

class QuestionArrayAdapter extends ArrayAdapter<Question>{

    // class for reusing views as list items ("QuestionListClass") scroll off and onto screen
    private static class ViewHolder{
        TextView questionId;
        TextView questionLabel;
//        TextView campaignId;
//        TextView sponsorId;

    }

    public QuestionArrayAdapter(Context context, List<Question> qlist){
        super(context, -1, qlist);
        // the argument "-1" indicates that we use a custom layout in this App so we can display moe than one TextView
    }

    // creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // get CampaignListClass object for this specified ListView position
        Question cp = getItem(position);
        Log.d(TAG,cp.toString());
        ViewHolder viewHolder;  // object that reference's list item's views
        // check for reusable ViewHolder from ListView item that scrolled
        // off screen; otherwise, create a new ViewHolder
        if(convertView == null) {// no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.question_list_item, parent, false);
            viewHolder.questionId = (TextView) convertView.findViewById(R.id.textViewQuestionId);
            viewHolder.questionLabel = (TextView) convertView.findViewById(R.id.textViewQuestionLabel);
//            viewHolder.campaignId = (TextView) convertView.findViewById(R.id.textViewCampaignId);
//            viewHolder.sponsorId = (TextView) convertView.findViewById(R.id.textViewSponsor);

            convertView.setTag(viewHolder);

        } else {  // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // see https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

//         get data from Question object and place into views
        Context context = getContext(); // for loading String resources
        viewHolder.questionId.setText(context.getString(R.string.question_id, Integer.toString(cp.getIdQuestion())));
        viewHolder.questionLabel.setText(context.getString(R.string.campaign_id, (cp.getLabel())));
//        viewHolder.campaignId.setText(context.getString(R.string.campaign_id, Integer.toString(cp.getIdCampaign())));
//        viewHolder.sponsorId.setText(context.getString(R.string.sponsor_id, Integer.toString(cp.getIdSponsor())));



        return convertView; // return completed list item to display
    }
}
