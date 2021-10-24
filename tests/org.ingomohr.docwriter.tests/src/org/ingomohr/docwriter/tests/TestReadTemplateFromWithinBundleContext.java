package org.ingomohr.docwriter.tests;

import org.ingomohr.docwriter.tests.util.BddContext;
import org.junit.jupiter.api.AfterEach;
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

	@AfterEach
	void post() {
		context.tearDown();
	}

	@Test
	void readTemplateFromWorkspace_TemplateIsAvailable_TemplateWasReadSuccessfully() {
		givenProjectExists();
		givenDocxTemplateExistsInProject();
		givenSimpleDocxProcessorExists();

		whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate();

		thenWrittenDocumentIsBasedOnExistingTemplate();
	}

	@Test
	void readTemplateFromBundle_TemplateIsAvailable_TemplateWasReadSuccessfully() {
		givenProjectExists();
		givenDocxTemplateExistsInBundle();
		givenSimpleDocxProcessorExists();

		whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplateFromBundle();

		thenWrittenDocumentIsBasedOnExistingTemplateFromBundle();
	}

	private void thenWrittenDocumentIsBasedOnExistingTemplateFromBundle() {
		context.thenWrittenDocumentIsBasedOnExistingTemplateFromBundle();
	}

	private void whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplateFromBundle() {
		context.whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplateFromBundle();
	}

	private void givenDocxTemplateExistsInBundle() {
		context.givenDocxTemplateExistsInBundle();
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
