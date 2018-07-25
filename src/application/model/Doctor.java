package application.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
	private StringProperty  acount, office_code, name, nameP, password, date;
	private BooleanProperty expert;
	public Doctor(String acount, String office_code, String name, String nameP,
			String password, Boolean expert, String date) {
		super();
		this.acount = new SimpleStringProperty(acount);
		this.office_code = new SimpleStringProperty(office_code);
		this.name = new SimpleStringProperty(name);
		this.nameP = new SimpleStringProperty(nameP);
		this.password = new SimpleStringProperty(password);
		this.date = new SimpleStringProperty(date);
		this.expert = new SimpleBooleanProperty(expert);
	}
	public String getAcount() {
		return acount.get();
	}

	public String getDate() {
		return date.get();
	}

	public String getName() {
		return name.get();
	}

	public String getNameP() {
		return nameP.get();
	}

	public String getOffice_code() {
		return office_code.get();
	}

	public String getPassword() {
		return password.get();
	}

	public boolean isExpert() {
		return expert.get();
	}

	public void setAcount(String acount) {
		this.acount.set(acount);
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public void setExpert(boolean expert) {
		this.expert.set(expert);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setNameP(String nameP) {
		this.nameP.set(nameP);
	}

	public void setOffice_code(String office_code) {
		this.office_code.set(office_code);
	}

	public void setPassword(String password) {
		this.password.set(password);
	}
	
	
	public StringProperty Acount() {
		return acount;
	}

	public StringProperty Office_code() {
		return office_code;
	}

	public StringProperty Name() {
		return name;
	}

	public StringProperty NameP() {
		return nameP;
	}

	public StringProperty Password() {
		return password;
	}

	public StringProperty Date() {
		return date;
	}

	public BooleanProperty Expert() {
		return expert;
	}
}
