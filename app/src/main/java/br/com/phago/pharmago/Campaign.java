package br.com.phago.pharmago;

/**
 * Created by Gustavo on 26/02/2017.
 */

public class Campaign {
    private int idSponsor;  // unique id of the Sponsor originally: idLaboratory
    private int idCampaign;    // unique id of this Campaign
    private int sequential;    // sequential number of this Campaign returned by Web Services
    private String nameOfSponsory;  // name of the sponsor (orig: nameOfLaboratory)
    private String eventDateOfCampaign;  // format YYYY-MM-DD like in "2017-01-17"
    private int questionsOfCampaign   ;   // number of questions in this campaign
    private int pointsForRightAnswer;     // sponsor defined: maximum number of points attainable from 100% correct answers
    private int pointsForParticipation;  // sponsor defined: points granted for ay user submitting a complete answer for this campaign
    private String status;  // Possible values are: "ANSWERED", TODO define possible statuses
    private String createdAt; // Timestamp for the creation of this Campaign in String format like "2017-01-17 13:53:38.0"


    // Bulk constructor for Campaign
    public Campaign(int idSponsor, int idCampaign, int sequential,
                    String nameOfSponsory, String eventDateOfCampaign,
                    int questionsOfCampaign, int pointsForRightAnswer,
                    int pointsForParticipation, String status, String createdAt) {
        this.idSponsor = idSponsor;
        this.idCampaign = idCampaign;
        this.sequential = sequential;
        this.nameOfSponsory = nameOfSponsory;
        this.eventDateOfCampaign = eventDateOfCampaign;
        this.questionsOfCampaign = questionsOfCampaign;
        this.pointsForRightAnswer = pointsForRightAnswer;
        this.pointsForParticipation = pointsForParticipation;
        this.status = status;
        this.createdAt = createdAt;
    }


}
