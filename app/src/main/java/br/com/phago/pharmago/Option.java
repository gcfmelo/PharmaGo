package br.com.phago.pharmago;

/**
 * Created by Gustavo on 12/03/2017.
 */

public class Option {

    private Integer idSponsor;
    private Integer idCampaign;
    private Integer idQuestion;
    private Integer sequential;
    private String label;
    private String rightAnswer;
    private String userAnswer;


    // constructors
    public Option() {
    }
    //// withou user answer... set user answer = "U" for Undifined
    public Option(Integer idSponsor, Integer idCampaign, Integer idQuestion, Integer sequential, String label, String rightAnswer) {
        this.idSponsor = idSponsor;
        this.idCampaign = idCampaign;
        this.idQuestion = idQuestion;
        this.sequential = sequential;
        this.label = label;
        this.rightAnswer = rightAnswer;
        this.userAnswer = "U";
    }

    public Option(Integer idSponsor, Integer idCampaign, Integer idQuestion, Integer sequential, String label, String rightAnswer, String userAnswer) {
        this.idSponsor = idSponsor;
        this.idCampaign = idCampaign;
        this.idQuestion = idQuestion;
        this.sequential = sequential;
        this.label = label;
        this.rightAnswer = rightAnswer;
        this.userAnswer = userAnswer;
    }

    // setters

    public void setIdSponsor(Integer idSponsor) {
        this.idSponsor = idSponsor;
    }
    public void setIdCampaign(Integer idCampaign) {
        this.idCampaign = idCampaign;
    }
    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }
    public void setSequential(Integer sequential) {
        this.sequential = sequential;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    // gettters


    public Integer getIdSponsor() {
        return idSponsor;
    }
    public Integer getIdCampaign() {
        return idCampaign;
    }
    public Integer getIdQuestion() {
        return idQuestion;
    }
    public Integer getSequential() {
        return sequential;
    }
    public String getLabel() {
        return label;
    }
    public String getRightAnswer() {
        return rightAnswer;
    }
    public String getUserAnswer() {
        return userAnswer;
    }
}
