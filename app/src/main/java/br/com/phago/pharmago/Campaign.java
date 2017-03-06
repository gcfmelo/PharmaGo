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
    private int idSponsor;  // unique id of the Sponsor originally: idLaboratory
    private int idCampaign;    // unique id of this Campaign
    private int sequential;    // sequential number of this Campaign returned by Web Services
    private String sponsorName;  // name of the sponsor (orig: nameOfLaboratory)
    private String eventDateOfCampaign;  // format YYYY-MM-DD like in "2017-01-17"
    private int questionsOfCampaign   ;   // number of questions in this campaign
    private int pointsForRightAnswer;     // sponsor defined: maximum number of points attainable from 100% correct answers
    private int pointsForParticipation;  // sponsor defined: points granted for ay user submitting a complete answer for this campaign
    private String status;  // Possible values are: "ANSWERED", TODO define possible statuses
    private String createdAt; // Timestamp for the creation of this Campaign in String format like "2017-01-17 13:53:38.0"

    // empty constructor
    public Campaign(){
    }
    // Complete constructor for Campaign
    public Campaign(int idSponsor, int idCampaign, int sequential,
                    String sponsorName, String eventDateOfCampaign,
                    int questionsOfCampaign, int pointsForRightAnswer,
                    int pointsForParticipation, String status, String createdAt) {
        this.idSponsor = idSponsor;
        this.idCampaign = idCampaign;
        this.sequential = sequential;
        this.sponsorName = sponsorName;
        this.eventDateOfCampaign = eventDateOfCampaign;
        this.questionsOfCampaign = questionsOfCampaign;
        this.pointsForRightAnswer = pointsForRightAnswer;
        this.pointsForParticipation = pointsForParticipation;
        this.status = status;
        this.createdAt = createdAt;
    }

    /// getters

    public int get_id() {
        return _id;
    }

    public int getIdCampaign() {
        return idCampaign;
    }

    public String getEventDate() {
        return eventDateOfCampaign;
    }

    public int getQuestions() {
        return questionsOfCampaign;
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

    @Override
    public String toString() {
        return "Campaign{" +
                "createdAt='" + createdAt + '\'' +
                ", sponsorName='" + sponsorName + '\'' +
                ", idCampaign=" + idCampaign +
                '}';
    }
}
