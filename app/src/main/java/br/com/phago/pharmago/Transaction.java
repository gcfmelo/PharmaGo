package br.com.phago.pharmago;

/**
 * JSONArray:
 *
 * http://www.benben.net.br/apiPharmaGo?email=gcfmelo@gmail.com&password=abc123&action=getTransactions&token=123456789
 *
 * [{"sponsorCode":"05080120000160","idCampaign":1,"idTransaction":1,"eventDate":"2017-02-28","title":"Vacina febre amarela","nature":"C","amount":150},
 * {"sponsorCode":"05080120000160","idCampaign":2,"idTransaction":2,"eventDate":"2017-03-01","title":"PharmaGo","nature":"C","amount":47}]
 *
 * Created by Gustavo on 05/03/2017.
 * CREATE TABLE pg_transaction(_id INTEGER PRIMARY KEY,idTransaction TEXT, idCampaign TEXT, title TEXT, sponsorCode TEXT,
 *                                                           eventDate TEXT, nature TEXT, amount INTEGER, created_at TEXT);
 *
 */

public class Transaction {

    private String sponsorCode, eventDate, title, nature;
    private int idCampaign, idTransaction, amount;

    public Transaction() {
    }

    public Transaction(String sponsorCode, String eventDate, String title, String nature, int idCampaign, int idTransaction, int amount) {
        this.sponsorCode = sponsorCode;
        this.eventDate = eventDate;
        this.title = title;
        this.nature = nature;
        this.idCampaign = idCampaign;
        this.idTransaction = idTransaction;
        this.amount = amount;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(int idCampaign) {
        this.idCampaign = idCampaign;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
