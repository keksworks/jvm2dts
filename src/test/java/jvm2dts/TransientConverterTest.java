package jvm2dts;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class TransientConverterTest {
    
  private final Converter converter = new Converter(new TypeMapper(emptyMap()), true);
 
  @Test
  void convertDoesNotSkipTransientField() {
    var converter = new Converter(new TypeMapper(emptyMap()), false);
    assertThat(converter.convert(Box.class)).isEqualTo("interface Box {id: number; secret: string;}");
  }

  @Test
  void convertSkipsTransientField() {
    var converter = new Converter(new TypeMapper(emptyMap()), true);
    assertThat(converter.convert(Box.class)).isEqualTo("interface Box {id: number;}");
  }
}

class Box {
  long id;
  transient String secret;

  public long getId() {
    return id;
  }

  public String getSecret() {
      return secret;
  }
}
