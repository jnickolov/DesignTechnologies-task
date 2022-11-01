package com.designtechnologies.task.jbn.dto;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor
@ToString
public class CustomerBalance {
	private String name;
	private double balance;
}
