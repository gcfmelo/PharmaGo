package br.com.phago.pharmago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gustavo on 09/03/2017.
 */

class CampaignArrayAdapter extends ArrayAdapter<CampaignListClass>{

    // class for reusing views as list items ("CampaignsListClass") scroll off and onto screen
    private static class ViewHolder{
        TextView campaignTextView;
        TextView campaignIdTextView;
        TextView sponsorTextView;
        TextView startDateTextView;
        TextView statusTextView;
    }

    public CampaignArrayAdapter(Context context, List<CampaignListClass> camplist){
        super(context, -1, camplist);
        // the argument "-1" indicates that we use a custom layout in this App so we can display more than one TextView
    }

    // creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // get CampaignListClass object for this specified ListView position
        CampaignListClass cp = getItem(position);
        ViewHolder viewHolder;  // object that reference's list item's views
        // check for reusable ViewHolder from ListView item that scrolled
        // off screen; otherwise, create a new ViewHolder
        if(convertView == null) {// no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.campaign_list_item, parent, false);
            viewHolder.campaignTextView = (TextView) convertView.findViewById(R.id.textViewCampaignName);
            viewHolder.campaignIdTextView = (TextView) convertView.findViewById(R.id.textViewCampaignId);
            viewHolder.sponsorTextView = (TextView) convertView.findViewById(R.id.textViewSponsor);
            viewHolder.startDateTextView = (TextView) convertView.findViewById(R.id.textViewStartDate);
            viewHolder.statusTextView = (TextView) convertView.findViewById(R.id.textViewStatus);

            convertView.setTag(viewHolder);

        } else {  // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // see https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

        // get other data from CampaignListClass object and place into views
        Context context = getContext(); // for loading String resources
        viewHolder.campaignTextView.setText(context.getString(
                R.string.campaign_description, cp.getCampaignName()));
        viewHolder.campaignIdTextView.setText(context.getString(
                R.string.campaign_id, Integer.toString(cp.getCampaignId())));
        viewHolder.sponsorTextView.setText(context.getString(
                R.string.sponsor_description, cp.getSponsorName()));
        viewHolder.startDateTextView.setText(
                context.getString(R.string.start_date_description, cp.getStartDate()));
        viewHolder.statusTextView.setText(
                context.getString(R.string.status_description, cp.getCampaignStatus()));


        return convertView; // return completed list item to display
    }
}
