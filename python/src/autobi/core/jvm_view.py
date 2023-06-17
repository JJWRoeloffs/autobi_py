import subprocess
import gc

from collections import defaultdict
from dataclasses import dataclass

from py4j.java_gateway import JavaGateway, JVMView

from typing import Optional

from autobi._jar import JARPATH
from .not_instantiable import NotInstantiable


class JVMNotRunning(Exception):
    pass


class _JVM:
    """
    The underlying JVM management Singleton

    This class performs the management of the JVM and its views behind the scenes,
    allowing for py4j to be interacted with with pythonic __enter__ and __exit__ methods
    It contains a *lot* of ugly C-style resource management. Sadly, this is required

    This class is private, and not designed to be interacted with by users.
    """

    @dataclass
    class AutobiJVM:
        _jvm: JVMView

    # It seems like this can be made cleaner with one dataclass that contains all three,
    # saving an optional instance of that dataclass.
    # However, because of the reference counting, this creates only more chaos
    gateway: Optional[JavaGateway] = None
    views: Optional[defaultdict[str, AutobiJVM]] = None
    ref_count: Optional[defaultdict[str, int]] = None

    @classmethod
    def is_in_valid_state(cls) -> bool:
        if not (cls.gateway is None) == (cls.views is None) == (cls.ref_count is None):
            return False

        if cls.views is not None and cls.ref_count is not None:
            if not set(cls.views.keys()) == set(cls.ref_count.keys()):
                print(cls.ref_count.keys(), cls.views.keys())
                return False

        return True

    @classmethod
    def is_running(cls) -> bool:
        assert cls.is_in_valid_state()
        return cls.views is not None

    @classmethod
    def get_view(cls, name: str = "default") -> AutobiJVM:
        if not cls.is_running():
            cls.gateway = JavaGateway.launch_gateway(
                classpath=str(JARPATH),
                redirect_stdout=subprocess.STDOUT,
                redirect_stderr=subprocess.PIPE,
            )
            cls.views = defaultdict(lambda: cls.AutobiJVM(cls.gateway.new_jvm_view()))
            cls.ref_count = defaultdict(lambda: 0)

        cls.ref_count[name] += 1
        result = cls.views[name]
        assert cls.is_in_valid_state()
        return result

    @classmethod
    def delete_view(cls, name: str = "default") -> None:
        if not cls.is_running():
            raise JVMNotRunning("Cannot delete view: JVM is not running")

        if not ((name in cls.ref_count.keys()) and (name in cls.views.keys())):
            raise ValueError(f"Cannot find view: {name}")

        cls.ref_count[name] -= 1
        if cls.ref_count[name] <= 0:
            del cls.views[name]
            del cls.ref_count[name]

        assert cls.is_in_valid_state()

        if not cls.views.keys():
            cls.shutdown()

    @classmethod
    def shutdown(cls) -> None:
        if cls.views is not None:
            cls.views.clear()
            del cls.views
        if cls.ref_count is not None:
            cls.ref_count.clear()
            del cls.ref_count
        if cls.gateway is not None:
            cls.gateway.shutdown()
            del cls.gateway
            # gc.collect is recommended to be called by the py4j docs after shutdown.
            gc.collect()
        cls.gateway = None
        cls.views = None
        cls.ref_count = None


class AutobiJVMHandler:
    """
    The handler that creates AutobiJVM instances with __enter__ and __exit__ methods

    Create a AutobiJVM instance in the following manner:
    with AutobiJVMHandler() as jvm:
        pass
    """

    def __init__(self, name: str) -> None:
        self.handler: Optional[_JVM.AutobiJVM] = None
        self.name = name

    def __enter__(self) -> _JVM.AutobiJVM:
        self.handler = _JVM.get_view(self.name)
        return self.handler

    def __exit__(self, exc_type, exc_value, traceback) -> None:
        try:
            _JVM.delete_view(self.name)
        except JVMNotRunning:
            # This means _shutdown was called somewhere halfway
            pass


def _shutdown():
    """The public shutdown function of the JVM.
    It is kept seperate from _JVM to allow it to be registered with atexit
    """
    _JVM.shutdown()


class AutobiJVM:
    _jvm: JVMView

    def __new__(cls):
        raise NotInstantiable(
            "AutobiJVM object can only be created by the AutobiJVMHandler"
        )


# Disallow usage of _JVM
__all__ = ("AutobiJVMHandler", "AutobiJVM", "_shutdown")

# Ensure that the JVM will always be shut down when the application is closed
import atexit

atexit.register(_shutdown)
