package org.ingomohr.docwriter.tests;

import org.ingomohr.docwriter.tests.util.BddContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests that reading templates works inside the OSGI bundle context as well.
 * <p>
 * This had been a problem in the past, that's why this test exists to prove
 * that it works.
 * </p>
 */
public class TestReadTemplateFromWithinBundleContext {

	private BddContext context;

	@BeforeEach
	void prep() {
		context = new BddContext();
	}

	@Test
	void readTemplateFromWorkspace_TemplateIsAvailable_TemplateWasReadSuccessfully() {
		givenProjectExists();
		givenDocxTemplateExistsInProject();
		givenSimpleDocxProcessorExists();

		whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate();

		thenWrittenDocumentIsBasedOnExistingTemplate();
	}

	private void givenProjectExists() {
		context.givenProjectExists();
	}

	private void givenDocxTemplateExistsInProject() {
		context.givenDocxTemplateExistsInProject();
	}

	private void givenSimpleDocxProcessorExists() {
		context.givenSimpleDocxProcessorExists();
	}

	private void whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate() {
		context.whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate();
	}

	private void thenWrittenDocumentIsBasedOnExistingTemplate() {
		context.thenWrittenDocumentIsBasedOnExistingTemplate();
	}

}
