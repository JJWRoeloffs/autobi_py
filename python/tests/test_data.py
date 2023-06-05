from __future__ import annotations

from pathlib import Path

from py4j.java_gateway import JavaGateway

from autobi.jar import JARPATH


class TestJar:
    def test_jar_includes(self):
        assert isinstance(JARPATH, Path)
        assert JARPATH.exists()
        assert JARPATH.is_file()
        assert JARPATH.suffix == ".jar"
        assert JARPATH.is_absolute()
        assert not JARPATH.is_reserved()

    def test_jar_loadable(self):
        gg = JavaGateway.launch_gateway(classpath=str(JARPATH))
        autobi = gg.jvm.edu.cuny.qc.speech.AuToBI.AuToBI()
        assert "toString" in dir(autobi)
