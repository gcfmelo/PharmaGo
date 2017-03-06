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
}
