/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.generator.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.yaml.snakeyaml.Yaml;

import me.tabinol.secuboid.generator.common.BufferedWriterArray;
import me.tabinol.secuboid.generator.common.CommonGenerator;

public class ConfigGenerator extends CommonGenerator {

    private static final String TAG_GENERATED_VARS = "{{generatedVars}}";
    private static final String TAG_GENERATED_GETTERS = "{{generatedGetters}}";
    private static final String TAG_GENERATED_LOADS = "{{generatedLoads}}";

    private final List<ConfigRecord> configRecords;

    public ConfigGenerator(BuildContext buildContext, String source, String javaTemplate, String javaTarget) {
        super(buildContext, source, javaTemplate, new String[] { javaTarget });

        configRecords = new ArrayList<>();
    }

    @Override
    protected void generate(InputStream isSource, BufferedReader brJavaTemplate,
            BufferedWriterArray bufferedWriterArray) throws MojoExecutionException, IOException {
        BufferedWriter bwJavaTarget = bufferedWriterArray.getBwTargets()[0];
        Yaml yaml = new Yaml();
        Map<String, Object> node = yaml.load(isSource);
        loopRecord("", node);

        String line;
        while ((line = brJavaTemplate.readLine()) != null) {
            if (line.contains(TAG_GENERATED_VARS)) {
                generateVars(bwJavaTarget);
            } else if (line.contains(TAG_GENERATED_GETTERS)) {
                generateGetters(bwJavaTarget);
            } else if (line.contains(TAG_GENERATED_LOADS)) {
                generateLoads(bwJavaTarget);
            } else {
                bwJavaTarget.append(line);
                bwJavaTarget.newLine();
            }
        }

    }

    private void loopRecord(String pathParent, Map<String, Object> node) throws MojoExecutionException {
        for (Map.Entry<String, Object> entry : node.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            generateRecord(pathParent, key, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void generateRecord(String pathParent, String key, Object value) throws MojoExecutionException {
        String path = pathParent + key;

        if (value instanceof Map) {
            loopRecord(path + YAML_KEY_DELEMITER, (Map<String, Object>) value);
            return;
        }

        String varName = toCamelCase(path);
        String javaType;
        String getFunction;
        String defaultValue;

        if (value instanceof String valueStr) {
            javaType = "String";
            getFunction = "getString";
            defaultValue = getQuoteString(valueStr);
        } else if (value instanceof Integer) {
            javaType = "int";
            getFunction = "getInt";
            defaultValue = value.toString();
        } else if (value instanceof Double) {
            javaType = "double";
            getFunction = "getDouble";
            defaultValue = value + "D";
        } else if (value instanceof Boolean) {
            javaType = "boolean";
            getFunction = "getBoolean";
            defaultValue = value.toString();
        } else if (value instanceof List) {
            javaType = "List<String>";
            getFunction = "getStringList";
            defaultValue = getDefaultValueListString(key, (List<?>) value);
        } else {
            throw new MojoExecutionException(
                    String.format("Type not yet implemented [key=%s, value=%s, type=%s]", key, value,
                            value.getClass()));
        }

        configRecords.add(new ConfigRecord(javaType, varName, getFunction, path, defaultValue));
    }

    private String getDefaultValueListString(String key, List<?> valueList) throws MojoExecutionException {
        StringBuilder sb = new StringBuilder();
        sb.append("Arrays.asList(");
        boolean isFirst = true;

        for (Object object : valueList) {
            if (object == null) {
                throw new MojoExecutionException(
                        String.format("Null not supported in list [key=%s]", key));
            }

            if (!isFirst) {
                sb.append(", ");
            }

            sb.append(getQuoteString(object.toString()));
            isFirst = false;
        }

        return sb.append(')').toString();
    }

    private void generateVars(BufferedWriter bwJavaTarget) throws IOException {
        for (ConfigRecord configRecord : configRecords) {
            bwJavaTarget //
                    .append("    private ") //
                    .append(configRecord.javaType()) //
                    .append(' ') //
                    .append(configRecord.varName()) //
                    .append(';');

            bwJavaTarget.newLine();
        }
    }

    private void generateGetters(BufferedWriter bwJavaTarget) throws IOException {
        boolean isFirst = true;
        for (ConfigRecord configRecord : configRecords) {
            if (!isFirst) {
                bwJavaTarget.newLine();
            }

            bwJavaTarget //
                    .append("    public ") //
                    .append(configRecord.javaType()) //
                    .append(' ')
                    .append(configRecord.varName()) //
                    .append("() {");

            bwJavaTarget.newLine();

            bwJavaTarget //
                    .append("        return ") //
                    .append(configRecord.varName()) //
                    .append(';');

            bwJavaTarget.newLine();
            bwJavaTarget.append("    }");
            bwJavaTarget.newLine();

            isFirst = false;
        }
    }

    private void generateLoads(BufferedWriter bwJavaTarget) throws IOException {
        for (ConfigRecord configRecord : configRecords) {
            bwJavaTarget //
                    .append("        fileConfiguration.addDefault(\"") //
                    .append(configRecord.path()) //
                    .append("\", ") //
                    .append(configRecord.defaultValue()) //
                    .append(");");

            bwJavaTarget.newLine();

            bwJavaTarget //
                    .append("        ") //
                    .append(configRecord.varName()) //
                    .append(" = fileConfiguration.") //
                    .append(configRecord.getFunction()) //
                    .append("(\"") //
                    .append(configRecord.path()) //
                    .append("\");");

            bwJavaTarget.newLine();
        }
    }
}
