from __future__ import annotations

from typing import List

from py4j.java_gateway import Py4JJavaError
from autobi.core import AutobiJVM


class FeaturenamesBuilder:
    def __init__(self, jvm: AutobiJVM):
        self._jvm = jvm
        self._object = jvm._jvm.edu.leidenuniv.AuToBIAdapter.FeatureNamesBuilder()

    def with_default_features(self, name: str) -> FeaturenamesBuilder:
        try:
            self._object.withDefaultFeatures(name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add feature {name}, not found")
        return self

    def with_feature(self, name: str) -> FeaturenamesBuilder:
        try:
            self._object.withFeature(name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add feature {name}, not found")
        return self

    def with_features(self, names: List[str]) -> FeaturenamesBuilder:
        _name = "\t".join(names)
        try:
            self._object.withFeatures(_name)
        except Py4JJavaError:
            raise ValueError(f"Cannot add features {names}, not found")
        return self

    def build(self) -> List[str]:
        return str(self._object.build()).split("\t")
