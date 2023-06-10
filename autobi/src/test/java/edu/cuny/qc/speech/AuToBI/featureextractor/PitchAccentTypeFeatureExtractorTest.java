/*  PitchAccentTypeFeatureExtractor.java

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

import static junit.framework.Assert.*;

import edu.cuny.qc.speech.AuToBI.core.Word;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PitchAccentTypeFeatureExtractor
 *
 * @see edu.cuny.qc.speech.AuToBI.featureextractor.PitchAccentTypeFeatureExtractor
 */
public class PitchAccentTypeFeatureExtractorTest {
  private PitchAccentTypeFeatureExtractor fe;
  private List<Word> words;

  @Before
  public void setUp() throws Exception {
    fe = new PitchAccentTypeFeatureExtractor();
    words = new ArrayList<>();
  }

  @Test
  public void testConstructorSetsExtractedFeaturesCorrectly() {
    assertEquals(1, fe.getExtractedFeatures().size());
    assertTrue(fe.getExtractedFeatures().contains("nominal_PitchAccentType"));
  }

  @Test
  public void testConstructorSetsRequiredFeaturesCorrectly() {
    assertEquals(0, fe.getRequiredFeatures().size());
  }

  @Test
  public void testExtractFeaturesExtractsFeatures() {
    Word w = new Word(0, 1, "word");
    w.setAccent("H*");
    words.add(w);

    fe.extractFeaturesWord(words);
    assertTrue(w.hasAttribute("nominal_PitchAccentType"));
  }

  @Test
  public void testExtractFeaturesExtractsFeaturesCorrectly() {
    Word w = new Word(0, 1, "word");
    w.setAccent("H*");
    words.add(w);

    fe.extractFeaturesWord(words);
    assertEquals("H*", w.getAttribute("nominal_PitchAccentType"));
  }

  @Test
  public void testExtractFeaturesExtractsCorrectlyWithNoPitchAccent() {
    Word w = new Word(0, 1, "word");
    words.add(w);

    fe.extractFeaturesWord(words);
    assertTrue(w.hasAttribute("nominal_PitchAccentType"));
    assertEquals("NOACCENT", w.getAttribute("nominal_PitchAccentType"));
  }
}
