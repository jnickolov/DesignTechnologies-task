package com.designtechnologies.task.jbn.model.contragents;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Client {
	@NonNull private final String name;
	@NonNull private final String vatCode;
}
