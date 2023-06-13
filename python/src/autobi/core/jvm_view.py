from collections import defaultdict
from dataclasses import dataclass

from py4j.java_gateway import JavaGateway, JVMView

from typing import Optional

from autobi._jar import JARPATH


class _JVM:
    """
    The underlying JVM management

    This class performs the management of the JVM and its views behind the scenes,
    allowing for py4j to be interacted with with pythonic __enter__ and __exit__ methods

    This class is private, and not designed to be interacted with by users.
    """

    @dataclass
    class AutobiJVM:
        _jvm: JVMView

    gateway: Optional[JavaGateway] = None
    views: Optional[defaultdict[str, AutobiJVM]] = None

    @classmethod
    def get_view(cls, name: str = "default") -> AutobiJVM:
        if cls.gateway is None or cls.views is None:
            cls.gateway = JavaGateway.launch_gateway(classpath=str(JARPATH))
            cls.views = defaultdict(lambda: cls.AutobiJVM(cls.gateway.new_jvm_view()))

        return cls.views[name]

    @classmethod
    def delete_view(cls, name: str = "default") -> None:
        if cls.gateway is None or cls.views is None:
            return

        del cls.views[name]

        if not cls.views:
            cls.gateway.shutdown()
            cls.gateway = None
            cls.views = None

    @classmethod
    def shutdown(cls) -> None:
        if cls.gateway is not None:
            cls.gateway.shutdown()
            del cls.gateway
        if cls.views is not None:
            cls.views.clear()
        cls.gateway = None
        cls.views = None

    def __del__(self):
        self.__class__.shutdown()


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
        _JVM.delete_view(self.name)


def _shutdown():
    _JVM.shutdown()


# Prevent users from instantiating an instance of this class themselves.
# However, because of the .pyi file, AutobiJVM will still be recognised as _JVM.AutobiJVM
#    by the python interpreter and any type checker
AutobiJVM = None

# Disallow usage of _JVM
__all__ = ("AutobiJVMHandler", "AutobiJVM")

# Ensure that the JVM will always be shut down when the application is closed
import atexit

atexit.register(_shutdown)
