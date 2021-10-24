package org.ingomohr.docwriter.tests.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.ingomohr.docwriter.docx.SimpleDocxProcessor;
import org.ingomohr.docwriter.docx.util.DocxDataInspector;

/**
 * Provides BDD methods for easier writing of tests.
 * <p>
 * Tests that use this context should call {@link #tearDown()} after they
 * finished.
 * </p>
 */
public class BddContext {

	private static final String TEMPLATE_TEXT = "TEMPLATE_TEXT";

	private static final String TEMPLATE_FROM_BUNDLE_TEXT = "TEMPLATE_FROM_BUNDLE";

	private static final String ADDED_TEXT = "ADDED_TEXT";

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
		proc.addMarkdown(TEMPLATE_TEXT);

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
		whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate(
				wsFileToFile(getTemplateFileFromWorkspace()));
	}

	public void whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplateFromBundle() {
		whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate(getTemplateFileFromBundle());
	}

	private void whenSimpleDocxProcessorIsToldToWriteADocumentBasedOnExistingTemplate(File existingTemplate) {
		try {
			processor.loadDocument(existingTemplate);
		} catch (IOException e) {
			fail("Cannot read template from workspace", e);
		}

		processor.addMarkdown(ADDED_TEXT);

		try {
			processor.saveDocumentToPath(wsFileToFile(getTargetFileFromWorkspace()));
		} catch (IOException e) {
			fail("Cannot write target file", e);
		}

		whenProjectHasBeenRefreshed();

	}

	public void thenWrittenDocumentIsBasedOnExistingTemplate() {
		thenWrittenDocumentIsBasedOnExistingTemplate(TEMPLATE_TEXT);
	}

	public void thenWrittenDocumentIsBasedOnExistingTemplateFromBundle() {
		thenWrittenDocumentIsBasedOnExistingTemplate(TEMPLATE_FROM_BUNDLE_TEXT);
	}

	private void thenWrittenDocumentIsBasedOnExistingTemplate(String expectedTemplateText) {
		try {
			String text = docxToText(wsFileToFile(getTargetFileFromWorkspace()));

			MatcherAssert.assertThat(text, CoreMatchers.containsString(expectedTemplateText));
			MatcherAssert.assertThat(text, CoreMatchers.containsString(ADDED_TEXT));

		} catch (Docx4JException e) {
			fail("Cannot read target docx", e);
		}
	}

	public void givenDocxTemplateExistsInBundle() {
		File file = getTemplateFileFromBundle();

		assertTrue(file.length() > 0, "Expected template from bundle not to be empty");
	}

	private File getTemplateFileFromBundle() {
		URL url = Platform.getBundle("org.ingomohr.docwriter.tests").getEntry("res/template-from-bundle.docx");
		File file = null;
		try {
			file = new File(FileLocator.resolve(url).toURI());
		} catch (Exception e) {
			fail("Cannot find template from bundle", e);
		}
		return file;
	}

	/**
	 * Deletes and data created by the context - so that the context doesn't break
	 * tests that run later.
	 */
	public void tearDown() {
		if (project != null) {
			try {
				project.delete(true, null);
			} catch (CoreException e) {
				System.err.println("Error deleting project: " + e.getMessage());
				e.printStackTrace();
			}
		}

		processor = null;
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

	private static String docxToText(File file) throws Docx4JException {
		List<String> vals = new ArrayList<>();

		WordprocessingMLPackage doc = WordprocessingMLPackage.load(file);

		List<Text> ts = new DocxDataInspector().getAllElements(doc.getMainDocumentPart(), Text.class);
		for (Text text : ts) {
			vals.add(text.getValue());
		}

		return String.join(",", vals);
	}

}
