/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple index that maps multiple file paths to a single patch key.
 *
 * @author Florian Zoubek
 *
 */
public class FileIndex {

  private Map<String, List<String>> attachmentIndex = new HashMap<>();

  private static final String PATCH_ENTRY_PATTERN = "\\[patch \"(.*)\"]";
  private final Pattern patchEntryPattern;
  private final String newLine;

  public FileIndex() {
    patchEntryPattern = Pattern.compile(PATCH_ENTRY_PATTERN);
    newLine = System.getProperty("line.separator");
  }

  /**
   * associates a new file path to a patch key
   *
   * @param key the patch key
   * @param filePath the file path to add
   */
  public void addEntry(String key, String filePath) {
    List<String> fileList = null;
    if (attachmentIndex.containsKey(key)) {
      fileList = attachmentIndex.get(key);
    } else {
      fileList = new ArrayList<String>();
      attachmentIndex.put(key, fileList);
    }

    if (!fileList.contains(filePath)) {
      fileList.add(filePath);
    }
  }

  /**
   * removes a file path from the given patch key
   *
   * @param key the patch key
   * @param filePath the file path to remove
   */
  public void removeEntry(String key, String filePath) {
    if (attachmentIndex.containsKey(key)) {
      List<String> fileList = attachmentIndex.get(key);
      fileList.remove(filePath);
    }
  }

  /**
   * reads the index from an input stream of text data. The file must be UTF-8
   * encoded and must contain the following format: a patch key is specified by
   * "[patch &lt;patchkey&gt]" followed by a new line, its corresponding file
   * paths are listed below this entry separated by new lines. Leading or
   * trailing spaces as well as empty lines will be ignored.
   *
   *
   * @param stream
   * @throws IOException
   */
  public void read(InputStream stream) throws IOException {
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(stream, StandardCharsets.UTF_8));
    String line = reader.readLine();
    String currentKey = null;
    while (line != null) {
      line = line.trim();
      if (!line.isEmpty()) {
        Matcher patchEntryMatcher = patchEntryPattern.matcher(line);
        if (patchEntryMatcher.matches()) {
          currentKey = patchEntryMatcher.group(1);
        } else {
          // line is the path of an attached file
          if (currentKey != null) {
            addEntry(currentKey, line);
          }
        }
      }
      line = reader.readLine();
    }
  }

  /**
   * retrieve a read only list containing all file paths of the attached files
   * associated patch with the given key
   *
   * @param key the key associated with the list of file paths
   * @return an unmodifiable list containing the file paths attached to the
   *         patch with the given key. An immutable empty list is returned if
   *         the key does not exist or no files have been attached to this patch
   */
  public List<String> get(String key) {
    List<String> filePaths = Collections.emptyList();

    if (attachmentIndex.containsKey(key)) {
      filePaths = Collections.unmodifiableList(attachmentIndex.get(key));
    }

    return filePaths;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (String key : attachmentIndex.keySet()) {
      result.append("[patch \"");
      result.append(key);
      result.append("\"]");
      result.append(newLine);

      for (String filePath : attachmentIndex.get(key)) {
        result.append(filePath);
        result.append(newLine);
      }

      result.append(newLine);
    }
    return result.toString();
  }

}
