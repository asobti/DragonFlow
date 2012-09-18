package me.notimplementedexception.dragonflow;

public class QuestionFilter {	
	public String value;
	public filterType type;
	
	public QuestionFilter() {
		this.value = "";
		this.type = filterType.NONE;
	}
	
	public QuestionFilter(filterType filter, String val) {
		this.value = val;
		this.type = filter;
	}
	
}
