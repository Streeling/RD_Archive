package org.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.ops.NDBase;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class Nd4j2Test {
  @Test
  void mean() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

    // Calculate the mean along the last axis (axis=-1, which is axis=1 for a 2D array)
    INDArray meanAlongRows = x.mean(true, -1);

    assertThat(meanAlongRows.shape(), Matchers.is(new long[] {2, 1}));
    assertThat(meanAlongRows.toDoubleMatrix(), Matchers.is(new double[][] {{2.0}, {5.0}}));
  }

  @Test
  void var() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

    // Calculate the variance along the last axis (axis=-1)
    INDArray varAlongLastAxis = (new NDBase()).variance(x, false, true, -1);

    assertThat(varAlongLastAxis.shape(), Matchers.is(new long[] {2, 1}));
    assertThat(varAlongLastAxis.toDoubleMatrix(), Matchers.is(new double[][] {{.6666666666666666}, {.6666666666666666}}));
  }

  @Test
  void sqrt() {
    INDArray x = Nd4j.create(new double[][] {{4, 9},
                                             {16, 25}});

    // Compute the square root of the matrix element-wise
    INDArray sqrtMatrix = Nd4j.math().sqrt(x);

    assertThat(sqrtMatrix.toDoubleMatrix(), Matchers.is(new double[][] {{2., 3.}, {4., 5.}}));
  }

  @Test
  void split() {
    INDArray x = Nd4j.create(new double[][]{{1, 2, 3, 4, 5, 6},
                                            {7, 8, 9, 10, 11, 12},
                                            {13, 14, 15, 16, 17, 18}});

    // Split the matrix x into 3 equally sized sub-matrices along the last axis (axis=-1)
    INDArray[] subMatrices = (new NDBase()).split(x, 3, -1);

    assertThat(subMatrices.length, Matchers.is(3));
    assertThat(subMatrices[0].toDoubleMatrix(), Matchers.is(new double[][] {{1, 2}, {7, 8}, {13, 14}}));
    assertThat(subMatrices[1].toDoubleMatrix(), Matchers.is(new double[][] {{3, 4}, {9, 10}, {15, 16}}));
    assertThat(subMatrices[2].toDoubleMatrix(), Matchers.is(new double[][] {{5, 6}, {11, 12}, {17, 18}}));
  }

  @Test
  void tri() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6},
                                             {7, 8, 9}});

    long n = x.shape()[0];
    INDArray lowerTriangular = Nd4j.ones(x.dataType(), n, n);
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        lowerTriangular.putScalar(i, j, 0);  // Set upper triangular elements to zero
      }
    }

    assertThat(lowerTriangular.toDoubleMatrix(), Matchers.is(new double[][] {{1, 0, 0}, {1, 1, 0}, {1, 1, 1}}));
  }

  @Test
  void max() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

    // Compute the maximum value along the last axis (axis=-1)
    INDArray maxValues = x.max(true, -1);

    assertThat(maxValues.toDoubleMatrix(), Matchers.is(new double[][] {{3}, {6}}));
  }

  @Test
  void exp() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

    // Compute the maximum value along the last axis (axis=-1)
    INDArray maxValues = Nd4j.math().exp(x);

    assertThat(maxValues.toDoubleMatrix(), Matchers.is(new double[][] {{2.718281828459045, 7.38905609893065, 20.085536923187668},
                                                                       {54.59815003314424, 148.4131591025766, 403.4287934927351}}));
  }

  @Test
  void shape_minus1() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

    assertThat(x.shape()[x.shape().length - 1], Matchers.is(3L));
  }

  @Test
  void hstack() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {6, 7, 8}});
    INDArray y = Nd4j.create(new double[][] {{4, 5},
                                             {9, 10}});


    INDArray result = Nd4j.hstack(x, y); // equiv Nd4j.concat(-1, x, y)

    assertThat(result.toDoubleMatrix(), Matchers.is(new double[][] {{1, 2, 3, 4, 5},
                                                                    {6, 7, 8, 9, 10}}));
  }


  @Test
  void argmax() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6},
                                             {9, 8, 7}});

    INDArray maxIndex = x.slice(x.size(0) - 1).argMax(-1);

    assertThat(maxIndex.shape(), Matchers.is(new long[] {}));
    assertThat(maxIndex.getDouble(0), Matchers.is(0.0));
  }

  @Test
  void pow() {
    INDArray x = Nd4j.create(new double[][] {{1, 2, 3},
                                             {4, 5, 6}});

//    INDArray pow = Nd4j.math.pow(x, 3);
    INDArray pow = Transforms.pow(x, 3, true);

    assertThat(pow.toDoubleMatrix(), Matchers.is(new double[][] {{1, 8, 27}, {64, 125, 216}}));
    assertThat(x.toDoubleMatrix(), Matchers.is(new double[][] {{1, 2, 3}, {4, 5, 6}}));

  }

  /**
   * https://www.offconvex.org/2015/12/12/word-embeddings-1/
   * https://www.offconvex.org/2016/02/14/word-embeddings-2/
   * @throws IOException
   */
  @Test
  void similarity() throws IOException {
    String modelSize = "124M";
    String modelsDir = "models";
    assumeTrue("UTF-8".equals(System.getProperty("file.encoding")));
    Params params = Params.getParams(Paths.get(modelsDir, modelSize).toString(), "params_.json");
    Encoder encoder = Encoder.getEncoder(modelSize, modelsDir);

    List<Integer> tokens = encoder.encode("Java JavaScript chemistry hot cold name John woman");
    INDArray java = params.wte().getRow(tokens.get(0));
    INDArray javaScript = params.wte().getRow(tokens.get(1));
    INDArray chemistry = params.wte().getRow(tokens.get(2));

    // java is more similar to javascript (closer to 1), than is to chemistry
    assertThat(1 - Transforms.cosineSim(java, javaScript), is(lessThan(Transforms.cosineSim(java, chemistry))));
  }
}
