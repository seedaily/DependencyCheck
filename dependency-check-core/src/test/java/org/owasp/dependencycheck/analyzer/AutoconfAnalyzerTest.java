/*
 * This file is part of dependency-check-core.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) 2015 Institute for Defense Analyses. All Rights Reserved.
 */
package org.owasp.dependencycheck.analyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owasp.dependencycheck.BaseTest;
import org.owasp.dependencycheck.analyzer.exception.AnalysisException;
import org.owasp.dependencycheck.dependency.Dependency;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.owasp.dependencycheck.dependency.Confidence;
import org.owasp.dependencycheck.dependency.Evidence;
import org.owasp.dependencycheck.dependency.EvidenceType;

/**
 * Unit tests for AutoconfAnalyzer. The test resources under autoconf/ were
 * obtained from outside open source software projects. Links to those projects
 * are given below.
 *
 * @author Dale Visser
 * @see <a href="http://readable.sourceforge.net/">Readable Lisp S-expressions
 * Project</a>
 * @see <a href="https://gnu.org/software/binutils/">GNU Binutils</a>
 * @see <a href="https://gnu.org/software/ghostscript/">GNU Ghostscript</a>
 */
public class AutoconfAnalyzerTest extends BaseTest {

    /**
     * The analyzer to test.
     */
    private AutoconfAnalyzer analyzer;

    /**
     * Correctly setup the analyzer for testing.
     *
     * @throws Exception thrown if there is a problem
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        analyzer = new AutoconfAnalyzer();
        analyzer.initializeSettings(getSettings());
        analyzer.setFilesMatched(true);
        analyzer.initialize(null);
    }

    /**
     * Cleanup the analyzer's temp files, etc.
     *
     * @throws Exception thrown if there is a problem
     */
    @After
    @Override
    public void tearDown() throws Exception {
        analyzer.close();
        analyzer = null;
        super.tearDown();
    }

    /**
     * Test whether expected evidence is gathered from Ghostscript's configure.
     *
     * @throws AnalysisException is thrown when an exception occurs.
     */
    @Test
    public void testAnalyzeConfigureAC1() throws AnalysisException {
        final Dependency result = new Dependency(BaseTest.getResourceAsFile(
                this, "autoconf/ghostscript/configure.ac"));
        analyzer.analyze(result, null);
        //TODO fix these
        assertTrue(result.contains(EvidenceType.VENDOR, new Evidence("configure.ac", "Bug report address", "gnu-ghostscript-bug@gnu.org", Confidence.HIGH)));
        assertTrue(result.contains(EvidenceType.PRODUCT, new Evidence("configure.ac", "Package", "gnu-ghostscript", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VERSION, new Evidence("configure.ac", "Package Version", "8.62.0", Confidence.HIGHEST)));
    }

    /**
     * Test whether expected evidence is gathered from Readable's configure.ac.
     *
     * @throws AnalysisException is thrown when an exception occurs.
     */
    @Test
    public void testAnalyzeConfigureAC2() throws AnalysisException {
        final Dependency result = new Dependency(BaseTest.getResourceAsFile(
                this, "autoconf/readable-code/configure.ac"));
        analyzer.analyze(result, null);

        assertTrue(result.contains(EvidenceType.VENDOR, new Evidence("configure.ac", "Bug report address", "dwheeler@dwheeler.com", Confidence.HIGH)));
        assertTrue(result.contains(EvidenceType.PRODUCT, new Evidence("configure.ac", "Package", "readable", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VERSION, new Evidence("configure.ac", "Package Version", "1.0.7", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VENDOR, new Evidence("configure.ac", "URL", "http://readable.sourceforge.net/", Confidence.HIGH)));
    }

    /**
     * Test whether expected evidence is gathered from GNU Binutil's configure.
     *
     * @throws AnalysisException is thrown when an exception occurs.
     */
    @Test
    public void testAnalyzeConfigureScript() throws AnalysisException {
        final Dependency result = new Dependency(BaseTest.getResourceAsFile(
                this, "autoconf/binutils/configure"));
        analyzer.analyze(result, null);

        assertTrue(result.contains(EvidenceType.PRODUCT, new Evidence("configure", "NAME", "binutils", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VERSION, new Evidence("configure", "VERSION", "2.25.51", Confidence.HIGHEST)));
    }

    /**
     * Test whether expected evidence is gathered from GNU Ghostscript's
     * configure.
     *
     * @throws AnalysisException is thrown when an exception occurs.
     */
    @Test
    public void testAnalyzeReadableConfigureScript() throws AnalysisException {
        final Dependency result = new Dependency(BaseTest.getResourceAsFile(
                this, "autoconf/readable-code/configure"));
        analyzer.analyze(result, null);

        assertTrue(result.contains(EvidenceType.VENDOR, new Evidence("configure", "BUGREPORT", "dwheeler@dwheeler.com", Confidence.HIGH)));
        assertTrue(result.contains(EvidenceType.PRODUCT, new Evidence("configure", "NAME", "readable", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VERSION, new Evidence("configure", "VERSION", "1.0.7", Confidence.HIGHEST)));
        assertTrue(result.contains(EvidenceType.VENDOR, new Evidence("configure", "URL", "http://readable.sourceforge.net/", Confidence.HIGH)));
    }

    /**
     * Test of getName method, of {@link AutoconfAnalyzer}.
     */
    @Test
    public void testGetName() {
        assertEquals("Analyzer name wrong.", "Autoconf Analyzer",
                analyzer.getName());
    }

    /**
     * Test of {@link AutoconfAnalyzer#accept(File)}.
     */
    @Test
    public void testSupportsFileExtension() {
        assertTrue("Should support \"ac\" extension.",
                analyzer.accept(new File("configure.ac")));
        assertTrue("Should support \"in\" extension.",
                analyzer.accept(new File("configure.in")));
        assertTrue("Should support \"configure\" extension.",
                analyzer.accept(new File("configure")));
    }
}
