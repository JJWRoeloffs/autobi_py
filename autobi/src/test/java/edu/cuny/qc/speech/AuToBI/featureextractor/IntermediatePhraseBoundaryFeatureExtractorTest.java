/*  IntermediatePhraseBoundaryFeatureExtractorTest.java

    Copyright 2012 Andrew Rosenberg

    This file is part of the AuToBI prosodic analysis package.

    AuToBI is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AuToBI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AuToBI.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cuny.qc.speech.AuToBI.featureextractor;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.cuny.qc.speech.AuToBI.core.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Test class for IntermediatePhraseBoundaryFeatureExtractor
 *
 * @see edu.cuny.qc.speech.AuToBI.featureextractor.IntermediatePhraseBoundaryFeatureExtractor
 */
public class IntermediatePhraseBoundaryFeatureExtractorTest {
  @Test
  public void testConstructorSetsExtractedFeaturesCorrectly() {
    IntermediatePhraseBoundaryFeatureExtractor fe =
        new IntermediatePhraseBoundaryFeatureExtractor();

    assertEquals(1, fe.getExtractedFeatures().size());
    assertTrue(fe.getExtractedFeatures().contains("nominal_IntermediatePhraseBoundary"));
  }

  @Test
  public void testConstructorSetsRequiredFeaturesCorrectly() {
    IntermediatePhraseBoundaryFeatureExtractor fe =
        new IntermediatePhraseBoundaryFeatureExtractor();

    assertEquals(0, fe.getRequiredFeatures().size());
  }

  @Test
  public void testExtractFeaturesExtractsFeatures() {
    IntermediatePhraseBoundaryFeatureExtractor fe =
        new IntermediatePhraseBoundaryFeatureExtractor();

    List<Word> words = new ArrayList<>();
    Word w = new Word(0.0, 1.0, "test");
    w.setPhraseAccent("L-");
    w.setBreakAfter("3");
    words.add(w);

    fe.extractFeaturesWord(words);
    assertTrue(w.hasAttribute("nominal_IntermediatePhraseBoundary"));
  }

  @Test
  public void testExtractFeaturesExtractsFeaturesCorrectly() {
    IntermediatePhraseBoundaryFeatureExtractor fe =
        new IntermediatePhraseBoundaryFeatureExtractor();

    List<Word> words = new ArrayList<>();
    Word w = new Word(0.0, 1.0, "test");
    w.setPhraseAccent("L-");
    w.setBreakAfter("3");
    words.add(w);

    fe.extractFeaturesWord(words);
    assertEquals("INTERMEDIATE_BOUNDARY", w.getAttribute("nominal_IntermediatePhraseBoundary"));
  }
}
