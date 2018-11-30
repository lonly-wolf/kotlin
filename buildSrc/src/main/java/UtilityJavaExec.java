import org.apache.tools.ant.types.Commandline;
import org.gradle.api.Incubating;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.process.JavaExecSpec;
import org.gradle.process.JavaForkOptions;
import org.gradle.process.ProcessForkOptions;
import org.gradle.process.internal.ExecActionFactory;
import org.gradle.process.internal.JavaExecAction;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This task is mostly copy of Gradle JavaExec task
 * We avoid using JavaExec tasks due to IDEA-200192:
 * IDEA makes all JavaExec tasks not up-to-date and attaches debugger making our breakpoints trigger during irrelevant task execution
 */
public class UtilityJavaExec extends ConventionTask implements JavaExecSpec {
    private final JavaExecAction javaExecHandleBuilder;

    public UtilityJavaExec() {
        javaExecHandleBuilder = getExecActionFactory().newJavaExecAction();
    }

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    public void exec() {
        setMain(getMain()); // make convention mapping work (at least for 'main'...
        setJvmArgs(getJvmArgs()); // ...and for 'jvmArgs')
        javaExecHandleBuilder.execute();
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllJvmArgs() {
        return javaExecHandleBuilder.getAllJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    public void setAllJvmArgs(List<String> arguments) {
        javaExecHandleBuilder.setAllJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public void setAllJvmArgs(Iterable<?> arguments) {
        javaExecHandleBuilder.setAllJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getJvmArgs() {
        return javaExecHandleBuilder.getJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    public void setJvmArgs(List<String> arguments) {
        javaExecHandleBuilder.setJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public void setJvmArgs(Iterable<?> arguments) {
        javaExecHandleBuilder.setJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec jvmArgs(Iterable<?> arguments) {
        javaExecHandleBuilder.jvmArgs(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec jvmArgs(Object... arguments) {
        javaExecHandleBuilder.jvmArgs(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getSystemProperties() {
        return javaExecHandleBuilder.getSystemProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void setSystemProperties(Map<String, ?> properties) {
        javaExecHandleBuilder.setSystemProperties(properties);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec systemProperties(Map<String, ?> properties) {
        javaExecHandleBuilder.systemProperties(properties);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec systemProperty(String name, Object value) {
        javaExecHandleBuilder.systemProperty(name, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public FileCollection getBootstrapClasspath() {
        return javaExecHandleBuilder.getBootstrapClasspath();
    }

    /**
     * {@inheritDoc}
     */
    public void setBootstrapClasspath(FileCollection classpath) {
        javaExecHandleBuilder.setBootstrapClasspath(classpath);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec bootstrapClasspath(Object... classpath) {
        javaExecHandleBuilder.bootstrapClasspath(classpath);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public String getMinHeapSize() {
        return javaExecHandleBuilder.getMinHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinHeapSize(String heapSize) {
        javaExecHandleBuilder.setMinHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    public String getDefaultCharacterEncoding() {
        return javaExecHandleBuilder.getDefaultCharacterEncoding();
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultCharacterEncoding(String defaultCharacterEncoding) {
        javaExecHandleBuilder.setDefaultCharacterEncoding(defaultCharacterEncoding);
    }

    /**
     * {@inheritDoc}
     */
    public String getMaxHeapSize() {
        return javaExecHandleBuilder.getMaxHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxHeapSize(String heapSize) {
        javaExecHandleBuilder.setMaxHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getEnableAssertions() {
        return javaExecHandleBuilder.getEnableAssertions();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnableAssertions(boolean enabled) {
        javaExecHandleBuilder.setEnableAssertions(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getDebug() {
        return javaExecHandleBuilder.getDebug();
    }

    /**
     * {@inheritDoc}
     */
    @Option(option = "debug-jvm", description = "Enable debugging for the process. The process is started suspended and listening on port 5005. [INCUBATING]")
    public void setDebug(boolean enabled) {
        javaExecHandleBuilder.setDebug(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public String getMain() {
        return javaExecHandleBuilder.getMain();
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setMain(String mainClassName) {
        javaExecHandleBuilder.setMain(mainClassName);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getArgs() {
        return javaExecHandleBuilder.getArgs();
    }

    /**
     * Parses an argument list from {@code args} and passes it to {@link #setArgs(List)}.
     *
     * <p>
     * The parser supports both single quote ({@code '}) and double quote ({@code "}) as quote delimiters.
     * For example, to pass the argument {@code foo bar}, use {@code "foo bar"}.
     * </p>
     * <p>
     * Note: the parser does <strong>not</strong> support using backslash to escape quotes. If this is needed,
     * use the other quote delimiter around it.
     * For example, to pass the argument {@code 'singly quoted'}, use {@code "'singly quoted'"}.
     * </p>
     *
     * @param args Args for the main class. Will be parsed into an argument list.
     * @return this
     * @since 4.9
     */
    @Incubating
    @Option(option = "args", description = "Command line arguments passed to the main class. [INCUBATING]")
    public UtilityJavaExec setArgsString(String args) {
        return setArgs(Arrays.asList(Commandline.translateCommandline(args)));
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setArgs(List<String> applicationArgs) {
        javaExecHandleBuilder.setArgs(applicationArgs);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setArgs(Iterable<?> applicationArgs) {
        javaExecHandleBuilder.setArgs(applicationArgs);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec args(Object... args) {
        javaExecHandleBuilder.args(args);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public JavaExecSpec args(Iterable<?> args) {
        javaExecHandleBuilder.args(args);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommandLineArgumentProvider> getArgumentProviders() {
        return javaExecHandleBuilder.getArgumentProviders();
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setClasspath(FileCollection classpath) {
        javaExecHandleBuilder.setClasspath(classpath);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec classpath(Object... paths) {
        javaExecHandleBuilder.classpath(paths);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public FileCollection getClasspath() {
        return javaExecHandleBuilder.getClasspath();
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec copyTo(JavaForkOptions options) {
        javaExecHandleBuilder.copyTo(options);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable @Optional @Input
    public String getExecutable() {
        return javaExecHandleBuilder.getExecutable();
    }

    /**
     * {@inheritDoc}
     */
    public void setExecutable(String executable) {
        javaExecHandleBuilder.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    public void setExecutable(Object executable) {
        javaExecHandleBuilder.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec executable(Object executable) {
        javaExecHandleBuilder.executable(executable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public File getWorkingDir() {
        return javaExecHandleBuilder.getWorkingDir();
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDir(File dir) {
        javaExecHandleBuilder.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDir(Object dir) {
        javaExecHandleBuilder.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec workingDir(Object dir) {
        javaExecHandleBuilder.workingDir(dir);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public Map<String, Object> getEnvironment() {
        return javaExecHandleBuilder.getEnvironment();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnvironment(Map<String, ?> environmentVariables) {
        javaExecHandleBuilder.setEnvironment(environmentVariables);
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec environment(String name, Object value) {
        javaExecHandleBuilder.environment(name, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec environment(Map<String, ?> environmentVariables) {
        javaExecHandleBuilder.environment(environmentVariables);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec copyTo(ProcessForkOptions target) {
        javaExecHandleBuilder.copyTo(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setStandardInput(InputStream inputStream) {
        javaExecHandleBuilder.setStandardInput(inputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public InputStream getStandardInput() {
        return javaExecHandleBuilder.getStandardInput();
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setStandardOutput(OutputStream outputStream) {
        javaExecHandleBuilder.setStandardOutput(outputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public OutputStream getStandardOutput() {
        return javaExecHandleBuilder.getStandardOutput();
    }

    /**
     * {@inheritDoc}
     */
    public UtilityJavaExec setErrorOutput(OutputStream outputStream) {
        javaExecHandleBuilder.setErrorOutput(outputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public OutputStream getErrorOutput() {
        return javaExecHandleBuilder.getErrorOutput();
    }

    /**
     * {@inheritDoc}
     */
    public JavaExecSpec setIgnoreExitValue(boolean ignoreExitValue) {
        javaExecHandleBuilder.setIgnoreExitValue(ignoreExitValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Input
    public boolean isIgnoreExitValue() {
        return javaExecHandleBuilder.isIgnoreExitValue();
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    public List<String> getCommandLine() {
        return javaExecHandleBuilder.getCommandLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommandLineArgumentProvider> getJvmArgumentProviders() {
        return javaExecHandleBuilder.getJvmArgumentProviders();
    }
}
