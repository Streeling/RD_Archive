package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Converted with ChatGPT from https://github.com/jaymody/picoGPT/blob/main/encoder.py
 */
public class Encoder {
  private Map<String, Integer> encoder;
  private Map<Integer, String> decoder;
  private String errors;
  private Map<Integer, String> byteEncoder;
  private Map<String, Integer> byteDecoder;
  private Map<List<String>, Integer> bpeRanks;
  private Map<String, String> cache;
  private Pattern pattern;

  public Encoder(Map<String, Integer> encoder, List<List<String>> bpeMerges, String errors) {
    this.encoder = encoder;
    this.decoder = new HashMap<>();
    for (Map.Entry<String, Integer> entry : encoder.entrySet()) {
      this.decoder.put(entry.getValue(), entry.getKey());
    }
    this.errors = errors;
    this.byteEncoder = bytesToUnicode();
    this.byteDecoder = new HashMap<>();
    for (Map.Entry<Integer, String> entry : this.byteEncoder.entrySet()) {
      this.byteDecoder.put(entry.getValue(), entry.getKey());
    }
    bpeRanks = new HashMap<>();
    for (int i = 0; i < bpeMerges.size(); i++) {
      bpeRanks.put(bpeMerges.get(i), i);
    }
    cache = new HashMap<>();

    pattern = Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+");
  }

  static Map<Integer, String> bytesToUnicode() {
    Map<Integer, String> map = new HashMap<>();

    for (int b = '!'; b <= '~'; b++) {
      map.put(b, String.valueOf((char) b));
    }
    for (int b = '¡'; b <= '¬'; b++) {
      map.put(b, String.valueOf((char) b));
    }
    for (int b = '®'; b <= 'ÿ'; b++) {
      map.put(b, String.valueOf((char) b));
    }

    int n = 0;
    for (int b = 0; b < 256; b++) {
      if (!map.containsKey(b)) {
        map.put(b, String.valueOf((char) (256 + n)));
        n++;
      }
    }

    return map;
  }

  static Set<List<String>> getPairs(List<String> word) {
    Set<List<String>> pairs = new LinkedHashSet<>();
    String prevChar = word.get(0);
    for (int i = 1; i < word.size(); i++) {
      String currChar = word.get(i);
      pairs.add(List.of(prevChar, currChar));
      prevChar = currChar;
    }

    return pairs;
  }

  public static Set<List<String>> getPairs(String word) {
    return getPairs(word.chars().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.toList()));
  }

  public String bpe(String token) {
    if (this.cache.containsKey(token)) {
      return this.cache.get(token);
    }
    List<String> word = token.chars()
        .mapToObj(c -> String.valueOf((char) c))
        .collect(Collectors.toList());
    Set<List<String>> pairs = getPairs(token);

    if (pairs.isEmpty()) {
      return token;
    }

    while (true) {
      List<String> bigram = pairs.stream()
          .min(Comparator.comparing(pair -> bpeRanks.getOrDefault(pair, Integer.MAX_VALUE)))
          .orElse(null);
      if (bigram == null || !bpeRanks.containsKey(bigram)) {
        break;
      }
      String first = bigram.get(0);
      String second = bigram.get(1);
      List<String> newWord = new ArrayList<>();
      int i = 0;
      while (i < word.size()) {
        int j = word.subList(i, word.size()).indexOf(first);
        if (j > -1) {
          newWord.addAll(word.subList(i, j).stream().map(String::valueOf).collect(Collectors.toList()));
          i = j;
        } else {
          newWord.addAll(word.subList(i, word.size()).stream().map(String::valueOf).collect(Collectors.toList()));
          break;
        }
        if (word.get(i).equals(first) && i < word.size() - 1 && word.get(i + 1).equals(second)) {
          newWord.add(first + second);
          i += 2;
        } else {
          newWord.add(String.valueOf(word.get(i)));
          i++;
        }
      }
      word = newWord;
      if (word.size() == 1) {
        break;
      } else {
        pairs = getPairs(word);
      }
    }
    String result = String.join(" ", word);
    this.cache.put(token, result);
    return result;
  }

  public List<Integer> encode(String text) {
    List<Integer> bpeTokens = new ArrayList<>();
    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      String token = matcher.group();
      StringBuilder builder = new StringBuilder();
      for (byte b : token.getBytes()) {
        builder.append(byteEncoder.get((int) b));
      }
      String encodedToken = builder.toString();
      for (String bpeToken : bpe(encodedToken).split(" ")) {
        bpeTokens.add(encoder.get(bpeToken));
      }
    }
    return bpeTokens;
  }

  public String decode(List<Integer> tokens) {
    StringBuilder text = new StringBuilder();
    for (Integer token : tokens) {
      text.append(decoder.get(token));
    }
    byte[] byteArray = new byte[text.length()];
    for (int i = 0; i < text.length(); i++) {
      byteArray[i] = byteDecoder.get(String.valueOf(text.charAt(i))).byteValue();
    }
    return new String(byteArray);
  }

  public Map<Integer, String> getDecoder() {
    return decoder;
  }

  public static Encoder getEncoder(String modelName, String modelsDir) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Path encoderFilePath = Paths.get(modelsDir, modelName, "encoder.json");
    Map<String, Integer> encoder = mapper.readValue(encoderFilePath.toFile(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class));
    Path bpeFilePath = Paths.get(modelsDir, modelName, "vocab.bpe");
    Scanner scanner = new Scanner(bpeFilePath);
    List<List<String>> bpeMerges = new ArrayList<>();
    // skip first line
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(" ");
      if (parts.length > 1) {
        bpeMerges.add(List.of(parts[0], parts[1]));
      }
    }
    scanner.close();
    return new Encoder(encoder, bpeMerges, "replace");
  }
}
