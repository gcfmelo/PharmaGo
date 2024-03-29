package br.com.phago.pharmago;

/**
 * Created by Gustavo on 06/03/2017.
 * CREATE TABLE pg_sponsor(_id INTEGER PRIMARY KEY,sponsorId INTEGER, sponsorCode TEXT, sponsorName TEXT, created_at TEXT);
 *
 */
public class Sponsor {
    private int sponsorId;
    private String sponsorCode;
    private String sponsorName;
    //private String createdAt;


    // constructors

    public Sponsor() {
    }

    public Sponsor(String sponsorCode, String sponsorName) {
        this.sponsorCode = sponsorCode;
        this.sponsorName = sponsorName;
    }

    public Sponsor(int id, String sponsorCode, String sponsorName) {
        this.sponsorId = id;
        this.sponsorCode = sponsorCode;
        this.sponsorName = sponsorName;
    }

    // setters

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

// getters


    public int getSponsorId() {
        return sponsorId;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public String getSponsorName() {
        return sponsorName;
    }


}
