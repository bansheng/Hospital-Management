package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DoctorIncome {
	private StringProperty nameKS, codeDoctor, nameDoctor, type;
//	private String nameKS, codeDoctor, nameDoctor, type;
	private StringProperty num, income;
//	private int num, income;
	
	public DoctorIncome(String nameKS, String codeDoctor, String nameDoctor, String type, int num, int income) {
		super();
		this.nameKS = new SimpleStringProperty(nameKS);
		this.codeDoctor = new SimpleStringProperty(codeDoctor);
		this.nameDoctor = new SimpleStringProperty(nameDoctor);
		this.type = new SimpleStringProperty(type);
		this.num = new SimpleStringProperty("" + num);
		this.income = new SimpleStringProperty("" + income);
	}

	public String getNameKS() {
		return nameKS.get();
	}

	public String getCodeDoctor() {
		return codeDoctor.get();
	}

	public String getNameDoctor() {
		return nameDoctor.get();
	}

	public String getType() {
		return type.get();
	}

	public String getNum() {
		return num.get();
	}

	public String getIncome() {
		return income.get();
	}

	public void setNameKS(String nameKS) {
		this.nameKS.set(nameKS);
	}

	public void setCodeDoctor(String codeDoctor) {
		this.codeDoctor.set(codeDoctor);
	}

	public void setNameDoctor(String nameDoctor) {
		this.nameDoctor.set(nameDoctor);
	}

	public void setType(String type) {
		this.type.set(type);
	}

	public void setNum(int num) {
		this.num.set("" + num);
	}

	public void setIncome(int income) {
		this.income.set("" + income);
	}

	public StringProperty NameKS() {
		return nameKS;
	}

	public StringProperty CodeDoctor() {
		return codeDoctor;
	}

	public StringProperty NameDoctor() {
		return nameDoctor;
	}

	public StringProperty Type() {
		return type;
	}

	public StringProperty Num() {
		return num;
	}

	public StringProperty Income() {
		return income;
	}
	
}
