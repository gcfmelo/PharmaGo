package br.com.phago.pharmago;

/**
 * JSONArray:
 * http://www.benben.net.br/apiPharmaGo?email=gcfmelo@gmail.com&password=abc123&action=getQuiz&token=123456789
 *
 * [
 *      {"idQuiz":1,
 *          "campaign":
 *              {"sponsorCode":"05080120000160",
 *              "idCampaign":1,
 *              "sponsorName":"Laboratório ABCD",
 *              "startDate":"2017-02-28",
 *              "endDate":"2017-03-31",
 *              "numberOfQuestions":2,
 *              "pointsForRightAnswer":50,
 *              "pointsForParticipation":100,
 *                      "questions":[
 *                                  {"questionLabel":"Qual a cor da febre que está na moda?",
 *                                          "options":
 *                                                  [
 *                                                  {"sequential":1, "optionLabel":"azul",     "rightAnswer":false} ,
 *                                                  {"sequential":2, "optionLabel":"amarela",  "rightAnswer":true}  ,
 *                                                  {"sequential":3, "optionLabel":"vermelha", "rightAnswer":false} ,
 *                                                  {"sequential":4, "optionLabel":"branca",   "rightAnswer":false}   ]
 *                                                  },
 *                                  {"questionLabel":"A peste xxxxx matou mais que a febre amarela. Qual a cor de xxxxx?",
 *                                          "options":
 *                                                  [
 *                                                  {"sequential":1,  "optionLabel":"verde",    "rightAnswer":false},
 *                                                  {"sequential":2,  "optionLabel":"lilás",    "rightAnswer":false},
 *                                                  {"sequential":3,  "optionLabel":"marrom",   "rightAnswer":false},
 *                                                  {"sequential":4,  "optionLabel":"negra",     "rightAnswer":true},
 *                                                  {"sequential":5,  "optionLabel":"listrada", "rightAnswer":false}  ]
 *                                                  }
 *                               ]
 *              },
 *              "token":"cb3130e1-8ad4-4b61-a305-65f6d1c53800",
 *              "status":"Respondido"
 *      },
 *      {"idQuiz":2,
 *          "campaign":
 *              {"sponsorCode":"05080120000160",
 *              "idCampaign":2,
 *              "sponsorName":"Laboratório ABCD",
 *              "startDate":"2017-02-28",
 *              "endDate":"2017-03-31",
 *              "numberOfQuestions":1,
 *              "pointsForRightAnswer":12,
 *              "pointsForParticipation":35,
 *                      "questions":[
 *                                  {"questionLabel":"Como ganhar dinheiro aprendendo sobre o medicamentos?",
 *                                          "options":
 *                                          [
 *                                          {"sequential":1,"optionLabel":"Vendendo borboletas","rightAnswer":false},
 *                                          {"sequential":2,"optionLabel":"Enxugando gelo",     "rightAnswer":false},
 *                                          {"sequential":3,"optionLabel":"Aderindo ao PharmaGo","rightAnswer":true},
 *                                          {"sequential":4,"optionLabel":"Penteando macaco",   "rightAnswer":false} ]
 *                                   }
 *                                   ]
 *             },
 *             "token":"dc1f749f-db25-4c3f-a21f-76916b1e8f20",
 *             "status":"Aguardando Resposta"
 *       }
 * ]
 *
 * Created by Gustavo on 26/02/2017.
 */

public class Campaign {
    private int _id;  // local unique id of the Campaign (autoincrement in SQLite)
    private String idCampaign; // concatenation of sponsorCode+startDate+numberOfQuestions+pointsForRightAnswer+pointsForParticipation
    private String  sponsorCode;  // unique id of the Sponsor originally: idLaboratory
    private String sponsorName;  // name of the sponsor (orig: nameOfLaboratory)
    private String startDate;  // format YYYY-MM-DD like in "2017-01-17"
    private String endDate;  // format YYYY-MM-DD like in "2017-01-17"
    private int numberOfQuestions   ;   // number of questions in this campaign
    private int pointsForRightAnswer;    // sponsor defined: maximum number of points attainable from 100% correct answers
    private int pointsForParticipation;  // sponsor defined: points granted for ay user submitting a complete answer for this campaign
    private String status;  // Possible values are: "ANSWERED", TODO define possible statuses


    // empty constructors
    public Campaign(){
    }

    public Campaign(String sponsorCode, String sponsorName, String startDate, String endDate, int numberOfQuestions,
                                                int pointsForRightAnswer, int pointsForParticipation, String status) {
        this.idCampaign=sponsorCode+startDate+Integer.toString(numberOfQuestions)+Integer.toString(pointsForRightAnswer)+Integer.toString(pointsForParticipation);
        this.sponsorCode = sponsorCode;
        this.sponsorName = sponsorName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfQuestions = numberOfQuestions;
        this.pointsForRightAnswer = pointsForRightAnswer;
        this.pointsForParticipation = pointsForParticipation;
        this.status = status;
    }

    public Campaign(int _id, String sponsorCode, String sponsorName, String startDate, String endDate, int numberOfQuestions,
                                                        int pointsForRightAnswer, int pointsForParticipation, String status) {
        this._id = _id;
        this.idCampaign=sponsorCode+startDate+Integer.toString(numberOfQuestions)+Integer.toString(pointsForRightAnswer)+Integer.toString(pointsForParticipation);
        this.sponsorCode = sponsorCode;
        this.sponsorName = sponsorName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfQuestions = numberOfQuestions;
        this.pointsForRightAnswer = pointsForRightAnswer;
        this.pointsForParticipation = pointsForParticipation;
        this.status = status;
    }

    // setters


    public void setIdCampaign(String sponsorCode, String startDate, int numberOfQuestions,
                              int pointsForRightAnswer, int pointsForParticipation) {
        this.idCampaign = sponsorCode+
                startDate+
                Integer.toString(numberOfQuestions)+
                Integer.toString(pointsForRightAnswer)+
                Integer.toString(pointsForParticipation);
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setPointsForRightAnswer(int pointsForRightAnswer) {
        this.pointsForRightAnswer = pointsForRightAnswer;
    }

    public void setPointsForParticipation(int pointsForParticipation) {
        this.pointsForParticipation = pointsForParticipation;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /// getters


    public int get_id() {
        return _id;
    }

    public String getIdCampaign() {
        return idCampaign;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getPointsForRightAnswer() {
        return pointsForRightAnswer;
    }

    public int getPointsForParticipation() {
        return pointsForParticipation;
    }

    public String getStatus() {
        return status;
    }
}
