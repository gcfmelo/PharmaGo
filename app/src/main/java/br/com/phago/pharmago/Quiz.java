package br.com.phago.pharmago;

/**
 * Created by Gustavo on 06/03/2017.
 */

public class Quiz {
    //CREATE TABLE pg_quiz(_id INTEGER PRIMARY KEY, idQuiz INTEGER, sponsorCode TEXT, token TEXT, status TEXT, created_at TEXT);
    private int id;
    private int idQuiz;
    private String sponsorCode;
    private String token;
    private String status;
    private String createdAt;

    public Quiz(){

    }

    public Quiz(int idQuiz, String sponsorCode, String token, String status) {
        this.idQuiz = idQuiz;
        this.sponsorCode = sponsorCode;
        this.token = token;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Quiz(int id, int idQuiz, String sponsorCode, String token, String status) {
        this.id = id;
        this.idQuiz = idQuiz;
        this.sponsorCode = sponsorCode;
        this.token = token;
        this.status = status;
        this.createdAt = createdAt;
    }

    //setters

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    //getters
    public int getId() {
        return id;
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }


}
