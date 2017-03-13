package br.com.phago.pharmago;

/**
 * Created by Gustavo on 12/03/2017.
 */

public class CampaignDetailListClass {
    private String campaignTitle, sponsorName, startDate, campaignStatus, questionLabel, optionLabel,optionIsRight,optionUserAnswer;
    private int optionSeqNumber, pointsRightAnswer, pointsParticipation;

    public CampaignDetailListClass() {
            }

    public CampaignDetailListClass(String campaignTitle, String sponsorName, String startDate,
                                   String campaignStatus, String questionLabel, String optionLabel,
                                   String optionIsRight, String optionUserAnswer, int optionSeqNumber, int pointsRightAnswer, int pointsParticipation) {
        this.campaignTitle = campaignTitle;
        this.sponsorName = sponsorName;
        this.startDate = startDate;
        this.campaignStatus = campaignStatus;
        this.questionLabel = questionLabel;
        this.optionLabel = optionLabel;
        this.optionIsRight = optionIsRight;
        this.optionUserAnswer = optionUserAnswer;
        this.optionSeqNumber = optionSeqNumber;
        this.pointsRightAnswer = pointsRightAnswer;
        this.pointsParticipation = pointsParticipation;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
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

    public String getQuestionLabel() {
        return questionLabel;
    }

    public void setQuestionLabel(String questionLabel) {
        this.questionLabel = questionLabel;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionIsRight() {
        return optionIsRight;
    }

    public void setOptionIsRight(String optionIsRight) {
        this.optionIsRight = optionIsRight;
    }

    public String getOptionUserAnswer() {
        return optionUserAnswer;
    }

    public void setOptionUserAnswer(String optionUserAnswer) {
        this.optionUserAnswer = optionUserAnswer;
    }

    public int getOptionSeqNumber() {
        return optionSeqNumber;
    }

    public void setOptionSeqNumber(int optionSeqNumber) {
        this.optionSeqNumber = optionSeqNumber;
    }

    public int getPointsRightAnswer() {
        return pointsRightAnswer;
    }

    public void setPointsRightAnswer(int pointsRightAnswer) {
        this.pointsRightAnswer = pointsRightAnswer;
    }

    public int getPointsParticipation() {
        return pointsParticipation;
    }

    public void setPointsParticipation(int pointsParticipation) {
        this.pointsParticipation = pointsParticipation;
    }
}
