/*  FeatureSetPropagatorTest.java

    Copyright (c) 2011 Andrew Rosenberg

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
package edu.cuny.qc.speech.AuToBI.core;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import edu.cuny.qc.speech.AuToBI.AuToBI;
import edu.cuny.qc.speech.AuToBI.ResourcePath;
import edu.cuny.qc.speech.AuToBI.featureextractor.FeatureExtractorException;
import edu.cuny.qc.speech.AuToBI.io.FormattedFile;
import java.util.List;
import org.junit.Test;

/**
 * Test class for FeatureSetPropagator.
 * <p/>
 * These tests are much larger than the standard UnitTests.
 * <p/>
 * FeatureSetPropagator is responsible for reading word segmentation, and a wave file, then calling
 * the feature extraction routines to generate the required features for a FeatureSet. <p/> These
 * test confirm that the process correctly operates on each style of input segmentation, generating
 * appropriately FeatureSet objects. <p/> Specific testing of file readers and feature extractors is
 * reserved to their corresponding unit tests.
 *
 * @see edu.cuny.qc.speech.AuToBI.core.FeatureSetPropagator
 */
public class FeatureSetPropagatorTest {
  @SuppressWarnings("UnusedDeclaration")
  @Test
  public void testConstructor() {
    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile("");
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
  }

  @Test
  public void testPropagateTextGridData() {
    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile(ResourcePath.getResourcePath("test.TextGrid"));
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
    FeatureSet new_fs = fsp.call();

    assertFalse(fs == new_fs);
    assertTrue(new_fs.getDataPoints().size() > 0);
  }

  @Test
  public void testPropagateSimpleWordData() {
    AuToBI autobi = new AuToBI();
    FormattedFile file = new FormattedFile(
        ResourcePath.getResourcePath("test.txt"), FormattedFile.Format.SIMPLE_WORD);
    FeatureSet fs = new FeatureSet();

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
    FeatureSet new_fs = fsp.call();
    assertFalse(fs == new_fs);
    assertTrue(new_fs.getDataPoints().size() > 0);
  }

  public static class MockClassAttributeFE extends FeatureExtractor {
    public final static String moniker = "test_class_attribute";

    public MockClassAttributeFE() {
      this.getExtractedFeatures().add("test_class_attribute");
    }

    @Override
    public void extractFeatures(List regions) throws FeatureExtractorException {
      for (Region r : (List<Region>) regions) {
        r.setAttribute("test_class_attribute", "SAVED");
      }
    }
  }

  @Test
  public void testAggressiveFeatureEliminationSparesClassAttribute() {
    AuToBI autobi = new AuToBI();
    AuToBIParameters params = new AuToBIParameters();
    params.setParameter("aggressive_feature_elimination", "true");
    autobi.setParameters(params);
    FeatureExtractor fe = new MockClassAttributeFE();

    autobi.getMonikerMap().put("test_class_attribute", fe.getClass());
    autobi.registerFeatureExtractor(fe);

    FormattedFile file = new FormattedFile(
        ResourcePath.getResourcePath("test.txt"), FormattedFile.Format.SIMPLE_WORD);
    FeatureSet fs = new FeatureSet();

    fs.setClassAttribute("test_class_attribute");

    FeatureSetPropagator fsp = new FeatureSetPropagator(autobi, file, fs);
    FeatureSet new_fs = fsp.call();

    assertTrue(new_fs.getDataPoints().size() > 0);

    assertTrue(new_fs.getDataPoints().get(0).hasAttribute("test_class_attribute"));
  }
}
