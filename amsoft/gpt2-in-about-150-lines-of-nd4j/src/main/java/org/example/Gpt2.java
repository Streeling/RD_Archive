package org.example;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nd4j.common.util.ArrayUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.ops.NDBase;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.Utils.loadHparamsJson;
import static org.example.Utils.updateProgress;

/**
 * Copied from https://github.com/jaymody/picoGPT/blob/main/gpt2.py.
 */
public class Gpt2 {
  INDArray gelu(INDArray x) {
    INDArray tanhArgument = Nd4j.scalar(Math.sqrt(2 / Math.PI)).mul(x.add(Nd4j.scalar(0.044715).mul(Transforms.pow(x, 3, true))));
    return Nd4j.scalar(0.5).mul(x).mul(Nd4j.scalar(1.0).add(Nd4j.math.tanh(tanhArgument)));
  }

  INDArray softmax(INDArray x) {
    INDArray expX = Nd4j.math.exp(x.sub(x.max(true, -1)));
    return expX.div(expX.sum(true, -1));
  }

  INDArray layerNorm(INDArray x, INDArray g, INDArray b) {
    double eps = 1e-5;
    INDArray mean = x.mean(true, -1);
    INDArray variance = (new NDBase()).variance(x, false, true, -1); // false for population variance
    return g.mul(x.sub(mean)).div(Nd4j.math().sqrt(variance.add(eps))).add(b);
  }

  INDArray linear(INDArray x, INDArray w, INDArray b) {
    return x.mmul(w).add(b);
  }

  INDArray ffn(INDArray x, Params.BiasAndWeight cFc, Params.BiasAndWeight cProj) {
    // Project up
    x = gelu(linear(x, cFc.w(), cFc.b()));
    // Project back down
    return linear(x, cProj.w(), cProj.b());
  }

  INDArray attention(INDArray q, INDArray k, INDArray v, INDArray mask) {
    return softmax(q.mmul(k.transpose()).div(Nd4j.scalar(Math.sqrt(q.shape()[q.shape().length - 1]))).add(mask)).mmul(v);
  }

  INDArray mha(INDArray x, Params.BiasAndWeight cAttn, Params.BiasAndWeight cProj, int nHead) {
    // QKV projection
    x = linear(x, cAttn.w(), cAttn.b());
    NDBase nd4jBase = new NDBase();
    // Split into QKV, then heads
    List<INDArray[]> qkvHeads = Arrays.stream(nd4jBase.split(x, 3, -1))
        .map(subMatrix -> nd4jBase.split(subMatrix, nHead, -1))
        .toList();
    assert qkvHeads.size() == 3 : "something wrong";
    long n = x.shape()[0];
    // Causal mask to hide future inputs from being attended to
    INDArray lowerTriangular = Nd4j.ones(x.dataType(), n, n);
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        lowerTriangular.putScalar(i, j, 0);  // Set upper triangular elements to zero
      }
    }
    INDArray causalMask = Nd4j.scalar(1.0).sub(lowerTriangular).mul(-1e10);

    // Perform attention over each head
    INDArray[] outHeads = new INDArray[nHead];
    for (int i = 0; i < nHead; i++) {
      outHeads[i] = attention(qkvHeads.get(0)[i], qkvHeads.get(1)[i], qkvHeads.get(2)[i], causalMask);
    }

    // Merge heads and out projection
    x = linear(Nd4j.hstack(outHeads), cProj.w(), cProj.b());
    return x;
  }

  INDArray transformerBlock(INDArray x, Params.Mlp mlp, Params.Attn attn, Params.LayerNormalization ln1, Params.LayerNormalization ln2, int nHead) {
    // Multi-head causal self attention
    x = x.add(mha(layerNorm(x, ln1.g(), ln1.b()), attn.cAttn(), attn.cProj(), nHead));

    // Position-wise feed forward network
    x = x.add(ffn(layerNorm(x, ln2.g(), ln2.b()), mlp.cFc(), mlp.cProj()));
    return x;
  }

  INDArray gpt2(int[] inputs, INDArray wte, INDArray wpe, Params.Block[] blocks, Params.LayerNormalization lnF, int nHead) {
    // Token + positional embeddings
    INDArray x = wte.getRows(inputs).add(wpe.getRows(ArrayUtil.range(0, inputs.length)));

    // Forward pass through n_layer transformer blocks
    for(int i = 0; i<blocks.length; i++) {
      x = transformerBlock(x, blocks[i].mlp(), blocks[i].attn(), blocks[i].ln1(), blocks[i].ln2(), nHead);
    }

    // Projection to vocab
    return layerNorm(x, lnF.g(), lnF.b()).mmul(wte.transpose());
  }

  List<Integer> generate(List<Integer> inputs, Params params, int nHead, int nTokensToGenerate) throws IOException {
    List<Integer> inputIds = new ArrayList<>(inputs);

    // Auto-regressive decode loop
    for (int i = 0; i < nTokensToGenerate ; i++) {
      updateProgress("generating", i, nTokensToGenerate);
      // Model forward pass
      INDArray logits = gpt2(inputIds.stream().mapToInt(Integer::intValue).toArray(), params.wte(), params.wpe(), params.blocks(), params.lnF(), nHead);
      // Greedy sampling
      int nextId = logits.slice(logits.size(0) - 1).argMax(-1).getInt(0);
      // Append prediction to input
      inputIds.add(nextId);
    }

    // Only return generated ids
    return inputIds.subList(inputIds.size() - nTokensToGenerate, inputIds.size());
  }

  public static void main(String[] args) throws IOException {
    if (!"UTF-8".equals(System.getProperty("file.encoding"))) {
      throw new IllegalArgumentException("Error: System file.encoding is not UTF-8");
    }

    // Create Options for command-line arguments
    Options options = new Options();
    options.addOption("p", "prompt", true, "Prompt string");
    options.addOption("n", "n-tokens-to-generate", true, "Number of tokens to generate");
    options.addOption("s", "model-size", true, "Model size");
    options.addOption("d", "models-dir", true, "Models directory");

    // Parse command-line arguments
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      String prompt = cmd.getOptionValue("p");
      int nTokensToGenerate = Integer.parseInt(cmd.getOptionValue("n", "40"));
      String modelSize = cmd.getOptionValue("s", "124M");
      String modelsDir = cmd.getOptionValue("d", "models");

      Path modelDir = Paths.get(modelsDir, modelSize);

      // Load encoder, hparams, and params from the released open-ai gpt-2 files
      Encoder encoder = Encoder.getEncoder(modelSize, modelsDir);
      Map<String, Object> hparams = loadHparamsJson(modelDir);
      Params params = Params.getParams(modelDir);

      // Encode the input string using the BPE tokenizer
      List<Integer> inputIds = encoder.encode(prompt);

      // Make sure we are not surpassing the max sequence length of our model
      assert inputIds.size() + nTokensToGenerate < (Integer) hparams.get("n_ctx") : "Length of inputIds + nTokensToGenerate should be less than n_ctx";

      // Generate output ids
      List<Integer> outputIds = (new Gpt2()).generate(inputIds, params, (Integer) hparams.get("n_head"), nTokensToGenerate);

      // Decode the ids back into a string
      String outputText = encoder.decode(outputIds);

      System.out.println(outputText);
    } catch (ParseException e) {
      System.err.println("Error parsing command line arguments: " + e.getMessage());
    }
  }
}