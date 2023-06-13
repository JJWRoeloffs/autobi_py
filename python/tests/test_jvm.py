from py4j.java_gateway import JavaGateway, JVMView
import pytest

from autobi.core import AutobiJVM, AutobiJVMHandler


class TestJVMTyping:
    def test_function_signature_possible(self):
        def hello(something: AutobiJVM) -> None:
            pass

    def test_instance_not_instantiatable(self):
        with pytest.raises(TypeError):
            AutobiJVM()


class TestJVMManagment:
    def test_name_management_same(self):
        with AutobiJVMHandler("test") as jvm1:
            with AutobiJVMHandler("test") as jvm2:
                assert jvm1 is jvm2

    def test_name_management_different(self):
        with AutobiJVMHandler("test1") as jvm1:
            with AutobiJVMHandler("test2") as jvm2:
                assert jvm1 is not jvm2

    def test_cleanup(self):
        # Code smell: We are doing soem reaching beneath the scenes to test this.
        from autobi.core.jvm_view import _JVM

        assert _JVM.views == None
        assert _JVM.gateway == None

        with AutobiJVMHandler("test1") as jvm1:
            assert "test1" in _JVM.views
            assert not "test2" in _JVM.views
            assert isinstance(_JVM.gateway, JavaGateway)
            assert isinstance(jvm1._jvm, JVMView)
            with AutobiJVMHandler("test2") as jvm2:
                assert "test1" in _JVM.views
                assert "test2" in _JVM.views
                assert isinstance(_JVM.gateway, JavaGateway)
                assert isinstance(jvm1._jvm, JVMView)
                assert isinstance(jvm2._jvm, JVMView)
            assert "test1" in _JVM.views
            assert not "test2" in _JVM.views
            assert isinstance(_JVM.gateway, JavaGateway)
            assert isinstance(jvm1._jvm, JVMView)

        assert _JVM.views == None
        assert _JVM.gateway == None

    def test_hard_shutdown(self):
        # The JVMView items will linger, and re-start the JavaGateway if used.
        # This simply asserts that a shutdown removes all references from this singleton
        # As far as I can find, there is not function to assert the gateway was actually shut down

        from autobi.core.jvm_view import _shutdown
        from autobi.core.jvm_view import _JVM

        with AutobiJVMHandler("test1") as jvm1:
            assert "test1" in _JVM.views
            _shutdown()
            assert _JVM.views == None
            assert _JVM.gateway == None
