from pathlib import Path
from typing import Self, List
from tempfile import TemporaryDirectory

import pandas as pd
from py4j.java_gateway import Py4JJavaError

from autobi.core import AutobiJVM, takes, to_resolved_path_str


class DatasetBuilder:
    def __init__(self, jvm: AutobiJVM, params: str):
        try:
            self._jvm = jvm
            self._object = jvm._jvm.edu.leidenuniv.AuToBIAdapter.DataSetBuilder(params)
        except Py4JJavaError:
            raise ValueError("Cannot read arguments. Could not parse Textgrid or Wav.")

    def with_default_features(self, name: str) -> Self:
        try:
            self._object.withDefaultFeatures(name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add feature {name}, not found")
        return self

    def with_feature(self, name: str) -> Self:
        try:
            self._object.withFeature(name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add feature {name}, not found")
        return self

    def with_features(self, names: List[str]) -> Self:
        _name = "\t".join(names)
        try:
            self._object.withFeatures(_name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add features {names}, not found")
        return self

    @takes(Path, converter=to_resolved_path_str)
    def write_csv(self, filename: str):
        self._object.writeCSV(filename)

    @takes(Path, converter=to_resolved_path_str)
    def write_arff(self, filename: str):
        self._object.writeARFF(filename)

    @takes(Path, converter=to_resolved_path_str)
    def write_liblinear(self, filename: str):
        self._object.writeLibLinear(filename)

    def build_pandas(self) -> pd.DataFrame:
        with TemporaryDirectory() as dir:
            tempfile = Path(dir).joinpath("out.csv")
            self.write_csv(tempfile)
            return pd.read_csv(tempfile, delimiter=",")
