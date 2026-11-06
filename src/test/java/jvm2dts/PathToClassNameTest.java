package jvm2dts;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PathToClassNameTest {

  private static final Method TO_CLASS_NAME = toClassNameMethod();

  @Test
  void convertsUnixStylePath() throws Exception {
    assertThat(convert("build/classes/jvm2dts/Model.class", "build/classes")).isEqualTo("jvm2dts.Model");
  }

  @Test
  void convertsWindowsStylePath() throws Exception {
    assertThat(convert("build\\classes\\jvm2dts\\Model.class", "build\\classes")).isEqualTo("jvm2dts.Model");
  }

  @Test
  void convertsNestedWindowsStylePath() throws Exception {
    assertThat(convert("build\\classes\\com\\example\\Model.class", "build\\classes")).isEqualTo("com.example.Model");
  }

  private static String convert(String path, String basePath) throws Exception {
    return (String) TO_CLASS_NAME.invoke(null, path(path), path(basePath));
  }

  private static Path path(String value) {
    return (Path) Proxy.newProxyInstance(
        PathToClassNameTest.class.getClassLoader(),
        new Class<?>[]{Path.class},
        (proxy, method, args) -> {
          if (method.getName().equals("toString")) return value;
          throw new UnsupportedOperationException(method.getName());
        });
  }

  private static Method toClassNameMethod() {
    try {
      var method = Main.class.getDeclaredMethod("toClassName", Path.class, Path.class);
      method.setAccessible(true);
      return method;
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }
}
