package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Unit tests {@link Encoder}.
 */
class EncoderTest {

  private Encoder encoder;

  @BeforeEach
  void init() throws IOException {
    assumeTrue("UTF-8".equals(System.getProperty("file.encoding")));

    String modelSize = "124M";
    String modelsDir = "models";

    encoder = Encoder.getEncoder(modelSize, modelsDir);
  }

  @Test
  void bytesToUnicode() {
    Map<Integer, String> byteEncoder = Encoder.bytesToUnicode();

    // Test specific mappings for known byte values and their expected Unicode representations
    // Add more test cases as needed
    assertThat(byteEncoder.get((int) ' '), is("Ġ"));

    assertThat(byteEncoder.get((int) 'A'), is("A"));
    assertThat(byteEncoder.get((int) 'B'), is("B"));
    assertThat(byteEncoder.get((int) 'Z'), is("Z"));

    assertThat(byteEncoder.get((int) '!'), is("!"));
    assertThat(byteEncoder.get((int) '~'), is("~"));
    assertThat(byteEncoder.get((int) '¡'), is("¡"));
    assertThat(byteEncoder.get((int) '®'), is("®"));
    assertThat(byteEncoder.get((int) 'ÿ'), is("ÿ"));

    // Test a few boundary cases and unknown byte values
    assertThat(byteEncoder.get(-1), is(nullValue()));
    assertThat(byteEncoder.get(256), is(nullValue()));
  }

  @Test
  void getPairs_ofString() {
    assertThat(Encoder.getPairs("word"), contains(List.of("w", "o"), List.of("o", "r"), List.of("r", "d")));
  }

  @Test
  void getPairs_ofListOfStrings() {
    assertThat(Encoder.getPairs(List.of("w", "o", "rd")), contains(List.of("w", "o"), List.of("o", "rd")));
  }

  @Test
  void bpe() {
    assertEquals("ĠAll", encoder.bpe("ĠAll"), "Wrong bpe");
  }

  @Test
  void encode() {
    assertThat(encoder.encode("Not all heroes wear capes."), contains(3673, 477, 10281, 5806, 1451, 274, 13));
  }

  @Test
  void decode() {
    List<Integer> inputIds = encoder.encode("Not all heroes wear capes.");

    assertThat(encoder.decode(inputIds), is("Not all heroes wear capes."));
  }
}