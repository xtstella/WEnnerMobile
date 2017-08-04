package com.limsi.coachsport;




public class Profil {
    private String username;
    private String password;
    private String result;

    public Profil(){
        username = null;
        password = null;
        result = null;

    }
    public Profil(String uSERNAME, String pASSWORD){
        username = uSERNAME;
        password = pASSWORD;
    }

    public String getResult() {
        return result;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public void ProfilToText(Profil profil, String filename ){



    }
}
