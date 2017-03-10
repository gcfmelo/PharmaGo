package br.com.phago.pharmago;

/**
 * Created by Gustavo on 06/03/2017.
 */

public class Question {
    private int id;
    private String idSponsor;
    private String idCampaign;       // concatenation of sponsorCode+startDate+numberOfQuestions+pointsForRightAnswer+pointsForParticipation
    private int seqNumber;           // from the iterator in ArrayList
    private String questionLabel;

    public void setIdCampaign(String sponsorCode, String startDate, int numberOfQuestions,
                              int pointsForRightAnswer, int pointsForParticipation) {
        this.idCampaign = sponsorCode+
                startDate+
                Integer.toString(numberOfQuestions)+
                Integer.toString(pointsForRightAnswer)+
                Integer.toString(pointsForParticipation);
    }

    public Question(String sponsorCode, String startDate, int numberOfQuestions,
                    int pointsForRightAnswer, int pointsForParticipation, int seqNumber, String questionLabel) {
        this.idSponsor = sponsorCode;
        this.seqNumber = seqNumber;
        this.questionLabel = questionLabel;
        this.idCampaign = sponsorCode+
                startDate+
                Integer.toString(numberOfQuestions)+
                Integer.toString(pointsForRightAnswer)+
                Integer.toString(pointsForParticipation);
    }

}
