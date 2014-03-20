package com.gmail.xendroidzx.cah;

public class BlackCard {

	private String s;

	private int answers;

	public BlackCard(String s) {
		this.s = s;
		for(char c : s.toCharArray()) {
			if(c == '_') {
				answers++;
			}
		}
	}

	public int getAnswers() {
		return answers;
	}

	public String getText() {
		return s;
	}

}
