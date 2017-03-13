package br.com.phago.pharmago;

/**
 * Created by Gustavo on 12/03/2017.
 * This class will hold items for the selection list the user interface
 */

public class CampaignListClass {
    private String campaignName, sponsorName, startDate, campaignStatus;

    public CampaignListClass() {
    }

    public CampaignListClass(String campaignName, String sponsorName, String startDate, String campaignStatus) {
        this.campaignName = campaignName;
        this.sponsorName = sponsorName;
        this.startDate = startDate;
        this.campaignStatus = campaignStatus;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    @Override
    public String toString() {
        return "CampaignListClass{" +
                "campaignName='" + campaignName + '\'' +
                ", sponsorName='" + sponsorName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", CampaignStatus='" + campaignStatus + '\'' +
                '}';
    }
}
