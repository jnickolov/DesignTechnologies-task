package com.designtechnologies.task.jbn.dto;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class DocumentData {
	@NonNull private final String clientName;
	@NonNull private final String clientVat;
	@NonNull private final String docNo;
	@NonNull private final Integer type;
			 private final String parentDocNo;
	@NonNull final private String currencyCode;
	@NonNull final private String total;
}
