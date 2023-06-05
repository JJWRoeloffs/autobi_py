/*  PitchAccentFeatureExtractor.java

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

import edu.cuny.qc.speech.AuToBI.core.Region;
import edu.cuny.qc.speech.AuToBI.core.Word;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PitchAccentFeatureExtractor
 *
 * @see PitchAccentFeatureExtractor
 */
public class PitchAccentFeatureExtractorTest {
  private PitchAccentFeatureExtractor fe;
  private List<Region> regions;

  @Before
  public void setUp() throws Exception {
    fe = new PitchAccentFeatureExtractor();
    regions = new ArrayList<Region>();
  }

  @Test
  public void testConstructorSetsExtractedFeaturesCorrectly() {
    assertEquals(1, fe.getExtractedFeatures().size());
    assertTrue(fe.getExtractedFeatures().contains("nominal_PitchAccent"));
  }

  @Test
  public void testConstructorSetsRequiredFeaturesCorrectly() {
    assertEquals(0, fe.getRequiredFeatures().size());
  }

  @Test
  public void testExtractFeaturesExtractsFeatures() {
    Word w = new Word(0, 1, "word");
    w.setAccent("H*");
    regions.add(w);

    try {
      fe.extractFeatures(regions);
      assertTrue(w.hasAttribute("nominal_PitchAccent"));
    } catch (FeatureExtractorException e) {
      fail();
    }
  }

  @Test
  public void testExtractFeaturesExtractsFeaturesCorrectly() {
    Word w = new Word(0, 1, "word");
    w.setAccent("H*");
    regions.add(w);

    try {
      fe.extractFeatures(regions);
      assertEquals("ACCENTED", w.getAttribute("nominal_PitchAccent"));
    } catch (FeatureExtractorException e) {
      fail();
    }
  }

  @Test
  public void testExtractFeaturesExtractsCorrectlyWithNoPitchAccent() {
    Word w = new Word(0, 1, "word");
    regions.add(w);

    try {
      fe.extractFeatures(regions);
      assertTrue(w.hasAttribute("nominal_PitchAccent"));
      assertEquals("DEACCENTED", w.getAttribute("nominal_PitchAccent"));
    } catch (FeatureExtractorException e) {
      fail();
    }
  }
}
