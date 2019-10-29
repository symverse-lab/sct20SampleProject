package com.symverse.sct20.transaction.domain;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sct20TempleteVO {

	private String sctType; 
	private String method;
	private ArrayList<String> params;
	
}
