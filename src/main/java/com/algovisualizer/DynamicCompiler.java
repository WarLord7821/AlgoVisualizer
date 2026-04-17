package com.algovisualizer;

import com.algovisualizer.algorithms.Algorithm;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class DynamicCompiler {

    public Algorithm compileAndLoad(String userLogic, int[] targetArray) {
        try {
            // Create a temporary directory
            File tempDir = Files.createTempDirectory("dynamic_algo").toFile();
            tempDir.deleteOnExit();

            // Prepare the source code
            String className = "CustomAlgorithm";
            String sourceCode = "package com.algovisualizer;\n" +
                    "\n" +
                    "import com.algovisualizer.algorithms.Algorithm;\n" +
                    "import javafx.scene.layout.Pane;\n" +
                    "import com.algovisualizer.SortingCanvas;\n" +
                    "\n" +
                    "public class " + className + " implements Algorithm {\n" +
                    "\n" +
                    "    private int[] array;\n" +
                    "    private int delay = 100;\n" +
                    "    private Pane canvas;\n" +
                    "\n" +
                    "    public " + className + "(int[] array) {\n" +
                    "        this.array = array;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void execute() {\n" +
                    userLogic + "\n" +
                    "    }\n" +
                    "\n" +
                    "    private void swap(int i, int j) {\n" +
                    "        int temp = array[i];\n" +
                    "        array[i] = array[j];\n" +
                    "        array[j] = temp;\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateUI(int activeIndex1, int activeIndex2) {\n" +
                    "        if (canvas != null && canvas instanceof SortingCanvas) {\n" +
                    "            ((SortingCanvas) canvas).drawArray(array, activeIndex1, activeIndex2);\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    private void sleep() {\n" +
                    "        try {\n" +
                    "            Thread.sleep(delay);\n" +
                    "        } catch (InterruptedException e) {\n" +
                    "            Thread.currentThread().interrupt();\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void setSpeed(int delayMs) {\n" +
                    "        this.delay = delayMs;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void setCanvas(Pane canvas) {\n" +
                    "        this.canvas = canvas;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void run() {\n" +
                    "        execute();\n" +
                    "    }\n" +
                    "}\n";

            // Write source code to a file
            File sourceFile = new File(tempDir, className + ".java");
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode);
            }

            // Compile the source code
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new RuntimeException("System JavaCompiler is not available. Ensure you are running with a JDK, not a JRE.");
            }

            String classpath = System.getProperty("java.class.path");
            int result = compiler.run(null, null, null, "-cp", classpath, sourceFile.getPath());
            if (result != 0) {
                throw new RuntimeException("Compilation failed for CustomAlgorithm.");
            }

            // Load and instantiate the compiled class
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{tempDir.toURI().toURL()});
            Class<?> cls = Class.forName("com.algovisualizer." + className, true, classLoader);

            return (Algorithm) cls.getDeclaredConstructor(int[].class).newInstance(targetArray);

        } catch (Exception e) {
            throw new RuntimeException("Failed to compile or load dynamic algorithm", e);
        }
    }
}
