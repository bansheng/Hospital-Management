package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
	StringProperty acount, name, password, date;
	DoubleProperty money;

	public Patient(String acount, String name, String password, double money, String date) {
		super();
		this.acount = new SimpleStringProperty(acount);
		this.name = new SimpleStringProperty(name);
		this.password = new SimpleStringProperty(password);
		this.money = new SimpleDoubleProperty(money);
		this.date = new SimpleStringProperty(date);
	}

	public StringProperty Acount() {
		return acount;
	}

	public StringProperty Name() {
		return name;
	}

	public StringProperty Password() {
		return password;
	}

	public StringProperty Date() {
		return date;
	}

	public DoubleProperty Money() {
		return money;
	}

	public String getAcount() {
		return acount.get();
	}

	public String getDate() {
		return date.get();
	}

	public double getMoney() {
		return money.get();
	}

	public String getName() {
		return name.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setAcount(String acount) {
		this.acount.set(acount);
	}

	public void setDate(String date) {
		this.date.set(date);
		;
	}

	public void setMoney(double money) {
		this.money.set(money);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

}
