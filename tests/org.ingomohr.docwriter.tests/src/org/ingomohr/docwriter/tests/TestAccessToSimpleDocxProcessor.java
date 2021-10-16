package org.ingomohr.docwriter.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.ingomohr.docwriter.docx.SimpleDocxProcessor;
import org.junit.jupiter.api.Test;

public class TestAccessToSimpleDocxProcessor {

	@Test
	void writeSampleDoc_DocWasWritten() {
		SimpleDocxProcessor processor = new SimpleDocxProcessor();

		processor.createDocument();

		processor.addToc();
		processor.addPageBreak();
		processor.addHeadlineH1("Hello");
		processor.addHeadlineH2("World");
		processor.addMarkdown("Hello **world**!");
		processor.updateToc();

		try {
			File tempFile = File.createTempFile("docwriter-eclipse-test", ".docx");
			tempFile.deleteOnExit();

			processor.saveDocumentToPath(tempFile);

			final long length = tempFile.length();
			final long expectedMinLength = 6_000;
			assertTrue(length >= expectedMinLength, "Expected docx file to be >= " + expectedMinLength + ": " + length);
		} catch (IOException e) {
			fail(e);
		}
	}

}
