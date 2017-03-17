package br.com.phago.pharmago;

/**
 * Created by Gustavo on 06/03/2017.
 */

public class Question {
    private int idQuestion;
    private int idCampaign;
    private int idSponsor;     // not necessary due to relation 1 Sponsor : N Campaign
    private String label;
//    private List<Option> optionList;

    // constructors
    public Question() {
    }
    public Question(int idQuestion, int idCampaign, int idSponsor, String label) {
        this.idQuestion = idQuestion;
        this.idCampaign = idCampaign;
        this.idSponsor = idSponsor;
        this.label = label;
    }

    // setters

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setIdCampaign(int idCampaign) {
        this.idCampaign = idCampaign;
    }

    public void setIdSponsor(int idSponsor) {
        this.idSponsor = idSponsor;
    }

    public void setLabel(String label) {
        this.label = label;
    }

//    public void setOptionList(List<Option> optionList) {
//        this.optionList = optionList;
//    }

// getters

    public int getIdQuestion() {
        return idQuestion;
    }

    public int getIdCampaign() {
        return idCampaign;
    }

    public int getIdSponsor() {
        return idSponsor;
    }

    public String getLabel() {
        return label;
    }

//    public List<Option> getOptionList() {
//        return optionList;
//    }
}
