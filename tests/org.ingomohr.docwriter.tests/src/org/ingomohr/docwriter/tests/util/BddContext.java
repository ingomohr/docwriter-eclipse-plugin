package org.ingomohr.docwriter.tests.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.ingomohr.docwriter.docx.SimpleDocxProcessor;

/**
 * Provides BDD methods for easier writing of tests.
 */
public class BddContext {

	private IProject project;

	private SimpleDocxProcessor processor;

	public void givenProjectExists() {
		String name = "p1";
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		try {
			project.create(null);
			project.open(null);
		} catch (CoreException e) {
			fail("Cannot create project", e);
		}

		assertEquals(true, project.isOpen(), "Expected project to be existing and open.");
	}

	public void givenDocxTemplateExistsInProject() {
		SimpleDocxProcessor proc = new SimpleDocxProcessor();
		proc.createDocument();
		proc.addMarkdown("TEMPLATE_TEXT");

		IFile wsFile = getTemplateFileFromWorkspace();
		assertFalse(wsFile.exists(), "Expecting template file in workspace not to exist: " + wsFile);

		File file = wsFileToFile(wsFile);

		try {
			proc.saveDocumentToPath(file);
		} catch (IOException e) {
			fail("Cannot prepare template file", e);
		}

		whenProjectHasBeenRefreshed();

		assertTrue(file.exists(), "Expecting template io.file to exist: " + file);
		assertTrue(file.length() > 0, "Expecting template io.file to contain data: " + file);

		assertTrue(wsFile.exists(), "Expecting template file in workspace to exist: " + wsFile);
	}

	public void whenProjectHasBeenRefreshed() {
		try {
			project.refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			fail("Cannot refresh project", e);
		}
	}

	public void givenSimpleDocxProcessorExists() {
		processor = new SimpleDocxProcessor();
	}

	public void whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate() {
		try {
			processor.loadDocument(wsFileToFile(getTemplateFileFromWorkspace()));
		} catch (IOException e) {
			fail("Cannot read template from workspace", e);
		}

		processor.addMarkdown("WRITING_ONTO_IMPORT_FROM_TEMPLATE");

		try {
			processor.saveDocumentToPath(wsFileToFile(getTargetFileFromWorkspace()));
		} catch (IOException e) {
			fail("Cannot write target file", e);
		}

		whenProjectHasBeenRefreshed();

	}

	public void thenWrittenDocumentIsBasedOnExistingTemplate() {
		final long lengthOfTemplate = wsFileToFile(getTemplateFileFromWorkspace()).length();
		final long lengthOfTarget = wsFileToFile(getTargetFileFromWorkspace()).length();

		assertTrue(lengthOfTarget > lengthOfTemplate,
				"Expected target file to be bigger than template file: " + lengthOfTarget + ":" + lengthOfTemplate);
	}

	private IFile getTemplateFileFromWorkspace() {
		return project.getFile("template.docx");
	}

	private IFile getTargetFileFromWorkspace() {
		return project.getFile("target.docx");
	}

	private static File wsFileToFile(IFile file) {
		return file.getLocation().toFile();
	}

}
