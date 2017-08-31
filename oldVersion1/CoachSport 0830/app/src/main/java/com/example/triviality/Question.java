package com.example.triviality;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private int ID;
	private String QUESTION;
	private String OPTA;
	private String OPTB;
	private String OPTC;
	private String OPTD;
	private String CATEGORY;
	public Question()
	{
		ID=0;
		QUESTION="";
		OPTA="";
		OPTB="";
		OPTC="";
		OPTD="";
		CATEGORY="0";
	}
	public Question(String qUESTION, String oPTA, String oPTB, String oPTC,
			String oPTD, String cATEGORY) {

		QUESTION = qUESTION;
		OPTA = oPTA;
		OPTB = oPTB;
		OPTC = oPTC;
		OPTD = oPTD;
		CATEGORY = cATEGORY;
	}
	public int getID()
	{
		return ID;
	}
	public String getQUESTION() {
		return QUESTION;
	}
	public String getOPTA() {
		return OPTA;
	}
	public String getOPTB() {
		return OPTB;
	}
	public String getOPTC() {
		return OPTC;
	}
	public String getOPTD() {
		return OPTD;
	}
	public String getCATEGORY() {return CATEGORY;}
	public void setID(int id)
	{
		ID=id;
	}
	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}
	public void setOPTA(String oPTA) {
		OPTA = oPTA;
	}
	public void setOPTB(String oPTB) {
		OPTB = oPTB;
	}
	public void setOPTC(String oPTC) {
		OPTC = oPTC;
	}
	public void setOPTD(String oPTD) {
		OPTD = oPTD;
	}
	public void setRESULT(String cATEGORY) {CATEGORY = cATEGORY;}

	public static List<Question> addQuestions(String[] listQ, String[] listR, String categorie )
	{
		List<Question> Questions = new ArrayList<Question>();
		for (int i = 0; i < listQ.length; i++){
			Question q = new Question(listQ[i], listR[0], listR[1], listR[2], listR[3], categorie);
			Questions.add(q);
		}
		return Questions;
	}
}
