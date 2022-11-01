package com.designtechnologies.task.jbn.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.designtechnologies.task.jbn.dto.DocumentData;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.documents.CreditNote;
import com.designtechnologies.task.jbn.model.documents.DebitNote;
import com.designtechnologies.task.jbn.model.documents.Document;
import com.designtechnologies.task.jbn.model.documents.Invoice;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.exceptions.InvalidDocumentDataException;

import lombok.NonNull;

@Service
public class DocumentDataService {
	private static final String delim = ",";
	private static final int ITEM_COUNT = 7;

	public List<Invoice> processRawData (@NonNull String csvData)  throws InvalidDocumentDataException, InvalidCurrencyCodeException {
		List<DocumentData> docDatas = parseCSV (csvData);
		List<Invoice> invoices = new LinkedList<>();
		List<DebitNote> debitNotes = new LinkedList<>();
		List<CreditNote> creditNotes = new LinkedList<>();

		for (DocumentData data: docDatas) {
			Document doc = Document.ofData (data);
			if (doc instanceof Invoice) {
				validateAndAddInvoice (invoices, (Invoice)doc);
			} else if (doc instanceof DebitNote) {
				validateAndAddDebitNote (debitNotes, (DebitNote)doc);
			} else if (doc instanceof CreditNote) {
				validateAndAddCreditNote (creditNotes, (CreditNote)doc);
			}
		}
		
		linkDebitNotes (invoices, debitNotes);
		linkCreditNotes (invoices, creditNotes);
		
		return invoices;
	}

	private Document findByDocNo (List<? extends Document> docs, String docNo) {
		return docs.stream()
			.filter(d -> d.getDocNo().equals(docNo))
			.findFirst()
			.orElse (null);
	}

	
	private void linkCreditNotes (List<Invoice> invoices, List<CreditNote> creditNotes) throws InvalidDocumentDataException {
		for (CreditNote note: creditNotes) {
			Invoice inv = (Invoice) findByDocNo (invoices, note.getParentDocNo());
			if (inv == null) {
				throw new InvalidDocumentDataException ("Credit note not linked to any invoice, DocNo:" + note.getDocNo());
			}
			inv.addCreditNote (note);
		}
	}


	private void linkDebitNotes (List<Invoice> invoices, List<DebitNote> debitNotes) throws InvalidDocumentDataException {
		for (DebitNote note: debitNotes) {
			Invoice inv = (Invoice) findByDocNo (invoices, note.getParentDocNo());
			if (inv == null) {
				throw new InvalidDocumentDataException ("Debit note not linked to any invoice, DocNo:" + note.getDocNo());
			}
			inv.addDebitNote (note);
		}
	}

	private void validateAndAddInvoice (List<Invoice> invoices, Invoice doc) throws InvalidCurrencyCodeException {
		if (CurrencyConverter.isKnownCurrency (doc.getValue().getCurrency())) {
			invoices.add (doc);
		} else {
			throw new InvalidCurrencyCodeException (
				"Unknown currency: [" + doc.getValue().getCurrency().getCode()
				+ "] for document No: " + doc.getDocNo());
		}
	}

	private void validateAndAddCreditNote (List<CreditNote> creditNotes, CreditNote doc) {
		creditNotes.add (doc);
	}

	private void validateAndAddDebitNote (List<DebitNote> debitNotes, DebitNote doc) {
		debitNotes.add (doc);
	}

	private List<DocumentData> parseCSV (@NonNull String csvData)  throws InvalidDocumentDataException {
		List<DocumentData> lst = new LinkedList<>();
		BufferedReader rdr = new BufferedReader (new StringReader(csvData));
		String line;
		try {
			line = rdr.readLine();  //  skip first line
			line = rdr.readLine();  //   first data line
			while (line != null) {
				DocumentData data = parseLine (line);
				validateAndAddRawData (lst, data);
				
				line = rdr.readLine();
			}
		} catch (IOException e) {
			throw new InvalidDocumentDataException ("Error reading document data");
		}
		
		return lst;
	}

	private void validateAndAddRawData (List<DocumentData> existingData, DocumentData data) throws InvalidDocumentDataException {
		// Check for duplicated document number
		if (existingData.stream()
				.filter (doc -> doc.getDocNo().equals(data))
				.findFirst().isPresent()) {
			throw new InvalidDocumentDataException (
					"Duplicated document No: " + data.getDocNo());
		}
		
		// Check for invalid document type
		if (! Document.isValidType (data.getType())) {
			throw new InvalidDocumentDataException (
				"Invalid document type (" + data.getType() + ") for document No. " + data.getDocNo());
		}
		
		// Check for not connected debit/credit notes
		if (Document.needsParentDocument (data.getType()) && (data.getParentDocNo().isBlank())) {
			throw new InvalidDocumentDataException (
				"Invalid parent for document No. " + data.getDocNo());
		}
		
		existingData.add (data);
		
	}

	private DocumentData parseLine (@NonNull String line) throws InvalidDocumentDataException {
		line = line.trim();
		String [] items = line.split (delim);
		
		if (items.length != ITEM_COUNT) {
			throw new InvalidDocumentDataException ("Invalid input data: " + line);
		}
		
		return new DocumentData (
				items[0].trim(),items[1].trim(), items[2].trim(), 
				Integer.parseInt(items[3].trim()),
				items[4].trim(),items[5].trim(), items[6].trim());
	}
}
