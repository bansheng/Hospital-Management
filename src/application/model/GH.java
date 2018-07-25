package application.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class GH {
	private StringProperty index, name, date, type;

	public GH(int index, String name, String date, boolean type) {
		super();
		String s = "" + index;
		int a = 6-s.length();
		while((a--) != 0) {
			s = "0"+s;
		}
		this.index = new SimpleStringProperty(s);
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleStringProperty(date);
		if(type)
			this.type = new SimpleStringProperty("×¨¼ÒºÅ");
		else
			this.type = new SimpleStringProperty("ÆÕÍ¨ºÅ");
	}

	public StringProperty Index() {
		return index;
	}

	public StringProperty Name() {
		return name;
	}

	public StringProperty Date() {
		return date;
	}

	public StringProperty Type() {
		return type;
	}
	
	public String getIndex() {
		return index.get();
	}

	public String getName() {
		return name.get();
	}

	public String getDate() {
		return date.get();
	}

	public String getType() {
		return type.get();
	}

	public void setIndex(int index) {
		String s = "" + index;
		int a = 6-s.length();
		while((a--) != 0) {
			s = "0"+s;
		}
		this.index.set(s);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public void setType(String type) {
		this.type.set(type);
	}
	
}

