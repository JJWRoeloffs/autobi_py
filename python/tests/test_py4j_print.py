from autobi import RunDefault
from autobi.core import AutobiJVMHandler
from autobi.core.jvm_view import _shutdown


class TestPy4jPrint:
    def test_with_run_default(self, capfd):
        with AutobiJVMHandler("test") as jvm:
            RunDefault(jvm).run("-adapter_test")

        out, err = capfd.readouterr()
        assert out.strip("\n") == "PY4J | TEST SUCCESS: The adapter is running"

    def test_repeated_startup(self, capfd):
        with AutobiJVMHandler("test") as jvm:
            RunDefault(jvm).run("-adapter_test")
            RunDefault(jvm).run("-adapter_test")

        _shutdown()

        with AutobiJVMHandler("test") as jvm:
            RunDefault(jvm).run("-adapter_test")

        _shutdown()

        with AutobiJVMHandler("test") as jvm:
            RunDefault(jvm).run("-adapter_test")

        _shutdown()

        out, err = capfd.readouterr()
        assert out.strip("\n") == "\n".join(
            ["PY4J | TEST SUCCESS: The adapter is running"] * 4
        )
