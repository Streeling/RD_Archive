package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests {@link Params} for 124M model size.
 */
class ParamsTest {

  private static Params params;

  @BeforeAll
  static void initAll() throws IOException {
    params = Params.getParams(Paths.get("models", "124M"));
  }

  @Test
  void blocks() {
    assertThat(params.blocks(), is(arrayWithSize(12)));
  }

  @Test
  void attnCAttn() {
    Params.BiasAndWeight cAttn = params.blocks()[0].attn().cAttn();

    assertThat(cAttn.b().size(0), is(2304L));
    assertThat(cAttn.w().shape(), is(new long[]{768L, 2304L}));
  }

  @Test
  void attnCProj() {
    Params.BiasAndWeight cProj = params.blocks()[0].attn().cProj();

    assertThat(cProj.b().size(0), is(768L));
    assertThat(cProj.w().shape(), is(new long[] {768L, 768L}));
  }

  @Test
  void blocksMlpCFc() {
    Params.BiasAndWeight cFc = params.blocks()[0].mlp().cFc();

    assertThat(cFc.b().size(0), is(3072L));
    assertThat(cFc.w().shape(), is(new long[] {768L, 3072L}));
  }

  @Test
  void blocksMlpCProj() {
    Params.BiasAndWeight cProj = params.blocks()[0].mlp().cProj();

    assertThat(cProj.b().size(0), is(768L));
    assertThat(cProj.w().shape(), is(new long[] {3072L, 768L}));
  }

  @Test
  void blocksLn1() {
    Params.LayerNormalization ln1 = params.blocks()[0].ln1();

    assertThat(ln1.b().size(0), is(768L));
    assertThat(ln1.g().size(0), is(768L));
  }

  @Test
  void blocksLn2() {
    Params.LayerNormalization ln2 = params.blocks()[0].ln2();

    assertThat(ln2.b().size(0), is(768L));
    assertThat(ln2.g().size(0), is(768L));
  }

  @Test
  void getLnF() {
    Params.LayerNormalization lnF = params.lnF();

    assertThat(lnF.b().size(0), is(768L));
    assertThat(lnF.g().size(0), is(768L));
  }

  @Test
  void getWpe() {
    INDArray wpe = params.wpe();

    assertThat(wpe.shape(), is(new long[]{1024L, 768L}));
  }

  @Test
  void getWte() {
    INDArray wte = params.wte();

    assertThat(wte.shape(), is(new long[]{50257L, 768L}));
  }
}