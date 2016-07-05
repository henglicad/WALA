package com.ibm.wala.examples.drivers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cfg.IntraproceduralCFGs;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.CommandLine;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.io.FileUtil;
import com.ibm.wala.ipa.cfg.IntraproceduralCFGs;

public class IntraproceduralCFGsTest {

  public static String REGRESSION_EXCLUSIONS = "Java60RegressionExclusions.txt";

  public static void main(String[] args) throws WalaException, IOException {
    
    Properties p = CommandLine.parse(args);
    validateCommandLine(p);
    
    String appJar = p.getProperty("appJar");
    if (appJar != null && isDirectory(appJar)) {
      appJar = findJarFiles(new String[] { appJar });
    }
    
    String exclusionFile = p.getProperty("exclusions");

    String scopeFile = p.getProperty("scopeFile");
    
    AnalysisScope scope = buildAnalysisScope(appJar, scopeFile, exclusionFile);
    
    IntraproceduralCFGs introCFGs = new IntraproceduralCFGs(scope);
    introCFGs.printAllMethods();
  }
  
  public static AnalysisScope buildAnalysisScope(String appJar, String scopeFile, String exclusionFile) throws IOException {
    AnalysisScope scope = null;
    if (appJar != null) {
      scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar, exclusionFile != null ? new File(exclusionFile)
          : (new FileProvider()).getFile(REGRESSION_EXCLUSIONS));
    } else if (scopeFile != null) {      
      scope = AnalysisScopeReader.readJavaScope(scopeFile, exclusionFile != null ? new File(exclusionFile)
          : (new FileProvider()).getFile(REGRESSION_EXCLUSIONS), 
          IntraproceduralCFGs.class.getClassLoader());
    } else {
      System.out.println("Error: should specify either appJar or scopeFile");
      System.exit(1);
    }
    return scope;
  }
  
  public static void validateCommandLine(Properties p) {
    if (p.get("appJar") == null && p.get("scopeFile") == null) {
      throw new UnsupportedOperationException("expected command-line to include -appJar or -scopeFile");
    }
  }
  
  public static boolean isDirectory(String appJar) {
    return (new File(appJar).isDirectory());
  }

  public static String findJarFiles(String[] directories) throws WalaException {
    Collection<String> result = HashSetFactory.make();
    for (int i = 0; i < directories.length; i++) {
      for (Iterator<File> it = FileUtil.listFiles(directories[i], ".*\\.jar", true).iterator(); it.hasNext();) {
        File f = (File) it.next();
        result.add(f.getAbsolutePath());
      }
    }
    return composeString(result);
  }
  private static String composeString(Collection<String> s) {
    StringBuffer result = new StringBuffer();
    Iterator<String> it = s.iterator();
    for (int i = 0; i < s.size() - 1; i++) {
      result.append(it.next());
      result.append(File.pathSeparator);
    }
    if (it.hasNext()) {
      result.append(it.next());
    }
    return result.toString();
  }  

}
