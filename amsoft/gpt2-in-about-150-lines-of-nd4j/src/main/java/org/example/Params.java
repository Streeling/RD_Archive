package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import de.undercouch.bson4jackson.BsonFactory;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Json file structure is:
 * <pre>
 *   blocks : [
 *    attn : {
 *      c_attn : {
 *        b : array of doubles
 *        w : matrix of doubles
 *      }
 *      c_proj: {
 *        b : array of doubles
 *        w : matrix of doubles
 *      }
 *    }
 *    mlp : {
 *      c_proj : {
 *        b : array of doubles
 *        w : matrix of doubles
 *      }
 *      c_fc : {
 *        b : array of doubles
 *        w : matrix of doubles
 *      }
 *    }
 *    ln_1 : {
 *      b: array of doubles
 *      g: array of doubles
 *    }
 *    ln_2 : {
 *      b: array of doubles
 *      g: array of doubles
 *    }
 *  ]
 *  ln_f: {
 *    b: array of doubles
 *    g: array of doubles
 *  }
 *  wpe : matrix of doubles
 *  wte : matrix of doubles
 * </pre>
 *
 * @param blocks
 * @param lnF    Refers to Layer Normalization applied to the final layer outputs of the GPT model
 * @param wpe    Represents the positional encoding matrix used to embed position information into token representations
 * @param wte    Refers to the word token embeddings, which are learned representations of input tokens in the GPT model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Params(
    Block[] blocks,
    @JsonProperty("ln_f") LayerNormalization lnF,
    @JsonDeserialize(converter = DoubleMatrixToINDArrayConverter.class) INDArray wpe,
    @JsonDeserialize(converter = DoubleMatrixToINDArrayConverter.class) INDArray wte
) {

  /**
   * @param attn Refers to self-attention mechanism
   * @param mlp  Refers to Multi-Layer Perceptron (MLP) component
   * @param ln1  Refers to Layer Normalization (LN) layer
   * @param ln2  Refers to Layer Normalization (LN) layer
   */
  public record Block(
      Attn attn,
      Mlp mlp,
      @JsonProperty("ln_1") LayerNormalization ln1,
      @JsonProperty("ln_2") LayerNormalization ln2
  ) {
  }

  /**
   * @param cAttn Refers to contextual attention
   * @param cProj Refers to linear projection layer
   */
  public record Attn(
      @JsonProperty("c_attn") BiasAndWeight cAttn,
      @JsonProperty("c_proj") BiasAndWeight cProj
  ) {
  }

  /**
   * @param cProj Refers to linear projection layer
   * @param cFc   Refers to Feed-Forward Network (FFN) layer
   */
  public record Mlp(
      @JsonProperty("c_proj") BiasAndWeight cProj,
      @JsonProperty("c_fc") BiasAndWeight cFc
  ) {
  }

  /**
   * @param b Refers to bias vector
   * @param w refers to weight matrix
   */
  public record BiasAndWeight(
      @JsonDeserialize(converter = DoubleArrayToINDArrayConverter.class) INDArray b,
      @JsonDeserialize(converter = DoubleMatrixToINDArrayConverter.class) INDArray w
  ) {
  }

  /**
   * @param b Refers to the bias vector, is a learnable parameter used in the layer normalization process
   * @param g Referred to as the gain vector, is another learnable parameter used in layer normalization
   */
  public record LayerNormalization(
      @JsonDeserialize(converter = DoubleArrayToINDArrayConverter.class) INDArray b,
      @JsonDeserialize(converter = DoubleArrayToINDArrayConverter.class) INDArray g
  ) {
  }

  static Params getParams(String modelDir) throws IOException {
//    return Params.getParams(modelDir, "params.json");
    return Params.getParams(modelDir, "params.bson");
  }

  static Params getParams(String modelDir, String paramsFileName) throws IOException {
//    ObjectMapper mapper = new ObjectMapper();
    ObjectMapper mapper = new ObjectMapper(new BsonFactory());;
    File paramsFile = Paths.get(modelDir, paramsFileName).toFile();
    long fileSize = paramsFile.length();

    // Progress callback to update progress
    Consumer<Long> progressCallback = bytesRead -> {
//      Utils.updateProgress("loading params.json", bytesRead, fileSize);
      Utils.updateProgress("loading params.bson", bytesRead, fileSize);
    };

    Params params = null;
    // Open input stream with progress tracking
    try (InputStream inputStream = new Utils.ProgressTrackingInputStream(new FileInputStream(paramsFile), fileSize, progressCallback)) {
      // Read JSON data using ObjectMapper
      params = mapper.readValue(inputStream, Params.class);
    }

    return params;
  }

  public static class DoubleMatrixToINDArrayConverter extends StdConverter<double[][], INDArray> {

    @Override
    public INDArray convert(double[][] value) {
      return Nd4j.create(value);
    }
  }

  public static class DoubleArrayToINDArrayConverter extends StdConverter<double[], INDArray> {

    @Override
    public INDArray convert(double[] value) {
      return Nd4j.create(value);
    }
  }
}