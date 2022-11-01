package com.designtechnologies.task.jbn.model.documents;

import java.util.Objects;

import com.designtechnologies.task.jbn.model.contragents.Client;
import com.designtechnologies.task.jbn.model.money.Money;

import lombok.Value;

@Value
public class DebitNote extends Document {

	private final String parentDocNo;

	protected DebitNote (String docNo, Client client, Money money, String parentDocNo) {
		super(docNo, client, money);
		this.parentDocNo = parentDocNo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DebitNote other = (DebitNote) obj;
		return Objects.equals (getDocNo(), other.getDocNo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(getDocNo());
		return result;
	}

	
}
