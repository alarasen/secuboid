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
package me.tabinol.secuboid.generator.flags;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.maven.plugin.MojoExecutionException;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.yaml.snakeyaml.Yaml;

import me.tabinol.secuboid.generator.common.BufferedWriterArray;
import me.tabinol.secuboid.generator.common.CommonGenerator;

public class FlagsGenerator extends CommonGenerator {

    private static final String TAG_GENERATED_FLAGS = "{{generatedFlags}}";

    // This static variable is a hack to be able to use it before the super()
    // constructor
    private static List<String> languages;

    private final List<FlagRecord> flagRecords;

    public FlagsGenerator(BuildContext buildContext, String source, String javaTemplate, String target,
            Map<String, String> languageToTarget) {
        super(buildContext, source, javaTemplate, createTargetArray(target, languageToTarget));

        flagRecords = new ArrayList<>();
    }

    private static String[] createTargetArray(String target, Map<String, String> languageToTarget) {
        languages = new ArrayList<>();
        List<String> targetList = new ArrayList<>();
        targetList.add(target);

        languageToTarget.forEach((k, v) -> {
            languages.add(k);
            targetList.add(v);
        });

        return targetList.toArray(new String[0]);
    }

    @Override
    protected void generate(InputStream isSource, BufferedReader brJavaTemplate,
            BufferedWriterArray bufferedWriterArray) throws MojoExecutionException, IOException {
        BufferedWriter[] bwTargets = bufferedWriterArray.getBwTargets();
        BufferedWriter bwJavaTarget = bwTargets[0];
        Yaml yaml = new Yaml();
        Map<String, Object> node = yaml.load(isSource);
        loopRecord(node);

        String line;
        while ((line = brJavaTemplate.readLine()) != null) {
            if (line.contains(TAG_GENERATED_FLAGS)) {
                generateFlags(bwJavaTarget);
            } else {
                bwJavaTarget.append(line);
                bwJavaTarget.newLine();
            }
        }

        for (int i = 0; i < languages.size(); i++) {
            String language = languages.get(0);
            BufferedWriter bwLangTarget = bwTargets[i + 1];
            generateLanguageFlags(language, bwLangTarget);
        }
    }

    private void loopRecord(Map<String, Object> node) throws MojoExecutionException {
        for (Map.Entry<String, Object> entry : node.entrySet()) {
            String flagName = entry.getKey();
            Object value = entry.getValue();
            generateRecord(flagName, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void generateRecord(String flagName, Object value) throws MojoExecutionException {
        if (!(value instanceof Map)) {
            throw new MojoExecutionException(String.format("The root key should be a map [flagName=%s]", flagName));
        }

        Map<String, Object> flagNode = (Map<String, Object>) value;

        Map<String, String> langToDescription = ((Map<String, Object>) flagNode.get("description")) //
                .entrySet() //
                .stream() //
                .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().toString()));

        boolean needSource = getOrDefault(flagNode, "need-source", true);
        boolean needTarget = getOrDefault(flagNode, "need-target", false);
        boolean needMetadata = getOrDefault(flagNode, "need-metadata", false);
        boolean isHidden = getOrDefault(flagNode, "hidden", false);

        FlagRecord flagRecord = new FlagRecord(flagName, langToDescription, needSource, needTarget, needMetadata,
                isHidden);
        flagRecords.add(flagRecord);
    }

    private void generateFlags(BufferedWriter bwJavaTarget) throws IOException {
        boolean isFirst = true;
        for (FlagRecord flagRecord : flagRecords) {
            if (!isFirst) {
                bwJavaTarget.newLine();
            }
            generateFlag(bwJavaTarget, flagRecord);
            isFirst = false;
        }
    }

    private void generateFlag(BufferedWriter bwJavaTarget, FlagRecord flagRecord) throws IOException {
        bwJavaTarget.append("    @FlagRegistered");
        bwJavaTarget.newLine();

        String nameUpper = toConstant("FLAG_" + flagRecord.name());
        bwJavaTarget //
                .append("    public static final FlagType ") //
                .append(nameUpper) //
                .append(" = new FlagType(\"") //
                .append(flagRecord.name()) //
                .append("\", \"") //
                .append(flagRecord.langToDescription().get("en")) //
                .append("\", ") //
                .append(Boolean.toString(flagRecord.needSource())) //
                .append(", ") //
                .append(Boolean.toString(flagRecord.needTarget())) //
                .append(", ") //
                .append(Boolean.toString(flagRecord.needMetadata())) //
                .append(", ") //
                .append(Boolean.toString(flagRecord.isHidden())) //
                .append(");");

        bwJavaTarget.newLine();
    }

    private void generateLanguageFlags(String language, BufferedWriter bwLangTarget) throws IOException {
        for (FlagRecord flagRecord : flagRecords) {
            String name = flagRecord.name();
            String description = flagRecord.langToDescription().get(language);
            if (description != null) {
                generateLanguageFlag(bwLangTarget, name, description);
            }
        }
    }

    private void generateLanguageFlag(BufferedWriter bwLangTarget, String name, String description) throws IOException {
        bwLangTarget //
                .append(name) //
                .append(": ") //
                .append(description);

        bwLangTarget.newLine();
    }

    public boolean getOrDefault(Map<String, Object> flagNode, String key, boolean defaultValue) {
        Object valueObj = flagNode.get(key);

        if (valueObj instanceof Boolean valueBool) {
            return valueBool;
        }

        return defaultValue;
    }
}
