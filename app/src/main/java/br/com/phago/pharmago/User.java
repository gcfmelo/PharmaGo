package br.com.phago.pharmago;

/**
 * JSON Object:
 *
 * http://www.benben.net.br/apiPharmaGo?email=gcfmelo@gmail.com&password=abc123&action=login&token=123456789
 *
 * {"email":"gcfmelo@gmail.com","name":"GUSTAVO MELO","status":"ENABLE","cpf":"31201032415","
 * companyCode":"05511842000121","companyName":"SOLANGE ALVES VICENTE DROGARIA - ME","
 * companyLatitude":"-22.8185009","companyLongitude":"-47.0930623"}
 *
 * Created by Gustavo on 05/03/2017.
 */

public class User {

    private String email;
    private String cpf;
    private String name;
    private String password;
    private String userAccountStatus;
    private String companyCode;
    private String companyName;
    private String companyLatitude;
    private String companyLongitude;
    private String createdAt;

    // constructors

    public User(){

    }
    public User(String email, String password, String cpf, String name, String status, String companyCode, String companyName, String companyLatitude, String companyLongitude) {
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.userAccountStatus = userAccountStatus;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.companyLatitude = companyLatitude;
        this.companyLongitude = companyLongitude;
    }


    // setters


    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserAccountStatus(String status) {
        this.userAccountStatus = status;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyLatitude(String companyLatitude) {
        this.companyLatitude = companyLatitude;
    }

    public void setCompanyLongitude(String companyLongitude) {
        this.companyLongitude = companyLongitude;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // getters


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getUserAccountStatus() {
        return userAccountStatus;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyLatitude() {
        return companyLatitude;
    }

    public String getCompanyLongitude() {
        return companyLongitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
