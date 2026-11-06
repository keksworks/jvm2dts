package jvm2dts.e2e;

import jvm2dts.Main;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class MainE2ETest {

  @Test
  void generatesTypesForCompiledClass() throws Exception {
    var classesDir = Path.of(E2eModel.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .toURI());

    var originalOut = System.out;
    var originalErr = System.err;
    var output = new ByteArrayOutputStream();
    var errors = new ByteArrayOutputStream();

    try {
      System.setOut(new PrintStream(output, true, UTF_8));
      System.setErr(new PrintStream(errors, true, UTF_8));

      Main.main(new String[]{"-classesDir", classesDir.toString(), "-exclude", ".*MainE2ETest", "jvm2dts.e2e"});
    } finally {
      System.setOut(originalOut);
      System.setErr(originalErr);
    }

    assertThat(output.toString(UTF_8))
        .contains("// jvm2dts.e2e.E2eModel")
        .contains("export interface E2eModel {")
        .contains("id: string;");
  }
}
