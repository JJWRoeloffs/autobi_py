import java.nio.file.*

object BuildPython {
  def getDestenationPathAndWritePython(
    pythonSrc: String,
    assemblyJarName: String,
  ): String = {
    val jarPath    = FileSystems
      .getDefault()
      .getPath("", pythonSrc.split("/").toList: _*)
      .resolve("jar")
    val binaryPath = jarPath.resolve(assemblyJarName)
    // If we don't do this, the assembly plugin will swear at us.
    Files.deleteIfExists(binaryPath)

    val pythonFile = jarPath.resolve("__init__.py")
    val pythonCode = s"""
      |from pathlib import Path
      |
      |JARPATH = Path(__file__).parent.joinpath("$assemblyJarName").resolve()
      """.stripMargin('|').trim()
    Files.writeString(
      pythonFile,
      pythonCode,
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING,
    )

    binaryPath.toAbsolutePath().toString()
  }
}
